package com.andy.meetquickly.activity.message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.GiftSelectAdapter;
import com.andy.meetquickly.adapter.ViewPagerAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.GiftBean;
import com.andy.meetquickly.bean.IsFriendBean;
import com.andy.meetquickly.bean.SelectGiftBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.showGiftMessage;
import com.andy.meetquickly.message.updatePicMessage;
import com.andy.meetquickly.utils.SoftHideKeyBoardUtil;
import com.andy.meetquickly.view.plugin.GiftMessage;
import com.andy.meetquickly.view.plugin.UserCardMessage;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

public class ChartActivity extends BaseActivity {

    @BindView(R.id.tv_gift_numb)
    TextView tvGiftNumb;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.constraint_gift)
    ConstraintLayout constraintGift;
    public int giftNumb = 1;
    public List<GiftBean> mData = new ArrayList<>();
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_add_friend)
    ImageView ivAddFriend;
    private List<View> mPagerList;//页面集合
    private LayoutInflater mInflater;
    private int currPage;
    /*总的页数*/
    private int pageCount;
    /*每一页显示的个数*/
    private int pageSize = 8;
    /*当前显示的是第几页*/
    private int curIndex = 0;
    private List<GiftSelectAdapter> giftSelectAdapters = new ArrayList<>();
    private String userId;
    private UserBean userBean;
    private int selectNumb = -1;
    private String title;

    @Override
    public int getLayoutId() {
        return R.layout.conversation;
    }

    @Override
    public void initView() {
        SoftHideKeyBoardUtil.assistActivity((Activity) mContext);
        userId = this.getIntent().getData().getQueryParameter("targetId");
        title = this.getIntent().getData().getQueryParameter("title");
        tvTitle.setText(title);
        userBean = UserCache.getInstance().getUser();
        EventBus.getDefault().register(this);
        initData();
        isFriend();
    }

    private void isFriend() {
        MQApi.isUserFriend(userBean.getUId(), userId, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<IsFriendBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<IsFriendBean>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<IsFriendBean>>(){});
                if (myApiResult.isOk()){
                    if ("0".equals(myApiResult.getData().getIsFriend())){
                        ivAddFriend.setVisibility(View.VISIBLE);
                    }else {
                        ivAddFriend.setVisibility(View.GONE);
                    }
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    public void initData() {
        MQApi.getGiftList(new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());

            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<GiftBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<GiftBean>>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<GiftBean>>>() {});
                if (myApiResult.isOk()) {
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        mData.add(myApiResult.getData().get(i));
                    }
                    initViewPager();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void initViewPager() {
        mInflater = LayoutInflater.from(mContext);
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mData.size() * 1.0 / pageSize);
        //viewpager
        mPagerList = new ArrayList<View>();
        for (int j = 0; j < pageCount; j++) {
            final RecyclerView recyclerView = (RecyclerView) mInflater.inflate(R.layout.layout_recycler_view, viewPager, false);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
            Integer fromIndex = j * pageSize;
            //如果总数少于PAGE_SIZE,为了防止数组越界,toIndex直接使用totalCount即可
            int toIndex = Math.min(mData.size(), (j + 1) * pageSize);

            final GiftSelectAdapter gridAdapter = new GiftSelectAdapter(mData.subList(fromIndex, toIndex), mContext);
            int finalJ = j;
            giftSelectAdapters.add(gridAdapter);
            gridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    setSelectGift(finalJ * pageSize + position);
                    selectNumb = finalJ * pageSize + position;
                    notifyAllAdapt();
                }
            });
            recyclerView.setAdapter(gridAdapter);
            mPagerList.add(recyclerView);
        }
        if (null != viewPager) {
            viewPager.setAdapter(new ViewPagerAdapter(mPagerList, mContext));
        }
    }

    private void notifyAllAdapt() {
        for (int i = 0; i < giftSelectAdapters.size(); i++) {
            giftSelectAdapters.get(i).notifyDataSetChanged();
        }
    }

    private void setSelectGift(int i) {
        for (int j = 0; j < mData.size(); j++) {
            if (j == i) {
                mData.get(j).setSelect(true);
            } else {
                mData.get(j).setSelect(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(showGiftMessage message) {
        constraintGift.setVisibility(View.VISIBLE);
    }

    public void sendGiftMessage() {
        final GiftMessage apkMessage = new GiftMessage();
        apkMessage.setContent("[礼物：" + mData.get(selectNumb).getName() + "]");
        Map<String, String> map = new HashMap<String, String>();
        SelectGiftBean selectGiftBean = new SelectGiftBean();
        selectGiftBean.setGiftNum(tvGiftNumb.getText().toString());
        selectGiftBean.setGiftData(mData.get(selectNumb));
        String s = JSON.toJSONString(selectGiftBean);
        apkMessage.setExtra(s);
        Message message = Message.obtain(userId, Conversation.ConversationType.PRIVATE, apkMessage);
        RongIM.getInstance().sendMessage(message, "[礼物]", null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_delete, R.id.iv_gift_add, R.id.iv_gift_cut, R.id.tv_send,R.id.iv_back, R.id.iv_add_friend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_add_friend:
                addFriend();
                break;
            case R.id.iv_delete:
                constraintGift.setVisibility(View.GONE);
                break;
            case R.id.iv_gift_add:
                giftNumb = Integer.parseInt(tvGiftNumb.getText().toString());
                if (giftNumb == 99) {
                    RxToast.showToast("最多赠送99个");
                    return;
                }
                giftNumb = giftNumb + 1;
                tvGiftNumb.setText(String.valueOf(giftNumb));
                break;
            case R.id.iv_gift_cut:
                giftNumb = Integer.parseInt(tvGiftNumb.getText().toString());
                if (giftNumb == 1) {
                    return;
                }
                giftNumb = giftNumb - 1;
                tvGiftNumb.setText(String.valueOf(giftNumb));
                break;
            case R.id.tv_send:
                sendGiftData();
                constraintGift.setVisibility(View.GONE);
                break;
        }
    }

    private void addFriend() {
        showWaitDialog(ChartActivity.this);
        MQApi.applyBecomeFriend(userBean.getUId(), userId, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());
//                        JSON.parseObject(s,new MyApiResult<String>().getClass());
                if (myApiResult.isOk()){
                    RxToast.showToast("已发出添加好友申请");
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void sendGiftData() {
        showWaitDialog(ChartActivity.this, "请稍后...");
        MQApi.giveGiftToUser(userBean.getUId(), userId, mData.get(selectNumb).getId(), tvGiftNumb.getText().toString(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<UserBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<UserBean>>(){}.getType());
//                        JSON.parseObject(s, new MyApiResult<UserBean>().getClass());
                if (myApiResult.isOk()) {
                    sendGiftMessage();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (200 == requestCode) {
            if (null != data) {
                sendUserCardMessage(data.getStringExtra("UserFriendBean"));
            }
        }
    }

    private void sendUserCardMessage(String source) {
        final UserCardMessage apkMessage = new UserCardMessage();
        apkMessage.setContent("[名片]");
        apkMessage.setExtra(source);
        Message message = Message.obtain(userId, Conversation.ConversationType.PRIVATE, apkMessage);
        RongIM.getInstance().sendMessage(message, "[名片]", null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

            }
        });
    }

}

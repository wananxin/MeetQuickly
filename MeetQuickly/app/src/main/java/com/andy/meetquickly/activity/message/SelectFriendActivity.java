package com.andy.meetquickly.activity.message;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.UserFriendAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.ShopInfoBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserFriendBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.view.indexbar.FloatingBarItemDecoration;
import com.andy.meetquickly.view.indexbar.IndexBar;
import com.andy.meetquickly.view.indexbar.PinnedSectionDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qdx.indexbarlayout.IndexLayout;

public class SelectFriendActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    //    @BindView(R.id.share_add_contact_sidebar)
//    IndexBar shareAddContactSidebar;
    private LinearLayoutManager mLayoutManager;
    private LinkedHashMap<Integer, String> mHeaderList;
    private ArrayList<UserFriendBean> mData = new ArrayList<>();
    private UserFriendAdapter adapter;
    private IndexLayout shareAddContactSidebar;
    private UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_friend;
    }

    @Override
    public void initView() {
        headNotify();
        setBarTitle("选择好友");
        userBean = UserCache.getInstance().getUser();
        shareAddContactSidebar = (IndexLayout) findViewById(R.id.share_add_contact_sidebar);
        mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(
//                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(
                new FloatingBarItemDecoration(this, new FloatingBarItemDecoration.DecorationCallback() {
                    @Override
                    public long getGroupId(int position) {
                        return Character.toUpperCase(mData.get(position).getNickName().charAt(0));
                    }

                    @Override
                    public String getGroupFirstLine(int position) {
                        return  mData.get(position).getNickName().substring(0, 1).toUpperCase();
                    }
                }));
//        recyclerView.addItemDecoration(new PinnedSectionDecoration(mContext, new PinnedSectionDecoration.DecorationCallback() {
//            @Override
//            public long getGroupId(int position) {
//                return Character.toUpperCase(mData.get(position).getNickName().charAt(0));
//            }
//
//            @Override
//            public String getGroupFirstLine(int position) {
//                return  mData.get(position).getNickName().substring(0, 1).toUpperCase();
//            }
//        }));
        adapter = new UserFriendAdapter(mContext, LayoutInflater.from(this),
                mData);
        adapter.setOnContactsBeanClickListener(new UserFriendAdapter.OnContactsBeanClickListener() {
            @Override
            public void onContactsBeanClicked(UserFriendBean bean) {
                Intent intent = new Intent();
                intent.putExtra("UserFriendBean", JSON.toJSONString(bean));
                setResult(Activity.RESULT_OK, intent);
                finish();//结束之后会将结果传回From
            }
        });
        recyclerView.setAdapter(adapter);
        initData();
    }

    @Override
    public void initData() {
        String Uid;
        if (null != userBean){
            Uid = userBean.getUId();
        }else {
            Uid = "";
        }
        MQApi.getUserFriendList(Uid, "", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<UserFriendBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<UserFriendBean>>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<UserFriendBean>>>() {});
                if (myApiResult.isOk()) {
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        UserFriendBean userFriendBean = myApiResult.getData().get(i);
                        userFriendBean.setNickName(userFriendBean.getNickName());
                        mData.add(userFriendBean);
                    }
                    adapter.notifyDataSetChanged();
                    headNotify();
                    if (mData.size() > 0) {
                        shareAddContactSidebar.getIndexBar().setIndexsList(new ArrayList<>(mHeaderList.values()));
                        shareAddContactSidebar.setIndexBarHeightRatio(0.9f);
                        shareAddContactSidebar.setCircleColor(Color.RED);
                        shareAddContactSidebar.setCircleRadius(100);
                        shareAddContactSidebar.setCircleDuration(500);
                        shareAddContactSidebar.setCircleColor(ContextCompat.getColor(mContext, R.color.circle_bg));
                        shareAddContactSidebar.getIndexBar().setIndexChangeListener(new qdx.indexbarlayout.IndexBar.IndexChangeListener(){
                            @Override
                            public void indexChanged(String indexName) {
                                for (int i = 0; i < mData.size(); i++) {
                                    if (indexName.equals(mData.get(i).getInitial())) {
                                        mLayoutManager.scrollToPositionWithOffset(i, 0);
                                        return;
                                    }
                                }
                            }
                        });
                    }
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    public static void start(Activity activity) {
        Intent starter = new Intent(activity, SelectFriendActivity.class);
        activity.startActivityForResult(starter,200);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void headNotify() {
        if (mHeaderList == null) {
            mHeaderList = new LinkedHashMap<>();
        }
        mHeaderList.clear();
        if (mData.size() == 0) {
            return;
        }
        addHeaderToList(0, mData.get(0).getInitial());
        for (int i = 1; i < mData.size(); i++) {
            if (!mData.get(i - 1).getInitial().equalsIgnoreCase(mData.get(i).getInitial())) {
                addHeaderToList(i, mData.get(i).getInitial());
            }
        }
    }

    private void addHeaderToList(int index, String header) {
        mHeaderList.put(index, header);
    }

}

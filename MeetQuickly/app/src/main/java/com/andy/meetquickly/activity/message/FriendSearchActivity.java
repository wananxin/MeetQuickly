package com.andy.meetquickly.activity.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.MyFraiendAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserFriendBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.fragment.message.FriendFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.KeyboardUtils;
import com.andy.meetquickly.utils.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

public class FriendSearchActivity extends BaseActivity {

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ArrayList<UserFriendBean> mData = new ArrayList<>();
    private MyFraiendAdapter adapter;
    private UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_friend_search;
    }

    @Override
    public void initView() {
        userBean = UserCache.getInstance().getUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyFraiendAdapter(mContext,mData);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RongIM.getInstance().startPrivateChat(FriendSearchActivity.this, mData.get(position).getUid(), mData.get(position).getNickName());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initData() {
        if (!StringUtil.isNotNull(etContent.getText().toString())) {
            RxToast.showToast("请输入搜索内容");
            return;
        }

        showWaitDialog(FriendSearchActivity.this);
        MQApi.getUserFriendList(userBean.getUId(), etContent.getText().toString(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<UserFriendBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<UserFriendBean>>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<UserFriendBean>>>() {});
                if (myApiResult.isOk()) {
                    mData.clear();
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        UserFriendBean userFriendBean = myApiResult.getData().get(i);
                        mData.add(userFriendBean);
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.textViewSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.textViewSearch:
                initData();
                KeyboardUtils.hideSoftInput(FriendSearchActivity.this);
                break;
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, FriendSearchActivity.class);
        context.startActivity(starter);
    }
}

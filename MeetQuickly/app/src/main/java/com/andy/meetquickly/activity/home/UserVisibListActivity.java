package com.andy.meetquickly.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.UserVisitAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserFriendBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
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

public class UserVisibListActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private int type;   //1为用户最近访问的人  2为网店休后的人
    private String mId;  //为用户ID或者网店ID
    private int mPage = 1;
    private UserBean userBean;
    List<UserFriendBean> mData = new ArrayList<>();
    private UserVisitAdapter adapter;
    private View notDataView;
    private View errorView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_visib_list;
    }

    @Override
    public void initView() {
        setBarTitle("最近访问");
        type = this.getIntent().getIntExtra("type",1);
        mId = this.getIntent().getStringExtra("mId");
        userBean = UserCache.getInstance().getUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new UserVisitAdapter(mData,mContext,userBean.getUId());
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (!"1".equals(mData.get(position).getIsAttention())){
                    followUser(position);
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserDataActivity.start(mContext,mData.get(position).getUid());
            }
        });
        recyclerView.setAdapter(adapter);
        notDataView = getLayoutInflater().inflate(R.layout.layout_empty_view, (ViewGroup) recyclerView.getParent(), false);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        errorView = getLayoutInflater().inflate(R.layout.layout_error_view, (ViewGroup) recyclerView.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        initData();
    }

    @Override
    public void initData() {
        showWaitDialog(UserVisibListActivity.this);
        if (type == 1){
            MQApi.getUserVisitLists(mId, mPage, userBean.getUId(), new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    dissmissWaitDialog();
                    RxToast.showToast(e.toString());
                    adapter.setEmptyView(errorView);
                }

                @Override
                public void onSuccess(String s) {
                    dissmissWaitDialog();
                    MyApiResult<List<UserFriendBean>> myApiResult =
                            new Gson().fromJson(s, new TypeToken<MyApiResult<List<UserFriendBean>>>(){}.getType());
                    if (myApiResult.isOk()){
                        for (int i = 0; i < myApiResult.getData().size(); i++) {
                            mData.add(myApiResult.getData().get(i));
                        }
                        adapter.notifyDataSetChanged();
                        if (mData.size() == 0){
                            adapter.setEmptyView(notDataView);
                        }
                    }else {
                        RxToast.showToast(myApiResult.getMsg());
                        adapter.setEmptyView(errorView);
                    }
                }
            });
        }else {
            MQApi.getShopVisitLists(userBean.getUId(), mPage, mId, new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    dissmissWaitDialog();
                    RxToast.showToast(e.toString());
                    adapter.setEmptyView(errorView);
                }

                @Override
                public void onSuccess(String s) {
                    dissmissWaitDialog();
                    MyApiResult<List<UserFriendBean>> myApiResult =
                            new Gson().fromJson(s, new TypeToken<MyApiResult<List<UserFriendBean>>>(){}.getType());
                    if (myApiResult.isOk()){
                        for (int i = 0; i < myApiResult.getData().size(); i++) {
                            mData.add(myApiResult.getData().get(i));
                        }
                        adapter.notifyDataSetChanged();
                        if (mData.size() == 0){
                            adapter.setEmptyView(notDataView);
                        }
                    }else {
                        RxToast.showToast(myApiResult.getMsg());
                        adapter.setEmptyView(errorView);
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public static void start(Context context,int type,String mId) {
        Intent starter = new Intent(context, UserVisibListActivity.class);
        starter.putExtra("type",type);
        starter.putExtra("mId",mId);
        context.startActivity(starter);
    }

    private void followUser(int positon) {
        showWaitDialog(UserVisibListActivity.this);
        MQApi.followUser(userBean.getUId(), mId, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());
                if (myApiResult.isOk()){
                    mData.get(positon).setIsAttention("1");
                    adapter.notifyItemChanged(positon);
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }
}

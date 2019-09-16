package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.EventCenterAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.EventCenterBean;
import com.andy.meetquickly.bean.UserBean;
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

public class EventCenterActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    EventCenterAdapter adapter;
    List<EventCenterBean> mData = new ArrayList<>();
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_event_center;
    }

    @Override
    public void initView() {
        setBarTitle("活动");
        userBean = UserCache.getInstance().getUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new EventCenterAdapter(mData, EventCenterActivity.this);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WebViewActivity.start(EventCenterActivity.this, "活动", mData.get(position).getContent());
            }
        });
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        showWaitDialog(EventCenterActivity.this);
        initData();

    }

    @Override
    public void initData() {
        MQApi.getActityList(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<EventCenterBean>> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<List<EventCenterBean>>>() {
                }.getType());
                swipeRefreshLayout.setRefreshing(false);
                if (myApiResult.isOk()) {
                    mData.clear();
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        mData.add(myApiResult.getData().get(i));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, EventCenterActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

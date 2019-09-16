package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.UserDataActivity;
import com.andy.meetquickly.adapter.InvitationPeopleAdapter;
import com.andy.meetquickly.adapter.WithProfitAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBaseBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.fragment.partner.PartnerTypeFragment;
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
import io.rong.imkit.RongIM;

public class UserInvitationPeopleActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private String numb;

    InvitationPeopleAdapter adapter;
    List<UserBaseBean> mData = new ArrayList<>();

    private View notDataView;
    private View errorView;
    private int mPage = 1;
    UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_invitation_people;
    }

    @Override
    public void initView() {
        numb = this.getIntent().getStringExtra("numb");
        setBarTitle("已邀请（" + numb + "）");
        userBean = UserCache.getInstance().getUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new InvitationPeopleAdapter(mData, mContext);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_user_head){
                    UserDataActivity.start(mContext,mData.get(position).getUid());
                }
            }
        });

        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPage = mPage + 1;
                initData();
            }
        }, recyclerView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
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

    public void refreshData() {
        mPage = 1;
        initData();
    }

    @Override
    public void initData() {
        MQApi.getUserInviteList(userBean.getUId(), mPage, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                adapter.loadMoreFail();
                swipeRefreshLayout.setRefreshing(false);
                RxToast.showToast(e.toString());
                adapter.setEmptyView(errorView);
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<UserBaseBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<UserBaseBean>>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<WithProfitBean>>>(){});
                if (myApiResult.isOk()) {
                    if (mPage == 1) {
                        mData.clear();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    if (myApiResult.isIsLastPage()) {
                        adapter.setEnableLoadMore(false);
                    } else {
                        adapter.setEnableLoadMore(true);
                        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                            @Override
                            public void onLoadMoreRequested() {
                                mPage = mPage + 1;
                                initData();
                            }
                        }, recyclerView);
                    }
                    adapter.loadMoreComplete();
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        mData.add(myApiResult.getData().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    if (mData.size() == 0) {
                        adapter.setEmptyView(notDataView);
                    }
                } else {
                    adapter.loadMoreFail();
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.setEmptyView(errorView);
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

    public static void start(Context context, String numb) {
        Intent starter = new Intent(context, UserInvitationPeopleActivity.class);
        starter.putExtra("numb", numb);
        context.startActivity(starter);
    }
}

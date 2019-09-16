package com.andy.meetquickly.fragment.profit;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.user.manager.ManageOrderDetailActivity;
import com.andy.meetquickly.adapter.CouponProfitAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.OrderDetailBean;
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
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CouponProfitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CouponProfitFragment extends BaseLazyFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    int mPage = 1;
    CouponProfitAdapter adapter;
    List<OrderDetailBean> mData = new ArrayList<>();
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private View notDataView;
    private View errorView;
    UserBean userBean;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private int mParam;


    public CouponProfitFragment() {
        // Required empty public constructor
    }

    @Override
    public void fetchData() {
        userBean = UserCache.getInstance().getUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CouponProfitAdapter(mData, getContext());
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_button) {
                    if (mParam == 1) {
//                        RongIM.getInstance().startPrivateChat(CouponProfitFragment.this.getContext(), mData.get(position).getUid(), mData.get(position).getNickName());
                    } else {
                        ManageOrderDetailActivity.start(CouponProfitFragment.this.getContext(), mData.get(position));
                    }
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

    private void initData() {
        if (mParam == 1) {

        } else {
            MQApi.getOrderCouponDetailList(userBean.getUId(), mPage, new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    RxToast.showToast(e.toString());
                    adapter.loadMoreFail();
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.setEmptyView(errorView);
                }

                @Override
                public void onSuccess(String s) {
                    MyApiResult<List<OrderDetailBean>> myApiResult =
                            new Gson().fromJson(s, new TypeToken<MyApiResult<List<OrderDetailBean>>>() {
                            }.getType());
//                            JSON.parseObject(s, new TypeReference<MyApiResult<List<OtherProfitBean>>>() {});
                    if (myApiResult.isOk()) {
                        if (mPage == 1) {
                            mData.clear();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        if (myApiResult.isIsLastPage()) {
                            adapter.setEnableLoadMore(false);
                        }else {
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
                            textView.setVisibility(View.GONE);
                        } else {
                            textView.setVisibility(View.VISIBLE);
                            if (mParam == 1) {
                                textView.setText("礼物收益：" + mData.get(0).getAmountTotal());
                            } else {
                                textView.setText("代金券收益：" + mData.get(0).getAmountTotal());
                            }
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
    }

    @Override
    public void doItEvery() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param Parameter .
     * @return A new instance of fragment WithProfitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CouponProfitFragment newInstance(int param) {
        CouponProfitFragment fragment = new CouponProfitFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getInt(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_with_profit, container, false);
        view.setTag(mParam - 1);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

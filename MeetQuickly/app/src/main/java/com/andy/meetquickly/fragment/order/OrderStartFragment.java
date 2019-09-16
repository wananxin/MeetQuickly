package com.andy.meetquickly.fragment.order;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.user.OrderDetailActivity;
import com.andy.meetquickly.adapter.OrderStartAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.OrderDetailBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.reachStoreMessage;
import com.andy.meetquickly.utils.DaoHangUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.SelectDialog;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderStartFragment extends BaseLazyFragment {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    OrderStartAdapter adapter;
    List<OrderDetailBean> mData = new ArrayList<>();
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private UserBean userBean;
    private int mPage = 1;
    private View notDataView;
    private View errorView;

    public OrderStartFragment() {
        // Required empty public constructor
    }

    @Override
    public void fetchData() {
        initData();
    }

    private void initData() {
        MQApi.getUserOrderInfo(userBean.getUId(), 1, 2, mPage, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                adapter.loadMoreFail();
                swipeRefreshLayout.setRefreshing(false);
                RxToast.showToast(e.toString());
                adapter.setEmptyView(errorView);
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<OrderDetailBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<OrderDetailBean>>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<OrderDetailBean>>>(){});
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
                    RxToast.showToast(myApiResult.getMsg());
                    adapter.setEmptyView(errorView);
                }
            }
        });
    }

    private void showOkDialog(int position) {
        SelectDialog.show(OrderStartFragment.this.getActivity(), "温馨提示", "确认到店后，您使用的代金券将无法退回 结账时请与接待经理联系抵扣您使用的代金券", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reachStore(position);
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCanCancel(true);
    }

    private void reachStore(int position) {
        showWaitDialog(OrderStartFragment.this.getActivity());
        MQApi.userReachStore(mData.get(position).getId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                        }.getType());
//                        JSON.parseObject(s,new MyApiResult<String>().getClass());
                if (myApiResult.isOk()) {
                    mData.get(position).setReachStore("2");
                    adapter.notifyItemChanged(position);
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void showCancelDialoh(int position) {
        SelectDialog.show(OrderStartFragment.this.getActivity(), "提示", "是否取消订单", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelOrderDialog(position);
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCanCancel(true);
    }

    private void cancelOrderDialog(int position) {
        showWaitDialog(OrderStartFragment.this.getActivity());
        MQApi.userCancelOrder(mData.get(position).getId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                        }.getType());
//                        JSON.parseObject(s,new MyApiResult<String>().getClass());
                if (myApiResult.isOk()) {
                    mData.remove(position);
                    adapter.notifyDataSetChanged();
                    RxToast.showToast("订单取消成功");
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void showPhoneDialog(int position) {
        String msg = mData.get(position).getStoreName() + "经理:" + mData.get(position).getStoreMobile();
        SelectDialog.show(OrderStartFragment.this.getActivity(), "拨打接待经理电话", msg, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RxDeviceTool.dial(OrderStartFragment.this.getContext(), mData.get(position).getStoreMobile());
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCanCancel(true);

    }

    @Override
    public void doItEvery() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        view.setTag(0);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(reachStoreMessage message) {
        int position = 0;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getOrderId().equals(message.getMessage())) {
                mData.get(i).setReachStore("2");
                position = i;
            }
        }
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        userBean = UserCache.getInstance().getUser();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderStartFragment.this.getContext()));
        adapter = new OrderStartAdapter(mData, OrderStartFragment.this.getContext());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderDetailActivity.start(OrderStartFragment.this.getContext(), mData.get(position));
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_navigation:
                        if (StringUtil.isNotNull(RxSPTool.getContent(OrderStartFragment.this.getContext(), Constant.KEY_LATITUDE))) {
                            showDaoHangDialog(mData.get(position).getLatitude(),
                                    mData.get(position).getLongitude(),
                                    mData.get(position).getCompanyName());
                        } else {
                            RxToast.showToast("您未打开定位权限");
                        }
                        break;
                    case R.id.iv_phone:
                        showPhoneDialog(position);
                        break;
                    case R.id.tv_cancel:
                        showCancelDialoh(position);
                        break;
                    case R.id.tv_ok:
                        if ("1".equals(mData.get(position).getReachStore())) {
                            showOkDialog(position);
                        }
                        break;
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
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    public void refreshData() {
        mPage = 1;
        initData();
    }

    private void showDaoHangDialog(String latitude, String longitude, String companyName) {
        SelectDialog.show(OrderStartFragment.this.getContext(), "导航", "是否导航到：" + companyName, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (StringUtil.isNotNull(RxSPTool.getString(OrderStartFragment.this.getContext(), Constant.KEY_LATITUDE))) {
                    DaoHangUtil.getApi(OrderStartFragment.this.getContext(), latitude,
                            longitude,
                            companyName,
                            RxSPTool.getString(OrderStartFragment.this.getContext(), Constant.KEY_LATITUDE),
                            RxSPTool.getString(OrderStartFragment.this.getContext(), Constant.KEY_LONGITUDE));
                } else {
                    RxToast.showToast("请打开应用定位权限并重启应用");
                }
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

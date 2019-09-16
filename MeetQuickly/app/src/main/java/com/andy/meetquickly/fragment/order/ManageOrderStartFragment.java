package com.andy.meetquickly.fragment.order;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.user.manager.ManageOrderDetailActivity;
import com.andy.meetquickly.activity.user.manager.UnpaidOrderActivity;
import com.andy.meetquickly.activity.user.manager.UploadBillActivity;
import com.andy.meetquickly.adapter.ManageOrderAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.OrderDetailBean;
import com.andy.meetquickly.bean.PayResult;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.WechartPayBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.wechartSuccessMessage;
import com.andy.meetquickly.utils.LogUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageOrderStartFragment extends BaseLazyFragment {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    ManageOrderAdapter adapter;
    List<OrderDetailBean> mData = new ArrayList<>();
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private UserBean userBean;
    private int mPage = 1;
    private View notDataView;
    private View errorView;
    private int mSelectOrder;

    public ManageOrderStartFragment() {
        // Required empty public constructor
    }

    @Override
    public void fetchData() {
        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {
        MQApi.getUserOrderInfo(userBean.getUId(), 2, 2, mPage, new SimpleCallBack<String>() {
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

    @Override
    public void doItEvery() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(wechartSuccessMessage message) {
        RxToast.showToast("支付成功");
        mData.get(mSelectOrder).setIsPayment("2");
        adapter.notifyItemChanged(mSelectOrder);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_start, container, false);
        view.setTag(0);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(ManageOrderStartFragment.this.getContext()));
        adapter = new ManageOrderAdapter(mData, ManageOrderStartFragment.this.getContext(), 1);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ManageOrderDetailActivity.start(ManageOrderStartFragment.this.getContext(), mData.get(position));
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_manage_bt) {
//                    if (!"2".equals(mData.get(position).getIsPayment())) {
                    if ("0".equals(mData.get(position).getManagerVerify())) {
                        UploadBillActivity.start(ManageOrderStartFragment.this.getContext(), mData.get(position));
                    } else {
                        RxToast.showToast("您已上传账单，无需重复上传");
                    }
//                    }
                } else if (view.getId() == R.id.tv_manage_pay) {
                    if (!"2".equals(mData.get(position).getIsPayment())) {
                        mSelectOrder = position;
                        showSelectPayType(position);
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
    }

    public void refreshData() {
        mPage = 1;
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    CustomDialog customDialog;
    private int payType = 1; //1为微信支付，2为阿里支付
    private static final int SDK_PAY_FLAG = 1;

    private void showSelectPayType(int position) {
        customDialog = CustomDialog.show(ManageOrderStartFragment.this.getContext(), R.layout.dialog_select_pay_type, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
                LinearLayout ll_wechart = rootView.findViewById(R.id.ll_wechart);
                LinearLayout ll_alipay = rootView.findViewById(R.id.ll_alipay);
                RadioButton rb_wechart = rootView.findViewById(R.id.rb_wechart);
                RadioButton rb_alipay = rootView.findViewById(R.id.rb_alipay);
                if (payType == 1) {
                    rb_wechart.setChecked(false);
                    rb_alipay.setChecked(true);
                } else {
                    rb_wechart.setChecked(true);
                    rb_alipay.setChecked(false);
                }
                TextView tv_cancel = rootView.findViewById(R.id.tv_cancel);
                TextView tv_sure = rootView.findViewById(R.id.tv_sure);
                ll_wechart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payType = 2;
                        rb_wechart.setChecked(true);
                        rb_alipay.setChecked(false);
                    }
                });
                ll_alipay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payType = 1;
                        rb_wechart.setChecked(false);
                        rb_alipay.setChecked(true);
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                    }
                });
                tv_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                        getPayInfo(position);
                    }
                });
            }
        }).setCanCancel(true);
    }

    private void getPayInfo(int position) {
        showWaitDialog(ManageOrderStartFragment.this.getActivity());
        String orderIds = mData.get(position).getOrderId();
        MQApi.managerPayOrders(userBean.getUId(), payType, mData.get(position).getPaymentAmount(), orderIds, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                if (payType == 1) {
                    MyApiResult<String> myApiResult =
                            new Gson().fromJson(s, new TypeToken<MyApiResult<String>>() {
                            }.getType());
//                            JSON.parseObject(s,new MyApiResult<String>().getClass());
                    if (myApiResult.isOk()) {
                        startAliPay(myApiResult.getData());
                    } else {
                        RxToast.showToast(myApiResult.getMsg());
                    }
                } else {
                    MyApiResult<WechartPayBean> myApiResult =
                            new Gson().fromJson(s, new TypeToken<MyApiResult<WechartPayBean>>() {
                            }.getType());
//                            JSON.parseObject(s, new TypeReference<MyApiResult<WechartPayBean>>(){});
                    if (myApiResult.isOk()) {
                        startWechartPay(myApiResult.getData());
                    } else {
                        RxToast.showToast(myApiResult.getMsg());
                    }
                }
            }
        });
    }

    private void startAliPay(String data) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ManageOrderStartFragment.this.getActivity());
                Map<String, String> result = alipay.payV2(data, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if ("9000".equals(resultStatus)) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                LogUtil.e("支付成功" + payResult);
                mData.get(mSelectOrder).setIsPayment("2");
                adapter.notifyItemChanged(mSelectOrder);
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                LogUtil.e("支付失败" + payResult);
            }
        }

        ;
    };

    //初始化微信支付
    private IWXAPI msgApi;

    private void startWechartPay(WechartPayBean data) {
        msgApi = WXAPIFactory.createWXAPI(ManageOrderStartFragment.this.getContext(), Constant.WECHART_ID);
        PayReq request = new PayReq();
        request.appId = data.getAppid();
        request.partnerId = data.getPartnerid();
        request.prepayId = data.getPrepayid();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = data.getNoncestr();
        request.timeStamp = data.getTimestamp();
        request.sign = data.getSign();
        msgApi.sendReq(request);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

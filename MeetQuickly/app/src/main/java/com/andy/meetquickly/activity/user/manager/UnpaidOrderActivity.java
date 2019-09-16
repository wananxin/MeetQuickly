package com.andy.meetquickly.activity.user.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alipay.sdk.app.PayTask;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.user.OpenPartnerActivity;
import com.andy.meetquickly.activity.user.partner.PartnerActivity;
import com.andy.meetquickly.adapter.UnpayOrderAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.PayResult;
import com.andy.meetquickly.bean.UnpayOrderBean;
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
import butterknife.OnClick;

public class UnpaidOrderActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.textViewPrice)
    TextView textViewPrice;
    private UserBean userBean;
    private List<UnpayOrderBean> mData = new ArrayList<>();
    private UnpayOrderAdapter adapter;
    private double calculationMoney;
    private int selectType = 0;
    CustomDialog customDialog;
    private int payType = 1; //1为微信支付，2为阿里支付
    private static final int SDK_PAY_FLAG = 1;
    //初始化微信支付
    private IWXAPI msgApi;

    @Override
    public int getLayoutId() {
        return R.layout.activity_unpaid_order;
    }

    @Override
    public void initView() {
        setBarTitle("未支付订单");
        EventBus.getDefault().register(this);
        userBean = UserCache.getInstance().getUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new UnpayOrderAdapter(mData);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mData.get(position).isSelect()) {
                    mData.get(position).setSelect(false);
                } else {
                    mData.get(position).setSelect(true);
                }
                boolean isAllSelect = true;
                for (int i = 0; i < mData.size(); i++) {
                    if (!mData.get(i).isSelect()){
                        checkBox.setChecked(false);
                        selectType = 1;
                        isAllSelect = false;
                    }
                }
                checkBox.setChecked(isAllSelect);
                adapter.notifyDataSetChanged();
                calculation();
            }
        });
        recyclerView.setAdapter(adapter);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    for (int i = 0; i < mData.size(); i++) {
                        mData.get(i).setSelect(true);
                    }
                    adapter.notifyDataSetChanged();
                    calculation();
                }else {
                    if (selectType == 0) {
                        for (int i = 0; i < mData.size(); i++) {
                            mData.get(i).setSelect(false);
                        }
                        adapter.notifyDataSetChanged();
                        calculation();
                    }else {
                        selectType = 0;
                    }
                }
            }
        });
        textViewPrice.setText("￥0");
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(wechartSuccessMessage message) {
        RxToast.showToast("支付成功");
        finish();
    }

    public void calculation(){
        double money = 0;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isSelect()){
                money = money + Double.parseDouble(mData.get(i).getPaymentAmount());
            }
        }
        calculationMoney = money;
        textViewPrice.setText("￥" + money);
    }

    @Override
    public void initData() {
        showWaitDialog(UnpaidOrderActivity.this);
        MQApi.getManagerUnpaid(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<UnpayOrderBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<UnpayOrderBean>>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<UnpayOrderBean>>>(){});
                if (myApiResult.isOk()) {
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
        Intent starter = new Intent(context, UnpaidOrderActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.textViewPrice, R.id.textViewSure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewPrice:
                break;
            case R.id.textViewSure:
                submitOrder();
                break;
        }
    }

    private void submitOrder() {
        if (calculationMoney == 0){
            RxToast.showToast("请选择要支付的订单");
            return;
        }

        showSelectPayType();

    }

    private void showSelectPayType() {
        customDialog = CustomDialog.show(UnpaidOrderActivity.this, R.layout.dialog_select_pay_type, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
                LinearLayout ll_wechart = rootView.findViewById(R.id.ll_wechart);
                LinearLayout ll_alipay = rootView.findViewById(R.id.ll_alipay);
                RadioButton rb_wechart = rootView.findViewById(R.id.rb_wechart);
                RadioButton rb_alipay = rootView.findViewById(R.id.rb_alipay);
                if (payType == 1){
                    rb_wechart.setChecked(false);
                    rb_alipay.setChecked(true);
                }else {
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
                        getPayInfo();
                    }
                });
            }
        }).setCanCancel(true);
    }

    private void getPayInfo() {
        showWaitDialog(UnpaidOrderActivity.this);
        String orderIds = "";
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isSelect()){
                orderIds = orderIds+mData.get(i).getOrderId() + ",";
            }
        }
        orderIds = orderIds.substring(0,orderIds.length() - 1);
        MQApi.managerPayOrders(userBean.getUId(), payType, String.valueOf(calculationMoney), orderIds, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                if (payType == 1){
                    MyApiResult<String> myApiResult =
                            new Gson().fromJson(s, new TypeToken<MyApiResult<String>>(){}.getType());
//                            JSON.parseObject(s,new MyApiResult<String>().getClass());
                    if (myApiResult.isOk()){
                        startAliPay(myApiResult.getData());
                    }else {
                        RxToast.showToast(myApiResult.getMsg());
                    }
                }else {
                    MyApiResult<WechartPayBean> myApiResult =
                            new Gson().fromJson(s, new TypeToken<MyApiResult<WechartPayBean>>(){}.getType());
//                            JSON.parseObject(s, new TypeReference<MyApiResult<WechartPayBean>>(){});
                    if (myApiResult.isOk()){
                        startWechartPay(myApiResult.getData());
                    }else {
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
                PayTask alipay = new PayTask((Activity) mContext);
                Map<String,String> result = alipay.payV2(data,true);

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
                finish();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                LogUtil.e("支付失败" + payResult);
            }
        };
    };

    private void startWechartPay(WechartPayBean data) {
        msgApi = WXAPIFactory.createWXAPI(this, Constant.WECHART_ID);
        PayReq request = new PayReq();
        request.appId = data.getAppid();
        request.partnerId = data.getPartnerid();
        request.prepayId= data.getPrepayid();
        request.packageValue = "Sign=WXPay";
        request.nonceStr= data.getNoncestr();
        request.timeStamp= data.getTimestamp();
        request.sign= data.getSign();
        msgApi.sendReq(request);

    }
}

package com.andy.meetquickly.activity.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alipay.sdk.app.PayTask;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.CommentAgreementActivity;
import com.andy.meetquickly.activity.user.partner.PartnerActivity;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.PayResult;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.WechartPayBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.wechartSuccessMessage;
import com.andy.meetquickly.utils.LogUtil;
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

import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OpenPartnerActivity extends BaseActivity {

    @BindView(R.id.tv_equity_introduce)
    TextView tvEquityIntroduce;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    UserBean userBean;
    @BindView(R.id.tv_content)
    TextView tvContent;
    CustomDialog customDialog;
    private int payType = 1; //1为微信支付，2为阿里支付
    private static final int SDK_PAY_FLAG = 1;
    //初始化微信支付
    private IWXAPI msgApi;


    @Override
    public int getLayoutId() {
        return R.layout.activity_open_partner;
    }

    @Override
    public void initView() {
        setBarTitle("开通合伙人");
        userBean = UserCache.getInstance().getUser();
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_equity_introduce, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_equity_introduce:
                CommentAgreementActivity.start(mContext, "合伙人权益介绍", Constant.PARTNER_EQUITY_AGREEMENT);
                break;
            case R.id.tv_sure:
                showSelectPayType();
//                PartnerActivity.start(mContext);
                break;
        }
    }

    private void showSelectPayType() {
        customDialog = CustomDialog.show(OpenPartnerActivity.this, R.layout.dialog_select_pay_type, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
                LinearLayout ll_wechart = rootView.findViewById(R.id.ll_wechart);
                LinearLayout ll_alipay = rootView.findViewById(R.id.ll_alipay);
                RadioButton rb_wechart = rootView.findViewById(R.id.rb_wechart);
                RadioButton rb_alipay = rootView.findViewById(R.id.rb_alipay);
                if (payType == 1) {
                    rb_wechart.setChecked(true);
                    rb_alipay.setChecked(false);
                } else {
                    rb_wechart.setChecked(false);
                    rb_alipay.setChecked(true);
                }
                TextView tv_cancel = rootView.findViewById(R.id.tv_cancel);
                TextView tv_sure = rootView.findViewById(R.id.tv_sure);
                ll_wechart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payType = 1;
                        rb_wechart.setChecked(true);
                        rb_alipay.setChecked(false);
                    }
                });
                ll_alipay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payType = 2;
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
                        if (payType == 1) {
                            //请求微信支付
                            getWechartInfo();
                        } else {
                            //请求阿里支付
                            getAliPayInfo();
                        }
                    }
                });
            }
        }).setCanCancel(true);
    }

    private void getWechartInfo() {
        showWaitDialog(OpenPartnerActivity.this);
        MQApi.getWXPayOrderStr(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<WechartPayBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<WechartPayBean>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<WechartPayBean>>(){});
                if (myApiResult.isOk()) {
                    startWechartPay(myApiResult.getData());
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void startWechartPay(WechartPayBean data) {
        msgApi = WXAPIFactory.createWXAPI(this, Constant.WECHART_ID);
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

    private void getAliPayInfo() {
        showWaitDialog(OpenPartnerActivity.this);
        MQApi.getAliPayOrderStr(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<String> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<String>>() {
                        }.getType());
//                        JSON.parseObject(s,new MyApiResult<String>().getClass());
                if (myApiResult.isOk()) {
                    startAliPay(myApiResult.getData());
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void startAliPay(String data) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask((Activity) mContext);
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
                RxToast.showToast("支付成功");
                PartnerActivity.start(mContext);
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                LogUtil.e("支付失败" + payResult);
                RxToast.showToast("支付失败");
            }
        }

        ;
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(wechartSuccessMessage message) {
        PartnerActivity.start(mContext);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, OpenPartnerActivity.class);
        context.startActivity(starter);
    }
}

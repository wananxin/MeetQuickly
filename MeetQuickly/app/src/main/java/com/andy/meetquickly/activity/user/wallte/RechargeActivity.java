package com.andy.meetquickly.activity.user.wallte;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alipay.sdk.app.PayTask;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.user.partner.PartnerActivity;
import com.andy.meetquickly.adapter.RechargeAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.BillBalanceBean;
import com.andy.meetquickly.bean.PayResult;
import com.andy.meetquickly.bean.RechargeBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.WechartPayBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.wechartSuccessMessage;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.andy.meetquickly.utils.LogUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

public class RechargeActivity extends BaseActivity {

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_money_numb)
    TextView tvMoneyNumb;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.rb_wechart)
    RadioButton rbWechart;
    @BindView(R.id.rb_alipay)
    RadioButton rbAlipay;
    @BindView(R.id.tv_sure)
    TextView tvSure;

    RechargeAdapter adapter;
    List<RechargeBean> mData = new ArrayList<>();
    String billNumb = "0";
    String payType = "2";
    UserBean userBean;
    private static final int SDK_PAY_FLAG = 1;
    private IWXAPI msgApi;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initView() {
        setBarTitle("快币");
        setRightTextAndClick("明细", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuicklyMoneyDetailActivity.start(mContext);
            }
        });
        EventBus.getDefault().register(this);
        userBean = UserCache.getInstance().getUser();
        ImageLoaderUtil.getInstance().loadCircleImage(mContext,userBean.getFace(),ivHead);
        tvNickname.setText(userBean.getNickName());
        mData.add(new RechargeBean("10快币", "售价10.00元", "10"));
        mData.add(new RechargeBean("20快币", "售价20.00元", "20"));
        mData.add(new RechargeBean("50快币", "售价50.00元", "50"));
        mData.add(new RechargeBean("100快币", "售价100.00元", "100"));
        mData.add(new RechargeBean("200快币", "售价200.00元", "200"));
        mData.add(new RechargeBean("500快币", "售价500.00元", "500"));
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        adapter = new RechargeAdapter(mData, mContext, 0);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {
                adapter.setSelectPosition(position);
                tvMoneyNumb.setText(mData.get(position).getMoney() + "个");
                billNumb = mData.get(position).getMoney();
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        if (payType.equals("2")) {
            rbWechart.setChecked(true);
            rbAlipay.setChecked(false);
        } else {
            rbWechart.setChecked(false);
            rbAlipay.setChecked(true);
        }
        tvMoneyNumb.setText(mData.get(0).getMoney() + "个");
        billNumb = mData.get(0).getMoney();
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isNotNull(s.toString())) {
                    billNumb = s.toString();
                    tvMoneyNumb.setText(billNumb + "个");
                }else {
                    billNumb = "0";
                    tvMoneyNumb.setText(billNumb + "个");
                }
            }
        });
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(wechartSuccessMessage message) {
        RxToast.showToast("充值成功");
        finish();
    }

    @Override
    public void initData() {
        //快币余额：9500
        showWaitDialog(RechargeActivity.this);
        MQApi.getKuaiBiBalance(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<BillBalanceBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<BillBalanceBean>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<BillBalanceBean>>(){});
                if (myApiResult.isOk()){
                    tvBalance.setText("快币余额："+myApiResult.getData().getTotalBalance());
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, RechargeActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_money_numb, R.id.tv_sure, R.id.linearLayout_wechart, R.id.linearLayout_alipay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_money_numb:

                break;
            case R.id.tv_sure:
                recharge();
                break;
            case R.id.linearLayout_wechart:
                payType = "2";
                rbWechart.setChecked(true);
                rbAlipay.setChecked(false);
                break;
            case R.id.linearLayout_alipay:
                payType = "1";
                rbWechart.setChecked(false);
                rbAlipay.setChecked(true);
                break;
        }
    }

    private void recharge() {
        try{
            if (Integer.parseInt(billNumb) == 0){
                RxToast.showToast("充值快币数要大于0");
                return;
            }
        }catch (Exception e){
            RxToast.showToast("请选择充值快币");
            return;
        }
        showWaitDialog(RechargeActivity.this);
        MQApi.rechargeKuaiBi(userBean.getUId(), payType, billNumb, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                if (payType.equals("1")){
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
                finish();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                LogUtil.e("支付失败" + payResult);
            }
        };
    };
}

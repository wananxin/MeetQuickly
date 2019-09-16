package com.andy.meetquickly.activity.user.wallte;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.CommentAgreementActivity;
import com.andy.meetquickly.activity.user.OpenPartnerActivity;
import com.andy.meetquickly.activity.user.UserAuthenticationActivity;
import com.andy.meetquickly.activity.user.partner.OpenPartnerTwoActivity;
import com.andy.meetquickly.base.BaseInvasionActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.WallteBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WallteActivity extends BaseInvasionActivity {

    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_kuai_money)
    TextView tvKuaiMoney;
    @BindView(R.id.tv_with_profit)
    TextView tvWithProfit;
    @BindView(R.id.tv_other_profit)
    TextView tvOtherProfit;
    UserBean userBean;
    private WallteBean wallteBean;
    private int isFirst = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallte;
    }

    @Override
    public void initView() {
        userBean = UserCache.getInstance().getUser();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst > 0) {
            initData();
        }
        isFirst++;
    }

    @Override
    public void initData() {
        showWaitDialog(WallteActivity.this);
        MQApi.getWalletDate(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<WallteBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<WallteBean>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<WallteBean>>(){});
                if (myApiResult.isOk()) {
                    wallteBean = myApiResult.getData();
                    tvMoney.setText(wallteBean.getWalletBalance());
                    tvKuaiMoney.setText(wallteBean.getKbCount());
                    tvWithProfit.setText(wallteBean.getCommissionCount());
                    tvOtherProfit.setText(wallteBean.getOtherIncomes());
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, WallteActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initView();
    }

    @OnClick({R.id.ll_with_profit, R.id.iv_back, R.id.tv_money, R.id.tv_tixian, R.id.tv_kuai_money, R.id.tv_with_profit, R.id.tv_other_profit, R.id.ll_jsmx, R.id.ll_zd, R.id.ll_smrz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_with_profit:
                CommentAgreementActivity.start(mContext,"合伙人权益介绍",Constant.PARTNER_EQUITY_AGREEMENT);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_money:

                break;
            case R.id.tv_tixian:
                userBean = UserCache.getInstance().getUser();
                if ("2".equals(userBean.getIsCertification())) {
                    CashActivity.start(mContext);
                } else {
                    RxToast.showToast("请先实名认证");
                }
                break;
            case R.id.tv_kuai_money:
                RechargeActivity.start(mContext);
                break;
            case R.id.tv_with_profit:
                if ("2".equals(userBean.getIsOpenPartner())) {
//                    OpenPartnerActivity.start(mContext);
                    WithProfitActivity.start(mContext);
                } else {
                    OpenPartnerTwoActivity.start(mContext);
                }
                break;
            case R.id.tv_other_profit:
                OtherProfitActivity.start(mContext);
                break;
            case R.id.ll_jsmx:
                //结算明细
                SettlementListActivity.start(mContext);
                break;
            case R.id.ll_zd:
                //账单
                BillsActivity.start(mContext);
                break;
            case R.id.ll_smrz:
                userBean = UserCache.getInstance().getUser();
                //实名认证
                if ("1".equals(userBean.getIsCertification())) {
                    RxToast.showToast("正在审核...请稍后");
                } else {
                    UserAuthenticationActivity.start(mContext);
                }
                break;
        }
    }
}

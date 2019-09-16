package com.andy.meetquickly.activity.user.wallte;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.WebHtmlActivity;
import com.andy.meetquickly.activity.home.CommentAgreementActivity;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.DrawalsInfoBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CashActivity extends BaseActivity {

    @BindView(R.id.et_money_numb)
    EditText etMoneyNumb;
    @BindView(R.id.tv_max_numb)
    TextView tvMaxNumb;
    @BindView(R.id.tv_alipay_numb)
    EditText tvAlipayNumb;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.check_box_sure)
    CheckBox checkBoxSure;
    @BindView(R.id.tv_xieyi)
    TextView tvXieyi;

    UserBean userBean;
    private DrawalsInfoBean drawalsInfoBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_cash;
    }

    @Override
    public void initView() {
        setBarTitle("提现");
        setRightTextAndClick("提现规则", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentAgreementActivity.start(mContext,"提现规则",Constant.CASH_RULE);
            }
        });
        userBean = UserCache.getInstance().getUser();
        initData();
    }

    @Override
    public void initData() {
        showWaitDialog(CashActivity.this);
        MQApi.getUserWithdrawalsInfo(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<DrawalsInfoBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<DrawalsInfoBean>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<DrawalsInfoBean>>(){});
                if (myApiResult.isOk()){
                    drawalsInfoBean = myApiResult.getData();
                    tvMaxNumb.setText("当前可提现金额" + drawalsInfoBean.getWalletBalance());
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CashActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_sure, R.id.tv_xieyi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                cashWithdrawal();
                break;
            case R.id.tv_xieyi:
                CommentAgreementActivity.start(mContext,"快逅提现协议",Constant.WITH_DRAW_AGREEMENT);
                break;
        }
    }

    private void cashWithdrawal() {
        if (StringUtil.isNotNull(etMoneyNumb.getText().toString())) {
            if (Integer.parseInt(etMoneyNumb.getText().toString()) <= 0) {
                RxToast.showToast("提现金额必须大于0");
                return;
            }
        }else {
            RxToast.showToast("提现金额不能为空");
            return;
        }

        if (!checkBoxSure.isChecked()){
            RxToast.showToast("请同意快逅兑换协议");
            return;
        }

        if (!StringUtil.isNotNull(tvAlipayNumb.getText().toString())){
            RxToast.showToast("请输入正确的支付宝账号");
            return;
        }
        showWaitDialog(CashActivity.this);
        MQApi.addUserWithdrawalsInfo(userBean.getUId(), etMoneyNumb.getText().toString(),
                tvAlipayNumb.getText().toString(), new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        dissmissWaitDialog();
                        RxToast.showToast(e.toString());
                    }

                    @Override
                    public void onSuccess(String s) {
                        dissmissWaitDialog();
                        MyApiResult<UserBean> myApiResult =
                                new Gson().fromJson(s, new TypeToken<MyApiResult<UserBean>>(){}.getType());
//                                JSON.parseObject(s,new MyApiResult<UserBean>().getClass());
                        if (myApiResult.isOk()){
                            RxToast.showToast("提现成功");
                            finish();
                        }else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }
}

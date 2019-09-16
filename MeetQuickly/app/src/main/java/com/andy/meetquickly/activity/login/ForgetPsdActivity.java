package com.andy.meetquickly.activity.login;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
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

public class ForgetPsdActivity extends BaseActivity {

    @BindView(R.id.et_phone_numb)
    AppCompatEditText etPhoneNumb;
    @BindView(R.id.et_verification_code)
    AppCompatEditText etVerificationCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.et_password)
    AppCompatEditText etPassword;
    @BindView(R.id.et_confirm_password)
    AppCompatEditText etConfirmPassword;
    private TimeCount time;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_psd;
    }

    @Override
    public void initView() {

        time = new TimeCount(60000, 1000);
    }

    @Override
    public void initData() {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ForgetPsdActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_get_code, R.id.tv_sure ,R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                getCode();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_sure:
                updataPsd();
                break;
        }
    }

    private void updataPsd() {
        if (etVerificationCode.getText().toString().trim().isEmpty()){
            RxToast.showToast("请您填写验证码");
            return;
        }

        if (etPassword.getText().toString().trim().isEmpty() && etPassword.getText().toString().trim().length() < 6){
            RxToast.showToast("请您填写密码并大于6位数");
            return;
        }

        if (!etPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())){
            RxToast.showToast("两次密码不一致");
            return;
        }

        showWaitDialog(ForgetPsdActivity.this);
        MQApi.forgetPassword(etPhoneNumb.getText().toString().trim(), etPassword.getText().toString().trim()
                , etVerificationCode.getText().toString().trim(), new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        dissmissWaitDialog();
                        RxToast.showToast(e.toString());
                    }

                    @Override
                    public void onSuccess(String s) {
                        dissmissWaitDialog();
                        MyApiResult myApiResult =
                                new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());
//                                JSON.parseObject(s,new MyApiResult<String>().getClass());
                        if (myApiResult.isOk()){
                            RxToast.showToast("密码修改成功!");
                            finish();
                        }else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }

    private void getCode() {
        if (etPhoneNumb.getText().toString().trim().isEmpty() && etPhoneNumb.getText().toString().trim().length() == 11){
            RxToast.showToast("请您填写正确手机号");
            return;
        }

        showWaitDialog(ForgetPsdActivity.this);
        MQApi.sendCaptchas(etPhoneNumb.getText().toString().trim(), "UPDATEPWD_MSG", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());
//                        JSON.parseObject(s,new MyApiResult<String>().getClass());
                if (myApiResult.isOk()){
                    RxToast.showToast("验证码已发送");
                    time.start();
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvGetCode.setClickable(false);
            tvGetCode.setText("("+millisUntilFinished / 1000 +") 秒");
        }

        @Override
        public void onFinish() {
            tvGetCode.setText("获取验证码");
            tvGetCode.setClickable(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        time.cancel();
    }
}

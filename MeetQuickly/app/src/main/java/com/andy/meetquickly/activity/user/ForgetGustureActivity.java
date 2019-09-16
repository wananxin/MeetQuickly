package com.andy.meetquickly.activity.user;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.login.BindPhoneActivity;
import com.andy.meetquickly.base.BaseActivity;
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

public class ForgetGustureActivity extends BaseActivity {

    @BindView(R.id.et_phone_numb)
    AppCompatEditText etPhoneNumb;
    @BindView(R.id.et_confirm_password)
    AppCompatEditText etConfirmPassword;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_gusture;
    }

    @Override
    public void initView() {
        setBarTitle("忘记手势密码");
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

    @OnClick({R.id.tv_get_code, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                getCode();
                break;
            case R.id.tv_sure:
                break;
        }
    }

    private void getCode() {
        if (!StringUtil.isNotNull(etPhoneNumb.getText().toString())){
            RxToast.showToast("请输入手机号码");
            return;
        }
        showWaitDialog(ForgetGustureActivity.this);
        MQApi.sendCaptchas(etPhoneNumb.getText().toString(), "WX_MSG", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                }.getType());
                if (myApiResult.isOk()){
                    RxToast.showToast("短信发送成功...");
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }
}

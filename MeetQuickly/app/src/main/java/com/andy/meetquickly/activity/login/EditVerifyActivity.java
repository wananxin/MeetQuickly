package com.andy.meetquickly.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkb.vcedittext.VerificationAction;
import com.jkb.vcedittext.VerificationCodeEditText;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditVerifyActivity extends BaseActivity {

    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.et_code)
    VerificationCodeEditText etCode;
    private String phone;
    private String code = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_verify;
    }

    @Override
    public void initView() {
        phone = this.getIntent().getStringExtra("PhoneNumb");
        tvPhone.setText("已发送验证码至：" + phone);
        etCode.setOnVerificationCodeChangedListener(new VerificationAction.OnVerificationCodeChangedListener() {
            @Override
            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {
                code = s.toString();
            }

            @Override
            public void onInputCompleted(CharSequence s) {
            }
        });
    }

    @Override
    public void initData() {

    }

    public static void start(Context context, String PhoneNumb) {
        Intent starter = new Intent(context, EditVerifyActivity.class);
        starter.putExtra("PhoneNumb", PhoneNumb);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_sure:
                verifyCode();
                break;
        }
    }

    private void verifyCode() {
        if (code.length() != 6){
            RxToast.showToast("请输入6位数验证码！");
            return;
        }

        MQApi.valiadCaptchas(phone, code, "",new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());
//                        JSON.parseObject(s, new MyApiResult<String>().getClass());
                if (myApiResult.isOk()){
                    EditInfomationActivity.start(mContext,phone,0);
                    finish();
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }
}

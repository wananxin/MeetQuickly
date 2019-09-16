package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.AuthenticationBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
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

public class UserAuthenticationActivity extends BaseActivity {

    @BindView(R.id.et_name)
    AppCompatEditText etName;
    @BindView(R.id.et_card_numb)
    AppCompatEditText etCardNumb;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_authentication;
    }

    @Override
    public void initView() {
        setBarTitle("实名认证");
        userBean = UserCache.getInstance().getUser();
        if ("2".equals(userBean.getIsCertification())) {
            tvSubmit.setText("已认证");
            tvSubmit.setEnabled(false);
        }
        initData();
    }

    @Override
    public void initData() {
        showWaitDialog(UserAuthenticationActivity.this);
        MQApi.getUserCertificationInfo(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<AuthenticationBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<AuthenticationBean>>() {
                }.getType());
                if (myApiResult.isOk()) {
                    if (null != myApiResult.getData()) {
                        etCardNumb.setText(myApiResult.getData().getIdCardNo());
                        etName.setText(myApiResult.getData().getName());
                    }
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, UserAuthenticationActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {

        if (!StringUtil.isNotNull(etName.getText().toString())) {
            RxToast.showToast("请输入您的姓名！");
            return;
        }

        if (!StringUtil.isNotNull(etCardNumb.getText().toString())) {
            RxToast.showToast("请输入您的身份证号！");
            return;
        }

        showWaitDialog(UserAuthenticationActivity.this);
        MQApi.addUserCertification(userBean.getUId(), etName.getText().toString().trim(), etCardNumb.getText().toString().trim(),
                new SimpleCallBack<String>() {
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
                        if (myApiResult.isOk()) {
                            RxToast.showToast("已提交认证...请等待审核");
                            userBean.setIsCertification("1");
                            UserCache.getInstance().update(userBean);
                            finish();
                        } else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }
}

package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
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

public class UpdateUserPsdActivity extends BaseActivity {


    @BindView(R.id.et_oldPassword)
    AppCompatEditText etOldPassword;
    @BindView(R.id.et_newPassword)
    AppCompatEditText etNewPassword;
    @BindView(R.id.et_againPassword)
    AppCompatEditText etAgainPassword;

    private UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_user_psd;
    }

    @Override
    public void initView() {
        setBarTitle("修改密码");
        userBean = UserCache.getInstance().getUser();
    }

    @Override
    public void initData() {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, UpdateUserPsdActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_sure)
    public void onViewClicked() {
        submitPassword();
    }

    private void submitPassword() {
        if (etOldPassword.getText().toString().length() < 6){
            RxToast.showToast("请您输入旧密码");
            return;
        }
        if (etNewPassword.getText().toString().length() < 6){
            RxToast.showToast("请您输入新密码");
            return;
        }
        if (!etNewPassword.getText().toString().equals(etAgainPassword.getText().toString())){
            RxToast.showToast("两次密码不一致");
            return;
        }

        showWaitDialog(UpdateUserPsdActivity.this);
        MQApi.updatePassword(userBean.getUId(), etOldPassword.getText().toString(), etNewPassword.getText().toString(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());
                if (myApiResult.isOk()){
                    RxToast.showToast("密码更新成功");
                    finish();
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });

    }
}

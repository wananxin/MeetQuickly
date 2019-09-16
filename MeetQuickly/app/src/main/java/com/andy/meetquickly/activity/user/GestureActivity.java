package com.andy.meetquickly.activity.user;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.wangnan.library.GestureLockView;
import com.wangnan.library.listener.OnGestureLockListener;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GestureActivity extends BaseActivity {

    @BindView(R.id.glv)
    GestureLockView glv;
    @BindView(R.id.textViewInfo)
    TextView textViewInfo;
    @BindView(R.id.textViewForget)
    TextView textViewForget;

    private int type;  //0为验证旧密码   1为输入新密码 2为验证密码并提交
    private String title;
    private String password;
    private String info;
    private UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_gesture;
    }

    @Override
    public void initView() {
        userBean = UserCache.getInstance().getUser();
        type = this.getIntent().getIntExtra("type", 0);
        title = this.getIntent().getStringExtra("title");
        password = this.getIntent().getStringExtra("password");
        info = this.getIntent().getStringExtra("info");
        if (type == 0){
            textViewForget.setVisibility(View.VISIBLE);
            textViewForget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        setBarTitle(title);
        textViewInfo.setText(info);
        glv.setUseAnim(true);
        glv.setUseVibrate(true);
        glv.showErrorStatus(600);
        glv.setGestureLockListener(new OnGestureLockListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(String progress) {

            }

            @Override
            public void onComplete(String result) {
                if (!StringUtil.isNotNull(result)) {
                    return;
                }
                if (type == 0) {
                    checkGusture(result);
                } else if (type == 1) {
                    againGusture(result);
                } else if (type == 2) {
                    submitGusture(result);
                }
            }
        });
    }

    private void submitGusture(String result) {
        if (!password.equals(result)) {
            textViewInfo.setText("两次设置不一样，请重新绘制");
            glv.clearView();
            return;
        }

        showWaitDialog(GestureActivity.this);
        MQApi.setGesturePassword(userBean.getUId(), result, new SimpleCallBack<String>() {
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
                    RxToast.showToast("手势密码设置成功");
                    userBean.setGesturesPassword(result);
                    userBean.setGesturesStatus("1");
                    UserCache.getInstance().update(userBean);
                    finish();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void againGusture(String result) {
        GestureActivity.start(mContext, 2, "设置手势密码", "再次绘制解锁图案", result);
        finish();
    }

    private void checkGusture(String result) {
        showWaitDialog(GestureActivity.this);
        MQApi.checkGesturePassword(userBean.getUId(), result, new SimpleCallBack<String>() {
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
                    GestureActivity.start(mContext, 1, "设置手势密码", "绘制解锁图案", "");
                    finish();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
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

    public static void start(Context context, int type, String title, String info, String password) {
        Intent starter = new Intent(context, GestureActivity.class);
        starter.putExtra("type", type);
        starter.putExtra("title", title);
        starter.putExtra("info", info);
        starter.putExtra("password", password);
        context.startActivity(starter);
    }
}

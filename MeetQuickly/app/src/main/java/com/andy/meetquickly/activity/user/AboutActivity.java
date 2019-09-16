package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.SysPhoneBean;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.fragment.HomeFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.DaoHangUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.SelectDialog;
import com.vondear.rxtool.RxAppTool;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.textViewEdition)
    TextView textViewEdition;
    @BindView(R.id.textViewPhone)
    TextView textViewPhone;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        setBarTitle("关于我们");
        textViewEdition.setText("当前版本："+ RxAppTool.getAppVersionName(mContext));
        initData();
    }

    @Override
    public void initData() {
        showWaitDialog(AboutActivity.this);
        MQApi.getSysConfigInfo(new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<SysPhoneBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<SysPhoneBean>>() {
                }.getType());
                if (myApiResult.isOk()) {
                    textViewPhone.setText(myApiResult.getData().getCONSUMER_HOTLINE());
                    textViewPhone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPhoneDialog(myApiResult.getData().getCONSUMER_HOTLINE());
                        }
                    });
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void showPhoneDialog(String phone) {
        SelectDialog.show(mContext, "提示", "是否拨打客服电话："+phone, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RxDeviceTool.dial(AboutActivity.this, phone);
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCanCancel(true);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AboutActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

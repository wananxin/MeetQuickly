package com.andy.meetquickly.activity.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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

public class OrderComplaintActivity extends BaseActivity {

    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.radioButton2)
    RadioButton radioButton2;
    @BindView(R.id.radioButton3)
    RadioButton radioButton3;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_et_numb)
    TextView tvEtNumb;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    String storeId;
    String orderId;
    UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_complaint;
    }

    @Override
    public void initView() {
        setBarTitle("投诉");
        userBean = UserCache.getInstance().getUser();
        storeId = this.getIntent().getStringExtra("storeId");
        orderId = this.getIntent().getStringExtra("orderId");
    }

    @Override
    public void initData() {

    }

    public static void start(Context context, String storeId, String orderId) {
        Intent starter = new Intent(context, OrderComplaintActivity.class);
        starter.putExtra("storeId", storeId);
        starter.putExtra("orderId", orderId);
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
        if (!(radioButton1.isChecked() || radioButton2.isChecked() || radioButton3.isChecked())) {
            RxToast.showToast("请选择投诉内容");
            return;
        }

        String content;

        if (radioButton1.isChecked()) {
            content = radioButton1.getText().toString();
        } else if (radioButton2.isChecked()) {
            content = radioButton2.getText().toString();
        } else {
            content = radioButton3.getText().toString();
        }

        showWaitDialog((Activity) mContext);
        MQApi.userComplainOrder(userBean.getUId(), storeId, orderId, content, etContent.getText().toString(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                        }.getType());
//                        JSON.parseObject(s,new MyApiResult<String>().getClass());
                if (myApiResult.isOk()) {
                    RxToast.showToast("投诉成功");
                    finish();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }
}

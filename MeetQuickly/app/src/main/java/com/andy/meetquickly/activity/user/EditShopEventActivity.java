package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserShopDetailBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditShopEventActivity extends BaseActivity {

    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    private UserBean userBean;
    private SimpleDateFormat dateFormat;
    private int type;// 0为增加，1为修改
    private String storeId;
    private UserShopDetailBean.StoreDiscountsTypeBean bean;


    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_shop_event;
    }

    @Override
    public void initView() {
        userBean = UserCache.getInstance().getUser();
        setBarTitle("现有活动");
        type = this.getIntent().getIntExtra("type", 0);
        storeId = this.getIntent().getStringExtra("storeId");
        bean = (UserShopDetailBean.StoreDiscountsTypeBean) this.getIntent().getSerializableExtra("bean");
        if (type == 1) {
            tvStartTime.setText(bean.getStartTime());
            tvEndTime.setText(bean.getEndTime());
            etContent.setText(bean.getName());
            setRightTextAndClick("删除", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteActivity();
                }
            });
        }
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public void initData() {

    }

    public static void start(Context context, int type, String storeId, UserShopDetailBean.StoreDiscountsTypeBean bean) {
        Intent starter = new Intent(context, EditShopEventActivity.class);
        starter.putExtra("type", type);
        starter.putExtra("storeId", storeId);
        starter.putExtra("bean", bean);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_start_time, R.id.tv_end_time, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                //时间选择器
                TimePickerView pvStartTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvStartTime.setText(dateFormat.format(date));
                    }
                }).setSubmitColor(Color.BLACK)//确定按钮文字颜色
                        .setCancelColor(Color.BLACK)//取消按钮文字颜色
                        .build();

                pvStartTime.show();
                break;
            case R.id.tv_end_time:
                //时间选择器
                TimePickerView pvEndTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvEndTime.setText(dateFormat.format(date));
                    }
                }).setSubmitColor(Color.BLACK)//确定按钮文字颜色
                        .setCancelColor(Color.BLACK)//取消按钮文字颜色
                        .build();

                pvEndTime.show();
                break;
            case R.id.tv_sure:
                submit();
                break;
        }
    }

    private void deleteActivity() {
        showWaitDialog(EditShopEventActivity.this);
        MQApi.delShopActivityInfo(bean.getId(), new SimpleCallBack<String>() {
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
//                                JSON.parseObject(s, new MyApiResult<String>().getClass());
                        if (myApiResult.isOk()) {
                            RxToast.showToast("删除成功");
                            finish();
                        } else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }

    private void submit() {
        showWaitDialog(EditShopEventActivity.this);
        if (type == 0) {
            MQApi.addShopActivityInfo(storeId, etContent.getText().toString(), tvStartTime.getText().toString(),
                    tvEndTime.getText().toString(), new SimpleCallBack<String>() {
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
//                                    JSON.parseObject(s, new MyApiResult<String>().getClass());
                            if (myApiResult.isOk()) {
                                RxToast.showToast("添加活动成功");
                                finish();
                            } else {
                                RxToast.showToast(myApiResult.getMsg());
                            }
                        }
                    });
        } else {
            MQApi.updateShopActivityInfo(bean.getId(), etContent.getText().toString(), tvStartTime.getText().toString(),
                    tvEndTime.getText().toString(), new SimpleCallBack<String>() {
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
//                                    JSON.parseObject(s, new MyApiResult<String>().getClass());
                            if (myApiResult.isOk()) {
                                RxToast.showToast("活动修改成功");
                                finish();
                            } else {
                                RxToast.showToast(myApiResult.getMsg());
                            }
                        }
                    });
        }
    }
}

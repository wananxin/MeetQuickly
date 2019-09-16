package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.ConsumptionBillBean;
import com.andy.meetquickly.bean.OrderDetailBean;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderCheckActivity extends BaseActivity {

    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.constraintLayoutPic)
    ConstraintLayout constraintLayoutPic;
    @BindView(R.id.textViewNull)
    TextView textViewNull;

    private OrderDetailBean bean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_check;
    }

    @Override
    public void initView() {
        setBarTitle("核对账单");
        bean = (OrderDetailBean) this.getIntent().getSerializableExtra("bean");
        setRightTextAndClick("有异议", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderObjectionActivity.start(mContext, bean);
            }
        });

        if ("1".equals(bean.getManagerVerify())) {
            constraintLayoutPic.setVisibility(View.VISIBLE);
            textViewNull.setVisibility(View.GONE);
            initData();
        } else {
            constraintLayoutPic.setVisibility(View.GONE);
            textViewNull.setVisibility(View.VISIBLE);
        }
    }

    private ConsumptionBillBean consumptionBillBean;

    @Override
    public void initData() {
        showWaitDialog(OrderCheckActivity.this);
        MQApi.getConsumptionBills(bean.getOrderId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<ConsumptionBillBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<ConsumptionBillBean>>() {
                }.getType());
                if (myApiResult.isOk()) {
                    consumptionBillBean = myApiResult.getData();
                    if (StringUtil.isNotNull(myApiResult.getData().getManagerProof1())) {
                        constraintLayoutPic.setVisibility(View.VISIBLE);
                        textViewNull.setVisibility(View.GONE);
                        ImageLoaderUtil.getInstance().loadImage(mContext, myApiResult.getData().getManagerProof1(), iv1);
                        ImageLoaderUtil.getInstance().loadImage(mContext, myApiResult.getData().getManagerProof2(), iv2);
                    } else {
                        constraintLayoutPic.setVisibility(View.GONE);
                        textViewNull.setVisibility(View.VISIBLE);
                    }
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv1, R.id.iv2, R.id.textViewOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv1:
                break;
            case R.id.iv2:
                break;
            case R.id.textViewOk:
                if ("1".equals(bean.getManagerVerify())) {
                    confirmOrder();
                } else {
                    RxToast.showToast("暂未上传凭证");
                }
                break;
        }
    }

    private void confirmOrder() {
        showWaitDialog(OrderCheckActivity.this);
        MQApi.userConfirmOrder(bean.getId(), new SimpleCallBack<String>() {
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
                    finish();
                    RxToast.showToast("订单已确认");
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    public static void start(Context context, OrderDetailBean bean) {
        Intent starter = new Intent(context, OrderCheckActivity.class);
        starter.putExtra("bean", bean);
        context.startActivity(starter);
    }
}

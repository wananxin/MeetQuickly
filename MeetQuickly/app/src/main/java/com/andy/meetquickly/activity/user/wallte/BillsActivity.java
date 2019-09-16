package com.andy.meetquickly.activity.user.wallte;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.BillsBean;
import com.andy.meetquickly.bean.UserBean;
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
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillsActivity extends BaseActivity {

    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_with_profit)
    TextView tvWithProfit;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_gift)
    TextView tvGift;
    @BindView(R.id.tv_all)
    TextView tvAll;
    private UserBean userBean;
    private String currentTime;
    private BillsBean billsBean;
    private SimpleDateFormat showData;
    private SimpleDateFormat reqData;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bills;
    }

    @Override
    public void initView() {
        setBarTitle("账单");
        userBean = UserCache.getInstance().getUser();
        // HH:mm:ss
        showData = new SimpleDateFormat("yyyy年MM月");
        // HH:mm:ss
        reqData = new SimpleDateFormat("yyyy-MM");
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        currentTime = reqData.format(date);
        tvDate.setText(showData.format(date));
        initData();
    }

    /*共计：￥*/
    @Override
    public void initData() {
        showWaitDialog(BillsActivity.this);
        MQApi.getUserBillCount(userBean.getUId(), currentTime, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<BillsBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<BillsBean>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<BillsBean>>(){});
                if (myApiResult.isOk()) {
                    billsBean = myApiResult.getData();
                    tvWithProfit.setText(billsBean.getCommissionCount());
                    tvCoupon.setText(billsBean.getCouponCount());
                    tvGift.setText(billsBean.getGiftCount());
                    tvAll.setText("共计：￥"+billsBean.getTotal());
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, BillsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_date, R.id.tv_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_date:
                showTimeDialog();
                break;
            case R.id.tv_all:
                break;
        }
    }

    private void showTimeDialog() {
        TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvDate.setText(showData.format(date));
                currentTime = reqData.format(date);
                initData();
            }
        }).setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .build();
        pvTime.show();
    }
}

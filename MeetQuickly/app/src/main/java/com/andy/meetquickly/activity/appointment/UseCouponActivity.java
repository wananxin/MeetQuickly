package com.andy.meetquickly.activity.appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.UseCouponAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.CouponBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.LogUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/22  19:15
 * 描述：     选择使用代金券
 */
public class UseCouponActivity extends BaseActivity {

    @BindView(R.id.recycler_coupon)
    RecyclerView recyclerCoupon;
    @BindView(R.id.tv_momey)
    TextView tvMomey;
    @BindView(R.id.textViewRange)
    TextView textViewRange;
    private UseCouponAdapter adapter;
    private int orderType;//1.网店填写资料预定 2.直接电话 3.直接填写资料 4.网店活动
    private String kind;
    private String mkind = "";
    private String storeId;
    private String mStoreId = "";
    private String companyName = "";
    private String storeName = "";
    List<CouponBean> datas = new ArrayList<>();
    private UserBean userBean;
    private String userCouponId = "";
    private View notDataView;
    private View errorView;
    private double selectMoney = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_use_coupon;
    }

    @Override
    public void initView() {
        setBarTitle("使用代金券");
        userBean = UserCache.getInstance().getUser();
        orderType = this.getIntent().getIntExtra("orderType", 3);
        kind = this.getIntent().getStringExtra("kind");
        storeId = this.getIntent().getStringExtra("storeId");
        companyName = this.getIntent().getStringExtra("companyName");
        storeName = this.getIntent().getStringExtra("storeName");
        recyclerCoupon.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new UseCouponAdapter(datas);
        //adapter设置点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapt, View view, int position) {
                adapter.selectCoupon(datas.get(position));
                adapter.notifyDataSetChanged();
                calculationMoney();
            }

        });
        recyclerCoupon.setAdapter(adapter);
        notDataView = getLayoutInflater().inflate(R.layout.layout_empty_view, (ViewGroup) recyclerCoupon.getParent(), false);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        errorView = getLayoutInflater().inflate(R.layout.layout_error_view, (ViewGroup) recyclerCoupon.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        initData();
    }

    private void calculationMoney() {
        List<CouponBean> selectCoupon = adapter.getSelect();
        selectMoney = 0;
        userCouponId = "";
        for (int i = 0; i < selectCoupon.size(); i++) {
            selectMoney = selectMoney + Double.parseDouble(selectCoupon.get(i).getCouponAmount());
            userCouponId = userCouponId + selectCoupon.get(i).getId() + ",";
        }
        mkind = "";
        mStoreId = "";
        if (selectCoupon.size() == 0){
            textViewRange.setVisibility(View.GONE);
        }else {
            textViewRange.setVisibility(View.VISIBLE);
            for (int i = 0; i < selectCoupon.size(); i++) {
                if (selectCoupon.get(i).getUseScope().equals("1")){
                    textViewRange.setText("(仅限商务KTV使用)");
                    mkind = "1";
                    break;
                }else if (selectCoupon.get(i).getUseScope().equals("2")){
                    textViewRange.setText("(仅限酒吧使用)");
                    mkind = "2";
                    break;
                }else if (selectCoupon.get(i).getUseScope().equals("3")){
                    textViewRange.setText("(仅限量贩KTV使用)");
                    mkind = "3";
                    break;
                }else if (selectCoupon.get(i).getUseScope().equals("4")){
                    textViewRange.setText("(仅限清吧使用)");
                    mkind = "4";
                    break;
                }else if (selectCoupon.get(i).getUseScope().equals("5")){
                    textViewRange.setText("(全平台通用)");
                }else{
                    textViewRange.setText("(仅限"+selectCoupon.get(i).getStoreName()+"经理使用)");
                    mkind = selectCoupon.get(i).getKind();
                    mStoreId = selectCoupon.get(i).getStoreId();
                    storeName = selectCoupon.get(i).getStoreName();
                    companyName = selectCoupon.get(i).getCompanyName();
                    break;
                }
            }
        }
        if (userCouponId.length() > 0) {
            userCouponId = userCouponId.substring(0, userCouponId.length() - 1);
        }
        tvMomey.setText(String.valueOf(selectMoney) + "代金券");

    }

    @Override
    public void initData() {
        MQApi.getUserCashCoupon(userBean.getUId(), kind, storeId, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
                adapter.setEmptyView(errorView);
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<CouponBean>> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<List<CouponBean>>>() {
                }.getType());
                if (myApiResult.isOk()) {
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        datas.add(myApiResult.getData().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    if (datas.size() == 0) {
                        if (StringUtil.isNotNull(storeId)) {
                            YuYueMeetPeopleActivity.start(mContext, orderType, storeId, String.valueOf(selectMoney), userCouponId, kind,companyName,storeName);
                        } else {
                            AppointmentInfoActivity.start(mContext, orderType, storeId, String.valueOf(selectMoney), userCouponId, "", "", kind,companyName,storeName);
                        }
                        finish();
                    }
                } else {
                    adapter.setEmptyView(errorView);
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

    public static void start(Context context, int orderType, String kind, String storeId,String companyName,String storeName) {
        Intent starter = new Intent(context, UseCouponActivity.class);
        starter.putExtra("kind", kind);
        starter.putExtra("storeId", storeId);
        starter.putExtra("orderType", orderType);
        starter.putExtra("companyName", companyName);
        starter.putExtra("storeName", storeName);
        context.startActivity(starter);
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        doNext();
    }

    private void doNext() {
        if (!StringUtil.isNotNull(kind)){
            kind = mkind;
        }
        if (!StringUtil.isNotNull(storeId)){
            storeId = mStoreId;
        }
        if (StringUtil.isNotNull(storeId)) {
            YuYueMeetPeopleActivity.start(mContext, orderType, storeId, String.valueOf(selectMoney), userCouponId, kind,companyName,storeName);
        } else {
            AppointmentInfoActivity.start(mContext, orderType, storeId, String.valueOf(selectMoney), userCouponId, "", "", kind,companyName,storeName);
        }
        finish();
    }
}

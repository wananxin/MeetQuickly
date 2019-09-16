package com.andy.meetquickly.activity.user.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.user.order.OrdeEventDetailActivity;
import com.andy.meetquickly.activity.user.wallte.OtherProfitActivity;
import com.andy.meetquickly.adapter.OrderMeetAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.OrderDetailBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageOrderDetailActivity extends BaseActivity {

    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_event)
    TextView tvEvent;
    @BindView(R.id.ll_all)
    LinearLayout llAll;
    @BindView(R.id.tv_fanli)
    TextView tvFanli;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_seat_numb)
    TextView tvSeatNumb;
    @BindView(R.id.tv_people_numb)
    TextView tvPeopleNumb;
    @BindView(R.id.tv_reach_time)
    TextView tvReachTime;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_daijia)
    TextView tvDaijia;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_order_numb)
    TextView tvOrderNumb;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_content)
    TextView tvContent;

    private OrderDetailBean bean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_manage_order_detail;
    }

    @Override
    public void initView() {
        setBarTitle("订单详情");
        bean = (OrderDetailBean) this.getIntent().getSerializableExtra("bean");
        if ("2".equals(bean.getOrderStatus())) {
            llAll.setVisibility(View.VISIBLE);
            tvFanli.setVisibility(View.GONE);
            tvState.setText("订单正在进行");
        } else {
            tvState.setText("订单已完成");
            llAll.setVisibility(View.GONE);
            tvFanli.setVisibility(View.VISIBLE);
            tvFanli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OtherProfitActivity.start(mContext);
                }
            });
        }

        if ("1".equals(bean.getIsCancel())){
            llAll.setVisibility(View.GONE);
            tvFanli.setVisibility(View.GONE);
            tvState.setText("订单已取消");
        }
        ImageLoaderUtil.getInstance().loadCircleImage(ManageOrderDetailActivity.this, bean.getFace(), ivHead);
        tvShopName.setText(bean.getName());
        if ("1".equals(bean.getSex())) {
            tvName.setText("(先生)");
        } else {
            tvName.setText("(女士)");
        }
        try {
            tvAddress.setText("到店时间：" + bean.getTargetDate() + " " + bean.getReachTime());
        } catch (Exception e) {
            tvAddress.setText("");
        }
        tvSeatNumb.setText(bean.getTables());
        tvPeopleNumb.setText(bean.getPeoples() + "人");
        tvReachTime.setText(bean.getTargetDate() + " " + bean.getReachTime());
        tvOrderNumb.setText(bean.getOrderId());
        if (StringUtil.isNotNull(bean.getRemark())) {
            tvContent.setText(bean.getRemark());
        } else {
            tvContent.setText("未填写需求");
        }
        if (StringUtil.isNotNull(bean.getCouponMoney())) {
            tvDaijia.setText("￥" + bean.getCouponMoney());
            tvOrderTime.setText("￥" + bean.getCouponMoney());
            tvFanli.setText("已返代金券" + bean.getCouponMoney());
        } else {
            tvDaijia.setText("0");
            tvOrderTime.setText("0");
            tvFanli.setText("已返代金券0");
        }

        if ("1".equals(bean.getUseCoupon())) {
            tvCoupon.setText("￥" + bean.getCoupon());
        } else {
            tvCoupon.setText("未使用");
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        OrderMeetAdapter adapter = new OrderMeetAdapter(bean.getUserInfo(), mContext);
        recyclerView.setAdapter(adapter);
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

    public static void start(Context context, OrderDetailBean bean) {
        Intent starter = new Intent(context, ManageOrderDetailActivity.class);
        starter.putExtra("bean", bean);
        context.startActivity(starter);
    }

    @OnClick(R.id.tv_event)
    public void onViewClicked() {
        if (null == bean.getStoreActivity()){
            RxToast.showToast("此订单没有活动");
        } else {
            OrdeEventDetailActivity.start(mContext,bean.getStoreActivity());
        }
    }
}

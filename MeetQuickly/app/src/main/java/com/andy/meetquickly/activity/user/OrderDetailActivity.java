package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.DialogInterface;
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
import com.andy.meetquickly.adapter.OrderMeetAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.OrderDetailBean;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.reachStoreMessage;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.SelectDialog;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity {


    @BindView(R.id.map_view)
    MapView map;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_call_phone)
    ImageView ivCallPhone;
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
    @BindView(R.id.ll_all)
    LinearLayout llAll;
    @BindView(R.id.tv_fanli)
    TextView tvFanli;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.textViewReceive)
    TextView textViewReceive;

    private OrderDetailBean bean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {
        setBarTitle("订单详情");
        bean = (OrderDetailBean) this.getIntent().getSerializableExtra("bean");
        if ("2".equals(bean.getOrderStatus())) {
            map.setVisibility(View.VISIBLE);
            llAll.setVisibility(View.VISIBLE);
            tvFanli.setVisibility(View.GONE);
            ivCallPhone.setVisibility(View.VISIBLE);
            tvState.setText("订单正在进行");
        } else {
            tvState.setText("订单已完成");
            map.setVisibility(View.GONE);
            llAll.setVisibility(View.GONE);
            tvFanli.setText("已返代金券" + bean.getFinalCouponReturn());
            tvFanli.setVisibility(View.VISIBLE);
            ivCallPhone.setVisibility(View.GONE);
        }

        isReachStore();

        ImageLoaderUtil.getInstance().loadCircleImage(OrderDetailActivity.this, bean.getCoverImg(), ivHead);
        tvShopName.setText(bean.getCompanyName());
        tvName.setText("(" + bean.getStoreName() + "经理)");
        tvAddress.setText(bean.getAddress());
        tvSeatNumb.setText(bean.getTables());
        tvPeopleNumb.setText(bean.getPeoples() + "人");
        tvReachTime.setText(bean.getTargetDate() + " " + bean.getReachTime());
        tvOrderTime.setText(bean.getCreateTime());
        tvOrderNumb.setText(bean.getOrderId());
        if (StringUtil.isNotNull(bean.getRemark())) {
            tvContent.setText(bean.getRemark());
        } else {
            tvContent.setText("未填写需求");
        }
        if (StringUtil.isNotNull(bean.getIsFreeDriving())) {
            tvDaijia.setText(bean.getIsFreeDriving() + "km免费");
        } else {
            tvDaijia.setText("无免费代驾");
        }

        if ("1".equals(bean.getUseCoupon())) {
            tvCoupon.setText("￥" + bean.getCoupon());
        } else {
            tvCoupon.setText("未使用");
        }

        LatLng cenpt = new LatLng(Double.parseDouble(bean.getLatitude()), Double.parseDouble(bean.getLongitude()));
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //在地图上添加Marker，并显示
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        BaiduMap mBaidumap = map.getMap();
        mBaidumap.setMapStatus(mMapStatusUpdate);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        OrderMeetAdapter adapter = new OrderMeetAdapter(bean.getUserInfo(), mContext);
        recyclerView.setAdapter(adapter);
    }

    private void isReachStore() {
        if ("2".equals(bean.getReachStore())) {
            textViewReceive.setVisibility(View.GONE);
        } else {
            textViewReceive.setVisibility(View.VISIBLE);
        }
    }

    private void showPhoneDialog() {
        String msg = bean.getStoreName() + "经理:" + bean.getStoreMobile();
        SelectDialog.show(mContext, "拨打接待经理电话", msg, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RxDeviceTool.dial(mContext, bean.getStoreMobile());
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCanCancel(true);
    }

    @Override
    public void initData() {

    }

    public static void start(Context context, OrderDetailBean bean) {
        Intent starter = new Intent(context, OrderDetailActivity.class);
        starter.putExtra("bean", bean);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_tousu, R.id.tv_hedui, R.id.tv_event, R.id.iv_call_phone, R.id.textViewReceive, R.id.tv_fanli})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_tousu:
                OrderComplaintActivity.start(mContext, bean.getStoreId(), bean.getOrderId());
                break;
            case R.id.tv_hedui:
                if ("1".equals(bean.getManagerVerify())) {
                    OrderCheckActivity.start(mContext, bean);
                }else {
                    RxToast.showToast("经理人暂未上传凭证");
                }
                break;
            case R.id.tv_event:
                if (null == bean.getStoreActivity()) {
                    RxToast.showToast("此订单没有活动");
                } else {
                    OrdeEventDetailActivity.start(mContext, bean.getStoreActivity());
                }
                break;
            case R.id.iv_call_phone:
                showPhoneDialog();
                break;
            case R.id.textViewReceive:
                if ("1".equals(bean.getReachStore())) {
                    showOkDialog();
                }
            case R.id.tv_fanli:
                CouponActivity.start(OrderDetailActivity.this);
                break;
        }
    }

    private void showOkDialog() {
        SelectDialog.show(OrderDetailActivity.this, "温馨提示", "确认到店后，您使用的代金券将无法退回 结账时请与接待经理联系抵扣您使用的代金券", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reachStore();
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCanCancel(true);
    }

    private void reachStore() {
        showWaitDialog(OrderDetailActivity.this);
        MQApi.userReachStore(bean.getId(), new SimpleCallBack<String>() {
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
                    bean.setReachStore("2");
                    EventBus.getDefault().post(new reachStoreMessage(bean.getOrderId()));
                    isReachStore();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (map != null) {
            map.onDestroy();
        }
    }
}

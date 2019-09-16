package com.andy.meetquickly.activity.appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.YuYueMeetAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
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

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/22  19:14
 * 描述：     预约邂逅的人
 */
public class YuYueMeetPeopleActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private int orderType;
    private String storeId;
    private String coupon;
    private String userCouponId;
    private String kind;
    private String companyName = "";
    private String storeName = "";
    private String missLoves = "";
    private YuYueMeetAdapter adapter;
    private List<UserBean> datas = new ArrayList<>();
    private int mPage = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_yu_yue_meet_people;
    }

    @Override
    public void initView() {
        setBarTitle("可能邂逅的人");
        orderType = this.getIntent().getIntExtra("orderType",3);
        storeId = this.getIntent().getStringExtra("storeId");
        coupon = this.getIntent().getStringExtra("coupon");
        userCouponId = this.getIntent().getStringExtra("userCouponId");
        kind = this.getIntent().getStringExtra("kind");
        companyName = this.getIntent().getStringExtra("companyName");
        storeName = this.getIntent().getStringExtra("storeName");
        setRightTextAndClick("继续", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theNext();
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        adapter = new YuYueMeetAdapter(datas,YuYueMeetPeopleActivity.this);
        //adapter设置点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapt, View view, int position) {
                if (adapter.selectCoupon(datas.get(position))) {
                    RxToast.showToast("最多选择5个");
                } else {
                    adapter.notifyItemChanged(position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        initData();
    }

    private void theNext() {
        missLoves = "";
        List<UserBean>  selectUser = adapter.getSelectUser();
        if (selectUser.size() > 0) {
            for (int i = 0; i < selectUser.size(); i++) {
                missLoves = missLoves + selectUser.get(i).getUId() + ",";
            }
            missLoves = missLoves.substring(0,missLoves.length() - 1);
        }
        AppointmentInfoActivity.start(mContext,orderType,storeId,coupon
        ,userCouponId,missLoves,"",kind,companyName,storeName);
        finish();
    }

    @Override
    public void initData() {
        showWaitDialog(YuYueMeetPeopleActivity.this);
        MQApi.getShopEncounterListByStoreId(storeId, mPage, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<UserBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<UserBean>>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<UserBean>>>(){});
                if (myApiResult.isOk()) {
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        datas.add(myApiResult.getData().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    if (datas.size() == 0){
                        theNext();
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

    public static void start(Context context, int orderType, String storeId, String coupon, String userCouponId
            , String kind,String companyName,String storeName) {
        Intent starter = new Intent(context, YuYueMeetPeopleActivity.class);
        starter.putExtra("orderType", orderType);
        starter.putExtra("storeId", storeId);
        starter.putExtra("coupon", coupon);
        starter.putExtra("userCouponId", userCouponId);
        starter.putExtra("kind", kind);
        starter.putExtra("companyName", companyName);
        starter.putExtra("storeName", storeName);
        context.startActivity(starter);
    }
}

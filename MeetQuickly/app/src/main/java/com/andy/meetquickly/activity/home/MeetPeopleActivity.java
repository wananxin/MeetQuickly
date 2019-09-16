package com.andy.meetquickly.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.ShopMeetAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.fragment.shopinfo.ShopMeetFragment;
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

public class MeetPeopleActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private String storeId;
    private int mPage = 1;
    private List<UserBean> mData = new ArrayList<>();
    private ShopMeetAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_meet_people;
    }

    @Override
    public void initView() {
        setBarTitle("可能邂逅的人");
        storeId = this.getIntent().getStringExtra("storeId");
        recyclerView.setLayoutManager(new GridLayoutManager(mContext , 2));
        adapter = new ShopMeetAdapter(mData,mContext);
        //adapter设置点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserDataActivity.start(mContext,mData.get(position).getUId());
            }
        });
        recyclerView.setAdapter(adapter);
        initData();
    }

    public static void start(Context context, String storeId) {
        Intent starter = new Intent(context, MeetPeopleActivity.class);
        starter.putExtra("storeId", storeId);
        context.startActivity(starter);
    }

    @Override
    public void initData() {
        showWaitDialog(MeetPeopleActivity.this);
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
                        mData.add(myApiResult.getData().get(i));
                    }
                    adapter.notifyDataSetChanged();
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
}

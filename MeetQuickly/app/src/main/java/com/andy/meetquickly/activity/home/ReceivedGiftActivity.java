package com.andy.meetquickly.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.ReceivedGiftAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserDataInfoBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceivedGiftActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private List<UserDataInfoBean.GiftsEntityBean> datas = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_received_gift;
    }

    @Override
    public void initView() {
        setBarTitle("收到的礼物");
        datas = (List<UserDataInfoBean.GiftsEntityBean>) this.getIntent().getSerializableExtra("datas");
        recyclerView.setLayoutManager(new GridLayoutManager(mContext , 5));
        ReceivedGiftAdapter adapter = new ReceivedGiftAdapter(datas,mContext);
        //adapter设置点击事件
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }

    public static void start(Context context,List<UserDataInfoBean.GiftsEntityBean> datas) {
        Intent starter = new Intent(context, ReceivedGiftActivity.class);
        starter.putExtra("datas", (Serializable) datas);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

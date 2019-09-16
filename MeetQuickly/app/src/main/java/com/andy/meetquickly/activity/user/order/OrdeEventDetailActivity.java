package com.andy.meetquickly.activity.user.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.StoreMediaAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.OrderDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdeEventDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right_text_button)
    TextView tvRightTextButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private OrderDetailBean.StoreActivityBean bean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_orde_event_detail;
    }

    @Override
    public void initView() {
        setBarTitle("此单活动");
        bean = (OrderDetailBean.StoreActivityBean) this.getIntent().getSerializableExtra("bean");
        tvContent.setText(bean.getContent());
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        StoreMediaAdapter adapter;
        adapter = new StoreMediaAdapter(bean.getStoreActivityImgs(), mContext, 1);
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

    public static void start(Context context, OrderDetailBean.StoreActivityBean bean) {
        Intent starter = new Intent(context, OrdeEventDetailActivity.class);
        starter.putExtra("bean", bean);
        context.startActivity(starter);
    }
}

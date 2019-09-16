package com.andy.meetquickly.activity.user.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.SelectShopImgAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.StoreImgTypeBean;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.LogUtil;
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

public class SelectImgActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    SelectShopImgAdapter adapter;
    List<StoreImgTypeBean> mData = new ArrayList<>();
    int type;
    private View notDataView;
    private View errorView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_img;
    }

    @Override
    public void initView() {
        setBarTitle("选择图片");
        type = this.getIntent().getIntExtra("type", 1);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        adapter = new SelectShopImgAdapter(mData, mContext);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                // 获取用户计算后的结果
                intent.putExtra("imgUrl", mData.get(position).getImgUrl()); //将计算的值回传回去
                setResult(2, intent);
                finish(); //结束当前的activity的生命周期
            }
        });
        recyclerView.setAdapter(adapter);
        notDataView = getLayoutInflater().inflate(R.layout.layout_empty_view, (ViewGroup) recyclerView.getParent(), false);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        errorView = getLayoutInflater().inflate(R.layout.layout_error_view, (ViewGroup) recyclerView.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        initData();
    }

    @Override
    public void initData() {
        showWaitDialog(SelectImgActivity.this);
        MQApi.getStoreTemplateByType(type, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
                adapter.setEmptyView(errorView);
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<StoreImgTypeBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<StoreImgTypeBean>>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<StoreImgTypeBean>>>() {});
                if (myApiResult.isOk()) {
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        mData.add(myApiResult.getData().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    if (mData.size() == 0) {
                        adapter.setEmptyView(notDataView);
                    }
                } else {
                    RxToast.showToast(myApiResult.getMsg());
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

    public static void start(Activity activity, int type) {
        Intent starter = new Intent(activity, SelectImgActivity.class);
        starter.putExtra("type", type);
        activity.startActivityForResult(starter, 200);
    }
}

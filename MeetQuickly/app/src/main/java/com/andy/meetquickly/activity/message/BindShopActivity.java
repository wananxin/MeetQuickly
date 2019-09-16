package com.andy.meetquickly.activity.message;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.BindUserAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.ShopBindUserBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.SelectDialog;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BindShopActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    List<ShopBindUserBean> mData = new ArrayList<>();
    private BindUserAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_shop;
    }

    @Override
    public void initView() {
        setBarTitle("邀请驻店");
        mData = (List<ShopBindUserBean>) this.getIntent().getSerializableExtra("listBean");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new BindUserAdapter(mData,mContext);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (!"1".equals(mData.get(position).getStatus())){
                    agreeBind(position);
                }
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                showDeleteDialog(position);
                return false;
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void showDeleteDialog(int position) {
        SelectDialog.show(mContext, "提示", "是否删除"+mData.get(position).getStoreName()+"的请求", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBind(position);
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCanCancel(true);
    }

    private void deleteBind(int position) {
        showWaitDialog(BindShopActivity.this);
        MQApi.updateMyUsers(mData.get(position).getId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult = JSON.parseObject(s,new MyApiResult().getClass());
                if (myApiResult.isOk()){
                    mData.remove(position);
                    adapter.notifyDataSetChanged();
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void agreeBind(int position) {
        showWaitDialog(BindShopActivity.this);
        MQApi.agreeManagerRecord(mData.get(position).getId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());
                if (myApiResult.isOk()){
                    mData.get(position).setStatus("1");
                    adapter.notifyItemChanged(position);
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
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

    public static void start(Context context,List<ShopBindUserBean> listBean) {
        Intent starter = new Intent(context, BindShopActivity.class);
        starter.putExtra("listBean", (Serializable) listBean);
        context.startActivity(starter);
    }
}

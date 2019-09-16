package com.andy.meetquickly.activity.message;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.UserDataActivity;
import com.andy.meetquickly.adapter.MyFraiendSimpleAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserFriendBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.fragment.order.OrderFinishFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.SelectDialog;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewFriendActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private MyFraiendSimpleAdapter adapter;
    private List<UserFriendBean> mData = new ArrayList<>();
    private UserBean userBean;
    private int mPage = 1;    //当前页面
    private View notDataView;
    private View errorView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_friend;
    }

    @Override
    public void initView() {
        setBarTitle("新的朋友");
        userBean = UserCache.getInstance().getUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyFraiendSimpleAdapter(mContext, mData,1);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserDataActivity.start(mContext,mData.get(position).getUid());
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_button){
                    addFriend(position);
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

    private void showDeleteDialog(int position) {
        SelectDialog.show(mContext, "提示", "是否删除"+mData.get(position).getNickName()+"的请求", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFriend(position);
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCanCancel(true);
    }

    private void deleteFriend(int position) {
        showWaitDialog(NewFriendActivity.this);
        MQApi.delUserFriend(mData.get(position).getId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<UserBean> myApiResult = JSON.parseObject(s,new MyApiResult<UserBean>().getClass());
                if (myApiResult.isOk()){
                    mData.remove(position);
                    adapter.notifyDataSetChanged();
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void addFriend(int position) {
        showWaitDialog(NewFriendActivity.this);
        MQApi.agreeUserFriendApply(mData.get(position).getId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<UserBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<UserBean>>(){}.getType());
//                        JSON.parseObject(s,new MyApiResult<UserBean>().getClass());
                if (myApiResult.isOk()){
                    mData.remove(position);
                    adapter.notifyDataSetChanged();
                    if (mData.size() == 0){
                        adapter.setEmptyView(notDataView);
                    }
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    public void initData() {
        showWaitDialog(NewFriendActivity.this);
        MQApi.getNewUserFriendApplyList(userBean.getUId(), mPage, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                adapter.setEmptyView(errorView);
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<UserFriendBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<UserFriendBean>>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<UserFriendBean>>>(){});
                if (myApiResult.isOk()) {
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        mData.add(myApiResult.getData().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    if (mData.size() == 0){
                        adapter.setEmptyView(notDataView);
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

    public static void start(Context context) {
        Intent starter = new Intent(context, NewFriendActivity.class);
        context.startActivity(starter);
    }
}

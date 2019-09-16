package com.andy.meetquickly.activity.user.set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.ShieldPhoneAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.ShieldPhoneBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserFriendBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.view.indexbar.FloatingBarItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qdx.indexbarlayout.IndexLayout;

public class ShieldPhoneActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.index_layout)
    IndexLayout indexLayout;
    private UserBean userBean;
    private int mPage = 1;
    private List<ShieldPhoneBean> mData = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private LinkedHashMap<Integer, String> mHeaderList;
    private ShieldPhoneAdapter adapter;
    private View notDataView;
    private View errorView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shield_phone;
    }

    @Override
    public void initView() {
        setBarTitle("屏蔽的号码");
        headNotify();
        userBean = UserCache.getInstance().getUser();
        mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(
                new FloatingBarItemDecoration(mContext, new FloatingBarItemDecoration.DecorationCallback() {
                    @Override
                    public long getGroupId(int position) {
                        if (mData.size() <= position) {
                            return position;
                        }else {
                            return Character.toUpperCase(mData.get(position).getInitial().charAt(0));
                        }

                    }

                    @Override
                    public String getGroupFirstLine(int position) {
                        if (mData.size() <= position) {
                            return "###";
                        }else {
                            return  mData.get(position).getInitial().substring(0, 1).toUpperCase();
                        }
                    }
                }));
        adapter = new ShieldPhoneAdapter(mData,mContext);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                delShield(position);
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

    private void delShield(int position) {
        showWaitDialog(ShieldPhoneActivity.this);
        MQApi.delUsersShieldListById(mData.get(position).getId(), new SimpleCallBack<String>() {
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
                    RxToast.showToast("取消屏蔽成功");
                    mData.remove(position);
                    dataChanged();
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }

            }
        });
    }

    @Override
    public void initData() {
        showWaitDialog(ShieldPhoneActivity.this);
        MQApi.getUsersShieldList(userBean.getUId(), mPage, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
                adapter.setEmptyView(errorView);
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<ShieldPhoneBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<ShieldPhoneBean>>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<UserFriendBean>>>() {});
                if (myApiResult.isOk()) {
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        ShieldPhoneBean userFriendBean = myApiResult.getData().get(i);
                        userFriendBean.setShieldName(myApiResult.getData().get(i).getShieldName());
                        mData.add(userFriendBean);
                    }
                    dataChanged();
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                    adapter.setEmptyView(errorView);
                }
            }
        });
    }

    private void dataChanged() {
        Collections.sort(mData);
        adapter.notifyDataSetChanged();
        headNotify();
        if (mData.size() > 0) {
            List<String> mHeaders = new ArrayList<>(mHeaderList.values());
            indexLayout.getIndexBar().setIndexsList(mHeaders);
            float i = (float) mHeaders.size() / 27;
            i = i * (float) 0.9;
            indexLayout.setIndexBarHeightRatio(i);
            indexLayout.setCircleColor(Color.RED);
            indexLayout.setCircleRadius(100);
            indexLayout.setCircleDuration(500);
            indexLayout.setCircleColor(ContextCompat.getColor(mContext, R.color.circle_bg));
            indexLayout.getIndexBar().setIndexChangeListener(new qdx.indexbarlayout.IndexBar.IndexChangeListener() {
                @Override
                public void indexChanged(String indexName) {
                    for (int i = 0; i < mData.size(); i++) {
                        if (indexName.equals(mData.get(i).getInitial())) {
                            mLayoutManager.scrollToPositionWithOffset(i + 1, 0);
                            return;
                        }
                    }
                }
            });
        }else {
            adapter.setEmptyView(notDataView);
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ShieldPhoneActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void headNotify() {
        if (mHeaderList == null) {
            mHeaderList = new LinkedHashMap<>();
        }
        mHeaderList.clear();
        if (mData.size() == 0) {
            return;
        }
        addHeaderToList(0, mData.get(0).getInitial());
        for (int i = 1; i < mData.size(); i++) {
            if (!mData.get(i - 1).getInitial().equalsIgnoreCase(mData.get(i).getInitial())) {
                addHeaderToList(i, mData.get(i).getInitial());
            }
        }
    }

    private void addHeaderToList(int index, String header) {
        mHeaderList.put(index, header);
    }
}

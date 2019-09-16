package com.andy.meetquickly.activity.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.UserDataActivity;
import com.andy.meetquickly.adapter.MyFraiendSimpleAdapter;
import com.andy.meetquickly.base.BaseActivity;
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

public class FansListActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.index_layout)
    IndexLayout indexLayout;

    private LinearLayoutManager mLayoutManager;
    private LinkedHashMap<Integer, String> mHeaderList;
    private ArrayList<UserFriendBean> mData = new ArrayList<>();
    private MyFraiendSimpleAdapter adapter;
    private UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_follow_list;
    }

    @Override
    public void initView() {
        setBarTitle("我的粉丝");
        headNotify();
        userBean = UserCache.getInstance().getUser();
        mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(
                new FloatingBarItemDecoration(mContext, new FloatingBarItemDecoration.DecorationCallback() {
                    @Override
                    public long getGroupId(int position) {
                        if (mData.size() == 1 && position == 1) {
                            return position;
                        }else {
                            return Character.toUpperCase(mData.get(position).getInitial().charAt(0));
                        }

                    }

                    @Override
                    public String getGroupFirstLine(int position) {
                        if (mData.size() == 1 && position == 1) {
                            return "";
                        }else {
                            return  mData.get(position).getInitial().substring(0, 1).toUpperCase();
                        }
                    }
                }));
        adapter = new MyFraiendSimpleAdapter(mContext,mData,3);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserDataActivity.start(mContext,mData.get(position).getUid());
            }
        });
        recyclerView.setAdapter(adapter);
        initData();
    }

    @Override
    public void initData() {
        showWaitDialog(FansListActivity.this);
        MQApi.getUserFansList(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<UserFriendBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<UserFriendBean>>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<UserFriendBean>>>() {});
                if (myApiResult.isOk()) {
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        UserFriendBean userFriendBean = myApiResult.getData().get(i);
                        userFriendBean.setNickName(myApiResult.getData().get(i).getNickName());
                        mData.add(userFriendBean);
                    }
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
                    }
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

    public static void start(Context context) {
        Intent starter = new Intent(context, FansListActivity.class);
        context.startActivity(starter);
    }
}

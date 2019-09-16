package com.andy.meetquickly.fragment.shopinfo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.UserDataActivity;
import com.andy.meetquickly.adapter.ShopMeetAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
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
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopMeetFragment extends BaseLazyFragment {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private static final String STORE_ID = "storeId";
    private String storeId = "";
    private int mPage = 1;
    private List<UserBean> mData = new ArrayList<>();
    private ShopMeetAdapter adapter;

    public ShopMeetFragment() {
        // Required empty public constructor
    }

    @Override
    public void fetchData() {
        MQApi.getShopEncounterListByStoreId(storeId, mPage, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
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
    public void doItEvery() {

    }

    // TODO: Customize parameter initialization
    public static ShopMeetFragment newInstance(String storeId) {
        ShopMeetFragment fragment = new ShopMeetFragment();
        Bundle args = new Bundle();
        args.putString(STORE_ID, storeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            storeId = getArguments().getString(STORE_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_meet, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        recyclerView.setTag(3);
        recyclerView.setLayoutManager(new GridLayoutManager(ShopMeetFragment.this.getContext() , 2));
        adapter = new ShopMeetAdapter(mData,ShopMeetFragment.this.getContext());
        //adapter设置点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserDataActivity.start(ShopMeetFragment.this.getContext(),mData.get(position).getUId());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

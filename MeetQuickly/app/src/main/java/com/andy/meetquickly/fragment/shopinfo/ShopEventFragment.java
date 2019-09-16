package com.andy.meetquickly.fragment.shopinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.ShopEventAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.ShopEventBean;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
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


public class ShopEventFragment extends BaseLazyFragment {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private static final String STORE_ID = "storeId";
    private String storeId = "";
    private int mPage = 1;
    private List<ShopEventBean> mData = new ArrayList<>();
    private ShopEventAdapter adapter;

    public ShopEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void fetchData() {
        MQApi.getShopDiscountDetailsByStoreId(storeId, mPage, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<ShopEventBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<ShopEventBean>>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<ShopEventBean>>>(){});
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
    public static ShopEventFragment newInstance(String storeId) {
        ShopEventFragment fragment = new ShopEventFragment();
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
        View view = inflater.inflate(R.layout.fragment_shop_event, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        recyclerView.setTag(1);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShopEventFragment.this.getContext()));
        adapter = new ShopEventAdapter(mData);
        //adapter设置点击事件
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

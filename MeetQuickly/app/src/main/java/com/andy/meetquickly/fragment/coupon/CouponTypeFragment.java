package com.andy.meetquickly.fragment.coupon;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.ShopDetailActivity;
import com.andy.meetquickly.activity.user.ManageShopActivity;
import com.andy.meetquickly.adapter.CouponIngAdapter;
import com.andy.meetquickly.adapter.CouponInvalidaAdapter;
import com.andy.meetquickly.adapter.CouponUseAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.CouponBean;
import com.andy.meetquickly.bean.ShopInfoBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.screenShopMessage;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CouponTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CouponTypeFragment extends BaseLazyFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TYPE = "type";
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    CouponIngAdapter ingAdapter;
    CouponUseAdapter useAdapter;
    CouponInvalidaAdapter invalidaAdapter;
    List<CouponBean> mData = new ArrayList<>();
    UserBean userBean;
    Intent intent;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    // TODO: Rename and change types of parameters
    private int mParamType;
    private View notDataView;
    private View errorView;
    //0是未使用，1是已使用，2是未使用


    public CouponTypeFragment() {
        // Required empty public constructor
    }

    @Override
    public void fetchData() {
        userBean = UserCache.getInstance().getUser();
        intent = new Intent();
        initData();
    }

    private void initData() {
        MQApi.getUserCouponInfoList(userBean.getUId(), mParamType, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                swipeRefreshLayout.setRefreshing(false);
                RxToast.showToast(e.toString());
                if (mParamType == 0) {
                    ingAdapter.setEmptyView(errorView);
                } else if (mParamType == 1) {
                    useAdapter.setEmptyView(errorView);
                } else {
                    invalidaAdapter.setEmptyView(errorView);
                }
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<CouponBean>> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<List<CouponBean>>>() {
                }.getType());
                if (myApiResult.isOk()) {
                    mData.clear();
                    swipeRefreshLayout.setRefreshing(false);
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        mData.add(myApiResult.getData().get(i));
                    }
                    if (mParamType == 0) {
                        ingAdapter.notifyDataSetChanged();
                    } else if (mParamType == 1) {
                        useAdapter.notifyDataSetChanged();
                    } else {
                        invalidaAdapter.notifyDataSetChanged();
                    }

                    if (mData.size() == 0) {
                        if (mParamType == 0) {
                            tvContent.setText("未使用0");
                            ingAdapter.setEmptyView(notDataView);
                        } else if (mParamType == 1) {
                            tvContent.setText("已使用0");
                            useAdapter.setEmptyView(notDataView);
                        } else {
                            tvContent.setText("已失效0");
                            invalidaAdapter.setEmptyView(notDataView);
                        }
                    } else {
                        if (mParamType == 0) {
                            tvContent.setText("未使用" + mData.get(0).getAmountTotal());
                        } else if (mParamType == 1) {
                            tvContent.setText("已使用" + mData.get(0).getAmountTotal());
                        } else {
                            tvContent.setText("已失效" + mData.get(0).getAmountTotal());
                        }
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    RxToast.showToast(myApiResult.getMsg());
                    if (mParamType == 0) {
                        ingAdapter.setEmptyView(errorView);
                    } else if (mParamType == 1) {
                        useAdapter.setEmptyView(errorView);
                    } else {
                        invalidaAdapter.setEmptyView(errorView);
                    }
                }
            }
        });
    }

    @Override
    public void doItEvery() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CouponTypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CouponTypeFragment newInstance(int type) {
        CouponTypeFragment fragment = new CouponTypeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamType = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coupon_type, container, false);
        view.setTag(mParamType);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(CouponTypeFragment.this.getContext()));
        switch (mParamType) {
            case 0:
                tvContent.setText("未使用0");
                ingAdapter = new CouponIngAdapter(mData);
                ingAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        if ("1".equals(mData.get(position).getUseScope())) {
                            EventBus.getDefault().post(new screenShopMessage(1, 0, 10, 0, ""));
                            intent.putExtra("numb", 0);
                            CouponTypeFragment.this.getActivity().setResult(RESULT_OK, intent);
                            CouponTypeFragment.this.getActivity().finish();
                        } else if ("2".equals(mData.get(position).getUseScope())) {
                            EventBus.getDefault().post(new screenShopMessage(2, 0, 10, 0, ""));
                            intent.putExtra("numb", 0);
                            CouponTypeFragment.this.getActivity().setResult(RESULT_OK, intent);
                            CouponTypeFragment.this.getActivity().finish();
                        } else if ("3".equals(mData.get(position).getUseScope())) {
                            EventBus.getDefault().post(new screenShopMessage(3, 0, 10, 0, ""));
                            intent.putExtra("numb", 0);
                            CouponTypeFragment.this.getActivity().setResult(RESULT_OK, intent);
                            CouponTypeFragment.this.getActivity().finish();
                        } else if ("4".equals(mData.get(position).getUseScope())) {
                            EventBus.getDefault().post(new screenShopMessage(4, 0, 10, 0, ""));
                            intent.putExtra("numb", 0);
                            CouponTypeFragment.this.getActivity().setResult(RESULT_OK, intent);
                            CouponTypeFragment.this.getActivity().finish();
                        } else if ("5".equals(mData.get(position).getUseScope())) {
                            EventBus.getDefault().post(new screenShopMessage(0, 0, 10, 0, ""));
                            intent.putExtra("numb", 0);
                            CouponTypeFragment.this.getActivity().setResult(RESULT_OK, intent);
                            CouponTypeFragment.this.getActivity().finish();
                        } else if ("6".equals(mData.get(position).getUseScope())) {
//                            EventBus.getDefault().post(new screenShopMessage(0, 0, 10, 0, mData.get(position).getStoreId()));
//                            intent.putExtra("numb", 0);
//                            CouponTypeFragment.this.getActivity().setResult(RESULT_OK, intent);
//                            CouponTypeFragment.this.getActivity().finish();
                            getShopInfo(mData.get(position).getStoreId());
                        } else if ("7".equals(mData.get(position).getUseScope())) {
//                            EventBus.getDefault().post(new screenShopMessage(0, 0, 10, 0, mData.get(position).getStoreId()));
//                            intent.putExtra("numb", 0);
//                            CouponTypeFragment.this.getActivity().setResult(RESULT_OK, intent);
//                            CouponTypeFragment.this.getActivity().finish();
                            getShopInfo(mData.get(position).getStoreId());
                        }
                    }
                });
                recyclerView.setAdapter(ingAdapter);
                break;
            case 1:
                tvContent.setText("已使用0");
                useAdapter = new CouponUseAdapter(mData);
                recyclerView.setAdapter(useAdapter);
                break;
            case 2:
                tvContent.setText("已失效0");
                invalidaAdapter = new CouponInvalidaAdapter(mData);
                recyclerView.setAdapter(invalidaAdapter);
                break;
        }
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getShopInfo(String storeId) {
        MQApi.getShopsList(userBean.getUId(), storeId, 1, 0, 0, 10, 0,
                RxSPTool.getContent(CouponTypeFragment.this.getContext(), Constant.KEY_LONGITUDE),
                RxSPTool.getContent(CouponTypeFragment.this.getContext(), Constant.KEY_LATITUDE), new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        dissmissWaitDialog();
                        RxToast.showToast(e.toString());
                    }

                    @Override
                    public void onSuccess(String s) {
                        dissmissWaitDialog();
//                MyApiResult<List<ShopInfoBean>> myApiResult = JSON.parseObject(s, new MyApiResult<List<ShopInfoBean>>().getClass());
                        MyApiResult<List<ShopInfoBean>> myApiResult =
                                new Gson().fromJson(s, new TypeToken<MyApiResult<List<ShopInfoBean>>>() {
                                }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<ShopInfoBean>>>(){});
                        if (myApiResult.isOk()) {
                            if ((null != myApiResult.getData()) && (myApiResult.getData().size() == 1)){
                                ShopInfoBean shopInfoBean = myApiResult.getData().get(0);
                                ShopDetailActivity.start(CouponTypeFragment.this.getContext(),shopInfoBean.getStoreId(),shopInfoBean.getStoreName(),
                                        shopInfoBean.getKind(),shopInfoBean.getStoreCoupon(),shopInfoBean.getIsCoupon(),
                                        shopInfoBean.getCompanyName());
                                CouponTypeFragment.this.getActivity().finish();
                            }else {
                                RxToast.showToast("数据异常");
                            }
                        } else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }
}

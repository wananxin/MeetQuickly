package com.andy.meetquickly.fragment.shopinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.UserDataActivity;
import com.andy.meetquickly.activity.home.UserVisibListActivity;
import com.andy.meetquickly.adapter.VisitPeopleAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.ShopDetailBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ShopInfoFragment extends BaseLazyFragment {

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    Unbinder unbinder;

    private static final String STORE_ID = "storeId";
    @BindView(R.id.tv_name)                   
    TextView tvName;
    @BindView(R.id.tv_small_name)
    TextView tvSmallName;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_jiushui)
    TextView tvJiushui;
    @BindView(R.id.tv_daijinquan)
    TextView tvDaijinquan;
    @BindView(R.id.tv_xiaofeibili)
    TextView tvXiaofeibili;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.recyclerView6)
    RecyclerView recyclerView;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.ratingBar1)
    RatingBar ratingBar1;
    @BindView(R.id.ratingBar2)
    RatingBar ratingBar2;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_driving)
    TextView tvDriving;
    private String storeId = "";
    private List<ShopDetailBean.StoreVisitBean> mData = new ArrayList<>();
    private VisitPeopleAdapter adapter;
    private UserBean userBean;

    public ShopInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void fetchData() {
        String Uid;
        if (null != userBean) {
            Uid = userBean.getUId();
        } else {
            Uid = "";
        }
        MQApi.getShopDataDetailsByStoreId(Uid, storeId,
                RxSPTool.getString(ShopInfoFragment.this.getContext(),Constant.KEY_LONGITUDE),
                RxSPTool.getString(ShopInfoFragment.this.getContext(),Constant.KEY_LATITUDE),
                new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<ShopDetailBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<ShopDetailBean>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<ShopDetailBean>>() {});
                if (myApiResult.isOk()) {
                    tvName.setText(myApiResult.getData().getCompanyName());
                    tvSmallName.setText("（" + myApiResult.getData().getStoreName() + "经理 店号：" + myApiResult.getData().getStoreId() + "）");
                    tvDistance.setText(myApiResult.getData().getDistance() + "km");
                    if (StringUtil.isNotNull(myApiResult.getData().getDiscount())) {
                        tvJiushui.setText("酒水：" + myApiResult.getData().getDiscount() + "折");
                    } else {
                        tvJiushui.setText("酒水：无折扣");
                    }
                    if (StringUtil.isNotNull(myApiResult.getData().getCashCouponReturn())) {
                        tvDaijinquan.setText("代金券：" + myApiResult.getData().getCashCouponReturn());
                    } else {
                        tvDaijinquan.setText("代金券：无");
                    }

                    if (StringUtil.isNotNull(myApiResult.getData().getIsFreeDriving())&& "1".equals(myApiResult.getData().getIsFreeDriving())){
                        tvDriving.setText("免费代驾：限20km内");
                    }else {
                        tvDriving.setText("免费代驾：无");
                    }

                    if (StringUtil.isNotNull(myApiResult.getData().getCashCouponProportion())) {
                        tvXiaofeibili.setText("代金券抵消费比例：" + myApiResult.getData().getCashCouponProportion());
                    } else {
                        tvXiaofeibili.setText("代金券抵消费比例：无");
                    }
                    if (StringUtil.isNotNull(myApiResult.getData().getBusinessTime())){
                        tvTime.setText("营业时间：" + myApiResult.getData().getBusinessTime());
                    }else {
                        tvTime.setText("营业时间：");
                    }
                    tvAddress.setText("地址：" + myApiResult.getData().getAddress());
                    tvContent.setText(myApiResult.getData().getRemark());
                    ratingBar.setRating(Float.parseFloat(myApiResult.getData().getServiceScore()));
                    ratingBar1.setRating(Float.parseFloat(myApiResult.getData().getPlaceScore()));
                    ratingBar2.setRating(Float.parseFloat(myApiResult.getData().getSatisfiedScore()));
                    if (null != myApiResult.getData().getStoreVisit()) {
                        for (ShopDetailBean.StoreVisitBean storeVisitBean : myApiResult.getData().getStoreVisit()) {
                            mData.add(storeVisitBean);
                        }
                        adapter.notifyDataSetChanged();
                    }
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
    public static ShopInfoFragment newInstance(String storeId) {
        ShopInfoFragment fragment = new ShopInfoFragment();
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
        userBean = UserCache.getInstance().getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_info, container, false);
        view.setTag(0);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShopInfoFragment.this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new VisitPeopleAdapter(mData, ShopInfoFragment.this.getContext());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserDataActivity.start(ShopInfoFragment.this.getContext(), mData.get(position).getUId());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.imageViewMore)
    public void onViewClicked() {
        UserVisibListActivity.start(ShopInfoFragment.this.getContext(), 2, storeId);
    }
}

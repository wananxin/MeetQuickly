package com.andy.meetquickly.fragment.userinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.ReceivedGiftActivity;
import com.andy.meetquickly.activity.home.ShopDetailActivity;
import com.andy.meetquickly.adapter.OftenPlaceAdapter;
import com.andy.meetquickly.adapter.UserGiftAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserDataInfoBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.StringUtil;
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
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserInfoFragment extends BaseLazyFragment {


    @BindView(R.id.recycler_like_shop)
    RecyclerView recyclerLikeShop;
    @BindView(R.id.tv_interest_sport)
    TextView tvInterestSport;
    @BindView(R.id.tv_interest_music)
    TextView tvInterestMusic;
    @BindView(R.id.tv_interest_eat)
    TextView tvInterestEat;
    @BindView(R.id.tv_interest_movie)
    TextView tvInterestMovie;
    @BindView(R.id.tv_interest_book)
    TextView tvInterestBook;
    @BindView(R.id.tv_interest_address)
    TextView tvInterestAddress;
    @BindView(R.id.recycler_gift)
    RecyclerView recyclerGift;
    @BindView(R.id.scrollview)
    NestedScrollView scrollview;
    Unbinder unbinder;
    private static final String U_ID = "uId";
    @BindView(R.id.tv_user_hangye)
    TextView tvUserHangye;
    @BindView(R.id.tv_user_lingyu)
    TextView tvUserLingyu;
    @BindView(R.id.tv_user_gongsi)
    TextView tvUserGongsi;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.tv_hobby)
    TextView tvHobby;
    @BindView(R.id.tv_Tag)
    TextView tvTag;
    @BindView(R.id.iv_received_gift)
    ImageView ivReceivedGift;
    private String uId = "";
    private UserBean userBean;
    private List<UserDataInfoBean.ManagerStoreBean> mDataShop = new ArrayList<>();
    private List<UserDataInfoBean.GiftsEntityBean> mDataGift = new ArrayList<>();
    private OftenPlaceAdapter adapter;
    private UserGiftAdapter userGiftAdapter;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    private void initData() {
        //type  0 是常规拉取   1 是只要拉取我的网店
        MQApi.getUserDataInfo(uId, userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<UserDataInfoBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<UserDataInfoBean>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<UserDataInfoBean>>() {});
                if (myApiResult.isOk()) {
                    //个性标签
                    if (myApiResult.getData().getTags().size() == 0) {
                        tvTag.setText("暂无个性标签");
                    } else {
                        tvTag.setText(myApiResult.getData().getTags().get(0).getContent());
                    }

                    //运动
                    if (myApiResult.getData().getSports().size() == 0) {
                        tvInterestSport.setText("暂无喜欢的运动");
                    } else {
                        tvInterestSport.setText(myApiResult.getData().getSports().get(0).getContent());
                    }

                    //音乐
                    if (myApiResult.getData().getMusics().size() == 0) {
                        tvInterestMusic.setText("暂无喜欢的音乐");
                    } else {
                        tvInterestMusic.setText(myApiResult.getData().getMusics().get(0).getContent());
                    }

                    //食物
                    if (myApiResult.getData().getFoods().size() == 0) {
                        tvInterestEat.setText("暂无喜欢的食物");
                    } else {
                        tvInterestEat.setText(myApiResult.getData().getFoods().get(0).getContent());
                    }

                    //电影
                    if (myApiResult.getData().getFilms().size() == 0) {
                        tvInterestMovie.setText("暂无喜欢的电影");
                    } else {
                        tvInterestMovie.setText(myApiResult.getData().getFilms().get(0).getContent());
                    }

                    //书籍
                    if (myApiResult.getData().getBooks().size() == 0) {
                        tvInterestBook.setText("暂无喜欢的书籍");
                    } else {
                        tvInterestBook.setText(myApiResult.getData().getBooks().get(0).getContent());
                    }

                    //旅行
                    if (myApiResult.getData().getVacation().size() == 0) {
                        tvInterestAddress.setText("暂无旅行足迹");
                    } else {
                        tvInterestAddress.setText(myApiResult.getData().getVacation().get(0).getContent());
                    }

                    //行业信息
                    if (myApiResult.getData().getIndustry().size() == 0) {
                        tvUserHangye.setText("暂无行业信息");
                    } else {
                        tvUserHangye.setText(myApiResult.getData().getIndustry().get(0).getContent());
                    }

                    //工作领域
                    if (myApiResult.getData().getWorks().size() == 0) {
                        tvUserLingyu.setText("暂无工作领域信息");
                    } else {
                        tvUserLingyu.setText(myApiResult.getData().getWorks().get(0).getContent());
                    }

                    //公司信息
                    if (StringUtil.isNotNull(myApiResult.getData().getUserBaseInfo().get(0).getCompany())) {
                        tvUserGongsi.setText(myApiResult.getData().getUserBaseInfo().get(0).getCompany());
                    } else {
                        tvUserGongsi.setText("暂无公司信息");
                    }

                    //共同爱好
                    if (myApiResult.getData().getCommonInterest().size() == 0) {
                        tvHobby.setText("暂无共同的爱好");
                    } else {
                        tvHobby.setText(myApiResult.getData().getCommonInterest().get(0).getContent());
                    }

                    //家乡信息
                    if (StringUtil.isNotNull(myApiResult.getData().getUserBaseInfo().get(0).getHomeCity())) {
                        tvUserAddress.setText(myApiResult.getData().getUserBaseInfo().get(0).getHomeProvince() + "-" +
                                myApiResult.getData().getUserBaseInfo().get(0).getHomeCity() + "-" +
                                myApiResult.getData().getUserBaseInfo().get(0).getHomeDistrict());
                    } else {
                        tvUserAddress.setText("暂无家乡信息");
                    }

                    //常去场所
                    mDataShop.clear();
                    for (int i = 0; i < myApiResult.getData().getManagerStore().size(); i++) {
                        mDataShop.add(myApiResult.getData().getManagerStore().get(i));
                    }
                    adapter.notifyDataSetChanged();

                    mDataGift.clear();
                    for (int i = 0; i < myApiResult.getData().getGiftsEntity().size(); i++) {
                        mDataGift.add(myApiResult.getData().getGiftsEntity().get(i));
                    }
                    userGiftAdapter.notifyDataSetChanged();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    public void fetchData() {
        initData();
    }

    @Override
    public void doItEvery() {

    }

    // TODO: Customize parameter initialization
    public static UserInfoFragment newInstance(String uId) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putString(U_ID, uId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uId = getArguments().getString(U_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        userBean = UserCache.getInstance().getUser();
        initView();
        return view;
    }

    private void initView() {
        scrollview.setTag(1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserInfoFragment.this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerLikeShop.setLayoutManager(linearLayoutManager);
        adapter = new OftenPlaceAdapter(mDataShop,UserInfoFragment.this.getContext(),1);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShopDetailActivity.start(UserInfoFragment.this.getContext(),mDataShop.get(position).getStoreId(),
                        mDataShop.get(position).getStoreName(),mDataShop.get(position).getKind(),
                        null,"1",mDataShop.get(position).getCompanyName());
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ShopDetailActivity.start(UserInfoFragment.this.getContext(),mDataShop.get(position).getStoreId(),
                        mDataShop.get(position).getStoreName(),mDataShop.get(position).getKind(),
                        null,"1",mDataShop.get(position).getCompanyName());
            }
        });
        recyclerLikeShop.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(UserInfoFragment.this.getContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerGift.setLayoutManager(linearLayoutManager1);
        userGiftAdapter = new UserGiftAdapter(mDataGift,UserInfoFragment.this.getContext());
        recyclerGift.setAdapter(userGiftAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.textView118, R.id.iv_received_gift})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView118:
                break;
            case R.id.iv_received_gift:
                ReceivedGiftActivity.start(UserInfoFragment.this.getContext(),mDataGift);
                break;
        }
    }
}

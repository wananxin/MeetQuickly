package com.andy.meetquickly.fragment.shopinfo;


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
import com.andy.meetquickly.adapter.ShopDandianAdapter;
import com.andy.meetquickly.adapter.ShopSeatTypeAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.UserShopDetailBean;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/16  15:00
 * 描述：     消费参考
 */
public class ShopConsumeFragment extends BaseLazyFragment {


    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;
    Unbinder unbinder;
    private static final String STORE_ID = "storeId";
    @BindView(R.id.recycler_seat_type)
    RecyclerView recyclerSeatType;
    @BindView(R.id.recycler_pijiu)
    RecyclerView recyclerPijiu;
    @BindView(R.id.recycler_yangjiu)
    RecyclerView recyclerYangjiu;
    @BindView(R.id.recycler_hongjiu)
    RecyclerView recyclerHongjiu;
    @BindView(R.id.recycler_jiweijiu)
    RecyclerView recyclerJiweijiu;
    @BindView(R.id.recycler_xiaochi)
    RecyclerView recyclerXiaochi;
    @BindView(R.id.recycler_qita)
    RecyclerView recyclerQita;
    @BindView(R.id.recycler_taocan)
    RecyclerView recyclerTaocan;
    private String storeId = "";
    List<UserShopDetailBean.StoreTableTypeBean> seatDatas = new ArrayList<>();
    List<UserShopDetailBean.SpendTypeListBean> pijiuDatas = new ArrayList<>();
    List<UserShopDetailBean.SpendTypeListBean> yangjiuDatas = new ArrayList<>();
    List<UserShopDetailBean.SpendTypeListBean> hongjiuDatas = new ArrayList<>();
    List<UserShopDetailBean.SpendTypeListBean> jiweijiuDatas = new ArrayList<>();
    List<UserShopDetailBean.SpendTypeListBean> xiaochiDatas = new ArrayList<>();
    List<UserShopDetailBean.SpendTypeListBean> qitaDatas = new ArrayList<>();
    List<UserShopDetailBean.SpendTypeListBean> taocanDatas = new ArrayList<>();
    private ShopSeatTypeAdapter seatAdapter;
    private ShopDandianAdapter pijiuAdapter;
    private ShopDandianAdapter yangjiuAdapter;
    private ShopDandianAdapter hongjiuAdapter;
    private ShopDandianAdapter jiweijiuAdapter;
    private ShopDandianAdapter xiaochiAdapter;
    private ShopDandianAdapter qitaAdapter;
    private ShopDandianAdapter taocanAdapter;
    private CustomDialog customDialog;

    public ShopConsumeFragment() {
        // Required empty public constructor
    }

    @Override
    public void fetchData() {
        initData();
    }

    private void initData() {
        MQApi.getShopConsumptionReferenceByStoreId(storeId, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<UserShopDetailBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<UserShopDetailBean>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<UserShopDetailBean>>() {});
                if (myApiResult.isOk()) {

                    for (int i = 0; i < myApiResult.getData().getStoreTableType().size(); i++) {
                        seatDatas.add(myApiResult.getData().getStoreTableType().get(i));
                    }
                    seatAdapter.notifyDataSetChanged();

                    for (int i = 0; i < myApiResult.getData().getBeerList().size(); i++) {
                        pijiuDatas.add(myApiResult.getData().getBeerList().get(i));
                    }
                    pijiuAdapter.notifyDataSetChanged();

                    for (int i = 0; i < myApiResult.getData().getWineList().size(); i++) {
                        yangjiuDatas.add(myApiResult.getData().getWineList().get(i));
                    }
                    yangjiuAdapter.notifyDataSetChanged();

                    for (int i = 0; i < myApiResult.getData().getRedWineList().size(); i++) {
                        hongjiuDatas.add(myApiResult.getData().getRedWineList().get(i));
                    }
                    hongjiuAdapter.notifyDataSetChanged();

                    for (int i = 0; i < myApiResult.getData().getCocktailList().size(); i++) {
                        jiweijiuDatas.add(myApiResult.getData().getCocktailList().get(i));
                    }
                    jiweijiuAdapter.notifyDataSetChanged();

                    for (int i = 0; i < myApiResult.getData().getSnackList().size(); i++) {
                        xiaochiDatas.add(myApiResult.getData().getSnackList().get(i));
                    }
                    xiaochiAdapter.notifyDataSetChanged();

                    for (int i = 0; i < myApiResult.getData().getDrinksList().size(); i++) {
                        qitaDatas.add(myApiResult.getData().getDrinksList().get(i));
                    }
                    qitaAdapter.notifyDataSetChanged();

                    for (int i = 0; i < myApiResult.getData().getMealList().size(); i++) {
                        taocanDatas.add(myApiResult.getData().getMealList().get(i));
                    }
                    taocanAdapter.notifyDataSetChanged();
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
    public static ShopConsumeFragment newInstance(String storeId) {
        ShopConsumeFragment fragment = new ShopConsumeFragment();
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
        View view = inflater.inflate(R.layout.fragment_shop_consume, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        scrollView.setTag(2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ShopConsumeFragment.this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerSeatType.setLayoutManager(layoutManager);
        seatAdapter = new ShopSeatTypeAdapter(seatDatas);
        recyclerSeatType.setAdapter(seatAdapter);
        seatAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(ShopConsumeFragment.this.getContext());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerPijiu.setLayoutManager(layoutManager1);
        pijiuAdapter = new ShopDandianAdapter(pijiuDatas,ShopConsumeFragment.this.getContext());
        recyclerPijiu.setAdapter(pijiuAdapter);
        pijiuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showConsumeDialog(pijiuDatas.get(position));
            }
        });

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(ShopConsumeFragment.this.getContext());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerYangjiu.setLayoutManager(layoutManager2);
        yangjiuAdapter = new ShopDandianAdapter(yangjiuDatas,ShopConsumeFragment.this.getContext());
        recyclerYangjiu.setAdapter(yangjiuAdapter);
        yangjiuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showConsumeDialog(yangjiuDatas.get(position));
            }
        });

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(ShopConsumeFragment.this.getContext());
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerHongjiu.setLayoutManager(layoutManager3);
        hongjiuAdapter = new ShopDandianAdapter(hongjiuDatas,ShopConsumeFragment.this.getContext());
        recyclerHongjiu.setAdapter(hongjiuAdapter);
        hongjiuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showConsumeDialog(hongjiuDatas.get(position));
            }
        });

        LinearLayoutManager layoutManager4 = new LinearLayoutManager(ShopConsumeFragment.this.getContext());
        layoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerJiweijiu.setLayoutManager(layoutManager4);
        jiweijiuAdapter = new ShopDandianAdapter(jiweijiuDatas,ShopConsumeFragment.this.getContext());
        recyclerJiweijiu.setAdapter(jiweijiuAdapter);
        jiweijiuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showConsumeDialog(jiweijiuDatas.get(position));
            }
        });

        LinearLayoutManager layoutManager5 = new LinearLayoutManager(ShopConsumeFragment.this.getContext());
        layoutManager5.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerXiaochi.setLayoutManager(layoutManager5);
        xiaochiAdapter = new ShopDandianAdapter(xiaochiDatas,ShopConsumeFragment.this.getContext());
        recyclerXiaochi.setAdapter(xiaochiAdapter);
        xiaochiAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showConsumeDialog(xiaochiDatas.get(position));
            }
        });

        LinearLayoutManager layoutManager6 = new LinearLayoutManager(ShopConsumeFragment.this.getContext());
        layoutManager6.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerQita.setLayoutManager(layoutManager6);
        qitaAdapter = new ShopDandianAdapter(qitaDatas,ShopConsumeFragment.this.getContext());
        recyclerQita.setAdapter(qitaAdapter);
        qitaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showConsumeDialog(qitaDatas.get(position));
            }
        });

        LinearLayoutManager layoutManager7 = new LinearLayoutManager(ShopConsumeFragment.this.getContext());
        layoutManager7.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerTaocan.setLayoutManager(layoutManager7);
        taocanAdapter = new ShopDandianAdapter(taocanDatas,ShopConsumeFragment.this.getContext());
        recyclerTaocan.setAdapter(taocanAdapter);
        taocanAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showConsumeDialog(taocanDatas.get(position));
            }
        });
    }

    private void showConsumeDialog(UserShopDetailBean.SpendTypeListBean spendTypeListBean) {
        customDialog = CustomDialog.show(ShopConsumeFragment.this.getContext(), R.layout.dialog_consumption, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
                ImageView imageView = rootView.findViewById(R.id.iv_pic);
                TextView textViewName = rootView.findViewById(R.id.textViewName);
                TextView textViewContent = rootView.findViewById(R.id.textViewContent);
                TextView textViewYuanjia = rootView.findViewById(R.id.textViewYuanjia);
                TextView textViewXianjia = rootView.findViewById(R.id.textViewXianjia);
                ImageLoaderUtil.getInstance().loadImage(ShopConsumeFragment.this.getContext(),spendTypeListBean.getDrinksImg(),imageView);
                textViewName.setText(spendTypeListBean.getName());
                textViewContent.setText(spendTypeListBean.getRemark());
                textViewYuanjia.setText("原价：￥" + spendTypeListBean.getCost());
                textViewXianjia.setText("现价：￥" + spendTypeListBean.getPrice());

            }
        }).setCanCancel(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

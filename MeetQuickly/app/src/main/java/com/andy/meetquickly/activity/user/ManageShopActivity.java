package com.andy.meetquickly.activity.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.ShopDetailActivity;
import com.andy.meetquickly.adapter.ManageShopActivityAdapter;
import com.andy.meetquickly.adapter.ManageShopSeatAdapter;
import com.andy.meetquickly.adapter.ManageShopSpendTypeAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.ShareInfoBean;
import com.andy.meetquickly.bean.ShopInfoBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserShopDetailBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.fragment.home.WherePlayFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.DecimalDigitsInputFilter;
import com.andy.meetquickly.utils.LogUtil;
import com.andy.meetquickly.utils.SoftHideKeyBoardUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageShopActivity extends BaseActivity {

    @BindView(R.id.et_discount)
    EditText etDiscount;
    @BindView(R.id.recycler_view_event)
    RecyclerView recyclerViewEvent;
    @BindView(R.id.iv_add_event)
    ImageView ivAddEvent;
    @BindView(R.id.recycler_view_seat)
    RecyclerView recyclerViewSeat;
    @BindView(R.id.iv_add_seat)
    ImageView ivAddSeat;
    @BindView(R.id.et_special)
    EditText etSpecial;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_edit_time)
    TextView tvEditTime;
    @BindView(R.id.switch_business)
    Switch switchBusiness;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.recycler_view_pijiu)
    RecyclerView recyclerViewPijiu;
    @BindView(R.id.iv_add_pijiu)
    ImageView ivAddPijiu;
    @BindView(R.id.recycler_view_yangjiu)
    RecyclerView recyclerViewYangjiu;
    @BindView(R.id.iv_add_yangjiu)
    ImageView ivAddYangjiu;
    @BindView(R.id.recycler_view_hongjiu)
    RecyclerView recyclerViewHongjiu;
    @BindView(R.id.iv_add_hongjiu)
    ImageView ivAddHongjiu;
    @BindView(R.id.recycler_view_jiweijiu)
    RecyclerView recyclerViewJiweijiu;
    @BindView(R.id.iv_add_jiweijiu)
    ImageView ivAddJiweijiu;
    @BindView(R.id.recycler_view_xiaochi)
    RecyclerView recyclerViewXiaochi;
    @BindView(R.id.iv_add_xiaochi)
    ImageView ivAddXiaochi;
    @BindView(R.id.recycler_view_other)
    RecyclerView recyclerViewOther;
    @BindView(R.id.iv_add_other)
    ImageView ivAddOther;
    @BindView(R.id.recycler_view_taocan)
    RecyclerView recyclerViewTaocan;
    @BindView(R.id.iv_add_taocan)
    ImageView ivAddTaocan;
    ManageShopActivityAdapter activityAdapter;
    ManageShopSeatAdapter seatAdapter;
    ManageShopSpendTypeAdapter pijiuAdapter;
    ManageShopSpendTypeAdapter yangjiuAdapter;
    ManageShopSpendTypeAdapter hongjiuAdapter;
    ManageShopSpendTypeAdapter jiweijiuAdapter;
    ManageShopSpendTypeAdapter xiaochiAdapter;
    ManageShopSpendTypeAdapter qitaAdapter;
    ManageShopSpendTypeAdapter taocanAdapter;
    List<UserShopDetailBean.StoreDiscountsTypeBean> activityDatas = new ArrayList<>();
    List<UserShopDetailBean.StoreTableTypeBean> seatDatas = new ArrayList<>();
    List<UserShopDetailBean.SpendTypeListBean> pijiuDatas = new ArrayList<>();
    List<UserShopDetailBean.SpendTypeListBean> yangjiuDatas = new ArrayList<>();
    List<UserShopDetailBean.SpendTypeListBean> hongjiuDatas = new ArrayList<>();
    List<UserShopDetailBean.SpendTypeListBean> jiweijiuDatas = new ArrayList<>();
    List<UserShopDetailBean.SpendTypeListBean> xiaochiDatas = new ArrayList<>();
    List<UserShopDetailBean.SpendTypeListBean> qitaDatas = new ArrayList<>();
    List<UserShopDetailBean.SpendTypeListBean> taocanDatas = new ArrayList<>();
    private UserBean userBean;
    private UserShopDetailBean userShopDetailBean;
    private SimpleDateFormat sdf;
    private String startTime;
    private TimePickerView pvTime;
    private String endTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_manage_shop;
    }

    @Override
    public void initView() {
        setBarTitle("管理网店");
        setRightTextAndClick("预览", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getShopInfo();
            }
        });
        userBean = UserCache.getInstance().getUser();
        SoftHideKeyBoardUtil.assistActivity((Activity) mContext);

        sdf = new SimpleDateFormat("HH:mm");

        etDiscount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(1)});
        etDiscount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etDiscount.setSelection(etDiscount.getText().toString().length());
                }
            }
        });
        etDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isNotNull(s.toString())) {
                    if (".".equals(s.toString())) {
                        etDiscount.setText("0.");
                    } else {
                        if ((s.toString().length() == 2)&&(s.toString().substring(0,1).contains("0")&&(!s.toString().equals("0.")))){
                            etDiscount.setText(s.toString().substring(1,2));
                            etDiscount.setSelection(etDiscount.getText().toString().length());
                        }else {
                            try {
                                if (Double.parseDouble(s.toString()) > 10.0) {
                                    etDiscount.setText("10");
                                    etDiscount.setSelection(etDiscount.getText().toString().length());
                                }
                            } catch (Exception e) {
                                etDiscount.setText("10");
                                etDiscount.setSelection(etDiscount.getText().toString().length());
                            }
                        }
                    }
                } else {
                    etDiscount.setText("0");
                    etDiscount.setSelection(etDiscount.getText().toString().length());
                }
            }
        });
        recyclerViewEvent.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewSeat.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewPijiu.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewYangjiu.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewHongjiu.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewJiweijiu.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewXiaochi.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewOther.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewTaocan.setLayoutManager(new LinearLayoutManager(mContext));
        activityAdapter = new ManageShopActivityAdapter(activityDatas);
        activityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EditShopEventActivity.start(mContext, 1, userShopDetailBean.getStoreId(), activityDatas.get(position));
            }
        });
        seatAdapter = new ManageShopSeatAdapter(seatDatas);
        seatAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EditSeatTypeActivity.start(mContext, 1, userShopDetailBean.getStoreId(), seatDatas.get(position));
            }
        });
        pijiuAdapter = new ManageShopSpendTypeAdapter(pijiuDatas, mContext);
        pijiuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EditDanDianActivity.start(mContext, 1, 1, userShopDetailBean.getStoreId(), "啤酒", pijiuDatas.get(position));
            }
        });
        yangjiuAdapter = new ManageShopSpendTypeAdapter(yangjiuDatas, mContext);
        yangjiuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EditDanDianActivity.start(mContext, 1, 2, userShopDetailBean.getStoreId(), "洋酒", yangjiuDatas.get(position));
            }
        });
        hongjiuAdapter = new ManageShopSpendTypeAdapter(hongjiuDatas, mContext);
        hongjiuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EditDanDianActivity.start(mContext, 1, 3, userShopDetailBean.getStoreId(), "红酒", hongjiuDatas.get(position));
            }
        });
        jiweijiuAdapter = new ManageShopSpendTypeAdapter(jiweijiuDatas, mContext);
        jiweijiuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EditDanDianActivity.start(mContext, 1, 4, userShopDetailBean.getStoreId(), "鸡尾酒", jiweijiuDatas.get(position));
            }
        });
        xiaochiAdapter = new ManageShopSpendTypeAdapter(xiaochiDatas, mContext);
        xiaochiAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EditDanDianActivity.start(mContext, 1, 5, userShopDetailBean.getStoreId(), "小吃", xiaochiDatas.get(position));
            }
        });
        qitaAdapter = new ManageShopSpendTypeAdapter(qitaDatas, mContext);
        qitaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EditDanDianActivity.start(mContext, 1, 6, userShopDetailBean.getStoreId(), "其他", qitaDatas.get(position));
            }
        });
        taocanAdapter = new ManageShopSpendTypeAdapter(taocanDatas, mContext);
        taocanAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EditDanDianActivity.start(mContext, 1, 7, userShopDetailBean.getStoreId(), "套餐", taocanDatas.get(position));
            }
        });
        recyclerViewEvent.setAdapter(activityAdapter);
        recyclerViewSeat.setAdapter(seatAdapter);
        recyclerViewPijiu.setAdapter(pijiuAdapter);
        recyclerViewYangjiu.setAdapter(yangjiuAdapter);
        recyclerViewHongjiu.setAdapter(hongjiuAdapter);
        recyclerViewJiweijiu.setAdapter(jiweijiuAdapter);
        recyclerViewXiaochi.setAdapter(xiaochiAdapter);
        recyclerViewOther.setAdapter(qitaAdapter);
        recyclerViewTaocan.setAdapter(taocanAdapter);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_seat_head, null);
        seatAdapter.addHeaderView(view);
        showWaitDialog(ManageShopActivity.this);
        initData();
    }

    private void getShopInfo() {
        MQApi.getShopsList(userBean.getUId(), userBean.getStoreId(), 1, 0, 0, 10, 0,
                RxSPTool.getContent(ManageShopActivity.this, Constant.KEY_LONGITUDE),
                RxSPTool.getContent(ManageShopActivity.this, Constant.KEY_LATITUDE), new SimpleCallBack<String>() {
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
                                ShopDetailActivity.start(mContext,shopInfoBean.getStoreId(),shopInfoBean.getStoreName(),
                                        shopInfoBean.getKind(),shopInfoBean.getStoreCoupon(),shopInfoBean.getIsCoupon(),
                                        shopInfoBean.getCompanyName());
                            }else {
                                RxToast.showToast("数据异常");
                            }
                        } else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }

    @Override
    public void initData() {  //0为全部更新 1为更新recycler
        showWaitDialog(ManageShopActivity.this);
        MQApi.getShopDetailInfo(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<UserShopDetailBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<UserShopDetailBean>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<UserShopDetailBean>>() {});
                if (myApiResult.isOk()) {
                    userShopDetailBean = myApiResult.getData();
                    for (int i = 0; i < myApiResult.getData().getStoreDiscountsType().size(); i++) {
                        activityDatas.add(myApiResult.getData().getStoreDiscountsType().get(i));
                    }
                    activityAdapter.notifyDataSetChanged();

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

                    etDiscount.setText(myApiResult.getData().getDiscount());
                    etSpecial.setText(myApiResult.getData().getFeature());
                    tvTime.setText(myApiResult.getData().getBusinessTime());
                    switchBusiness.setChecked("1".equals(myApiResult.getData().getIsOnline()));


                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    public void initRecyclerData() {  //0为全部更新 1为更新recycler
        showWaitDialog(ManageShopActivity.this);
        MQApi.getShopDetailInfo(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<UserShopDetailBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<UserShopDetailBean>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<UserShopDetailBean>>() {});
                if (myApiResult.isOk()) {
                    activityDatas.clear();
                    userShopDetailBean = myApiResult.getData();
                    for (int i = 0; i < myApiResult.getData().getStoreDiscountsType().size(); i++) {
                        activityDatas.add(myApiResult.getData().getStoreDiscountsType().get(i));
                    }
                    activityAdapter.notifyDataSetChanged();

                    seatDatas.clear();
                    for (int i = 0; i < myApiResult.getData().getStoreTableType().size(); i++) {
                        seatDatas.add(myApiResult.getData().getStoreTableType().get(i));
                    }
                    seatAdapter.notifyDataSetChanged();

                    pijiuDatas.clear();
                    for (int i = 0; i < myApiResult.getData().getBeerList().size(); i++) {
                        pijiuDatas.add(myApiResult.getData().getBeerList().get(i));
                    }
                    pijiuAdapter.notifyDataSetChanged();

                    yangjiuDatas.clear();
                    for (int i = 0; i < myApiResult.getData().getWineList().size(); i++) {
                        yangjiuDatas.add(myApiResult.getData().getWineList().get(i));
                    }
                    yangjiuAdapter.notifyDataSetChanged();

                    hongjiuDatas.clear();
                    for (int i = 0; i < myApiResult.getData().getRedWineList().size(); i++) {
                        hongjiuDatas.add(myApiResult.getData().getRedWineList().get(i));
                    }
                    hongjiuAdapter.notifyDataSetChanged();

                    jiweijiuDatas.clear();
                    for (int i = 0; i < myApiResult.getData().getCocktailList().size(); i++) {
                        jiweijiuDatas.add(myApiResult.getData().getCocktailList().get(i));
                    }
                    jiweijiuAdapter.notifyDataSetChanged();

                    xiaochiDatas.clear();
                    for (int i = 0; i < myApiResult.getData().getSnackList().size(); i++) {
                        xiaochiDatas.add(myApiResult.getData().getSnackList().get(i));
                    }
                    xiaochiAdapter.notifyDataSetChanged();

                    qitaDatas.clear();
                    for (int i = 0; i < myApiResult.getData().getDrinksList().size(); i++) {
                        qitaDatas.add(myApiResult.getData().getDrinksList().get(i));
                    }
                    qitaAdapter.notifyDataSetChanged();

                    taocanDatas.clear();
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

    public static void start(Context context) {
        Intent starter = new Intent(context, ManageShopActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /*EditDanDianActivity.start(mContext);*/
    @OnClick({R.id.iv_add_event, R.id.iv_add_seat, R.id.tv_edit_time, R.id.tv_submit, R.id.iv_add_pijiu, R.id.iv_add_yangjiu, R.id.iv_add_hongjiu, R.id.iv_add_jiweijiu, R.id.iv_add_xiaochi, R.id.iv_add_other, R.id.iv_add_taocan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_event:
                //添加活动
                EditShopEventActivity.start(mContext, 0, userShopDetailBean.getStoreId(), null);
                break;
            case R.id.iv_add_seat:
                //订台类型
                EditSeatTypeActivity.start(mContext, 0, userShopDetailBean.getStoreId(), null);
                break;
            case R.id.tv_edit_time:
                //营业时间
                showSelectTime(0);
                break;
            case R.id.tv_submit:
                //保存
                submit();
                break;
            case R.id.iv_add_pijiu:
                EditDanDianActivity.start(mContext, 0, 1, userShopDetailBean.getStoreId(), "啤酒", null);
                break;
            case R.id.iv_add_yangjiu:
                EditDanDianActivity.start(mContext, 0, 2, userShopDetailBean.getStoreId(), "洋酒", null);
                break;
            case R.id.iv_add_hongjiu:
                EditDanDianActivity.start(mContext, 0, 3, userShopDetailBean.getStoreId(), "红酒", null);
                break;
            case R.id.iv_add_jiweijiu:
                EditDanDianActivity.start(mContext, 0, 4, userShopDetailBean.getStoreId(), "鸡尾酒", null);
                break;
            case R.id.iv_add_xiaochi:
                EditDanDianActivity.start(mContext, 0, 5, userShopDetailBean.getStoreId(), "小吃", null);
                break;
            case R.id.iv_add_other:
                EditDanDianActivity.start(mContext, 0, 6, userShopDetailBean.getStoreId(), "其他", null);
                break;
            case R.id.iv_add_taocan:
                EditDanDianActivity.start(mContext, 0, 7, userShopDetailBean.getStoreId(), "套餐", null);
                break;
        }
    }

    private void showSelectTime(int timeType) {
        // 默认全部显示
//默认设置为年月日时分秒
        pvTime = new TimePickerBuilder(ManageShopActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 0){
                    startTime = sdf.format(date);
                    pvTime.dismiss();
                    showSelectTime(1);
                }else {
                    endTime = sdf.format(date);
                    tvTime.setText(startTime + "-" + endTime);
                    pvTime.dismiss();
                }
            }
        }).setType(new boolean[]{false, false, false, true, true, false})// 默认全部显示
                .setTitleText(timeType == 0?"请选择开始时间":"请选择结束时间")
                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .build();
        pvTime.show();
    }

    private void submit() {
        if (!StringUtil.isNotNull(etDiscount.getText().toString())){
            RxToast.showToast("请填写酒水折扣");
            return;
        }else {
            if (Double.parseDouble(etDiscount.getText().toString()) == 0){
                RxToast.showToast("请填写正确的酒水折扣");
                return;
            }
        }
        if (!StringUtil.isNotNull(tvTime.getText().toString())){
            RxToast.showToast("请选择营业时间");
            return;
        }
        showWaitDialog(ManageShopActivity.this);
        MQApi.updateShopInfo(userBean.getUId(), userShopDetailBean.getStoreId(), etDiscount.getText().toString(),
                switchBusiness.isChecked() ? "1" : "2", tvTime.getText().toString(), etSpecial.getText().toString(), new SimpleCallBack<String>() {
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
//                                JSON.parseObject(s, new MyApiResult<UserBean>().getClass());
                        if (myApiResult.isOk()) {
                            RxToast.showToast("网店信息更新成功");
                        } else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }

}

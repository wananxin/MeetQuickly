package com.andy.meetquickly.fragment.updateUserInfo;


import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.ShopDetailActivity;
import com.andy.meetquickly.activity.user.UpdataUserAllInfoActivity;
import com.andy.meetquickly.adapter.OftenPlaceAdapter;
import com.andy.meetquickly.adapter.SignTypeAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.AddTagBean;
import com.andy.meetquickly.bean.CardBean;
import com.andy.meetquickly.bean.TagTypeBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserDataInfoBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.StringUtil;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.v2.CustomDialog;
import com.kongzue.dialog.v2.InputDialog;
import com.kongzue.dialog.v2.SelectDialog;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateBaseInfoFragment extends BaseLazyFragment {


    @BindView(R.id.textView_name)
    TextView textViewName;
    @BindView(R.id.textView_birthday)
    TextView textViewBirthday;
    @BindView(R.id.textView_height)
    TextView textViewHeight;
    @BindView(R.id.textView_weight)
    TextView textViewWeight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.textViewSign)
    TextView textViewSign;
    @BindView(R.id.textViewSport)
    TextView textViewSport;
    @BindView(R.id.textViewMusic)
    TextView textViewMusic;
    @BindView(R.id.textViewFood)
    TextView textViewFood;
    @BindView(R.id.textViewMovie)
    TextView textViewMovie;
    @BindView(R.id.textViewBook)
    TextView textViewBook;
    @BindView(R.id.textViewVacation)
    TextView textViewVacation;
    @BindView(R.id.textViewHangye)
    TextView textViewHangye;
    @BindView(R.id.textViewWork)
    TextView textViewWork;
    @BindView(R.id.textViewCompany)
    TextView textViewCompany;
    @BindView(R.id.textViewHome)
    TextView textViewHome;
    Unbinder unbinder;

    private UserBean userBean;
    private OftenPlaceAdapter adapter;
    private List<UserDataInfoBean.ManagerStoreBean> mData = new ArrayList<>();
    private List<TagTypeBean> signDatas = new ArrayList<>();
    private CustomDialog customDialogSign;
    private String textSign = "";
    private String textSport = "";
    private String textMusic = "";
    private String textFood = "";
    private String textMovie = "";
    private String textBook = "";
    private String textVacation = "";
    private String textHangye = "";
    private String textWork = "";

    private String textCompany = "";
    private String textHome = "";
    private String userName;
    private String birthday;
    private String height;
    private String weight;
    private String mProvince = "";
    private String mCity = "";
    private String mDistrict = "";

    public UpdateBaseInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void fetchData() {
        initData(0);
    }

    private void initData(int type) {
        //type  0 是常规拉取   1 是只要拉取我的网店
        MQApi.getUserDataInfo(userBean.getUId(), "", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<UserDataInfoBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<UserDataInfoBean>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<UserDataInfoBean>>(){});
                if (myApiResult.isOk()) {
                    if (type == 0) {
                        textViewName.setText(myApiResult.getData().getUserBaseInfo().get(0).getNickName());
                        userName = myApiResult.getData().getUserBaseInfo().get(0).getNickName();
                        textViewBirthday.setText(myApiResult.getData().getUserBaseInfo().get(0).getBirthday());
                        birthday = myApiResult.getData().getUserBaseInfo().get(0).getBirthday();
                        textViewHeight.setText(myApiResult.getData().getUserBaseInfo().get(0).getHeight() + "cm");
                        height = myApiResult.getData().getUserBaseInfo().get(0).getHeight();
                        textViewWeight.setText(myApiResult.getData().getUserBaseInfo().get(0).getWeight() + "kg");
                        weight = myApiResult.getData().getUserBaseInfo().get(0).getWeight();

                        //个性标签
                        if (myApiResult.getData().getTags().size() == 0) {
                            textViewSign.setText("+ 我的个性标签");
                        } else {
                            textSign = myApiResult.getData().getTags().get(0).getContent();
                            textViewSign.setText(myApiResult.getData().getTags().get(0).getContent());
                        }

                        //运动
                        if (myApiResult.getData().getSports().size() == 0) {
                            textViewSport.setText("+ 我喜欢的运动");
                        } else {
                            textSport = myApiResult.getData().getSports().get(0).getContent();
                            textViewSport.setText(myApiResult.getData().getSports().get(0).getContent());
                        }

                        //音乐
                        if (myApiResult.getData().getMusics().size() == 0) {
                            textViewMusic.setText("+ 我喜欢的音乐");
                        } else {
                            textMusic = myApiResult.getData().getMusics().get(0).getContent();
                            textViewMusic.setText(myApiResult.getData().getMusics().get(0).getContent());
                        }

                        //食物
                        if (myApiResult.getData().getFoods().size() == 0) {
                            textViewFood.setText("+ 我喜欢的食物");
                        } else {
                            textFood = myApiResult.getData().getFoods().get(0).getContent();
                            textViewFood.setText(myApiResult.getData().getFoods().get(0).getContent());
                        }

                        //电影
                        if (myApiResult.getData().getFilms().size() == 0) {
                            textViewMovie.setText("+ 我喜欢的电影");
                        } else {
                            textMovie = myApiResult.getData().getFilms().get(0).getContent();
                            textViewMovie.setText(myApiResult.getData().getFilms().get(0).getContent());
                        }

                        //书籍
                        if (myApiResult.getData().getBooks().size() == 0) {
                            textViewBook.setText("+ 我喜欢的书籍");
                        } else {
                            textBook = myApiResult.getData().getBooks().get(0).getContent();
                            textViewBook.setText(myApiResult.getData().getBooks().get(0).getContent());
                        }

                        //旅行
                        if (myApiResult.getData().getVacation().size() == 0) {
                            textViewVacation.setText("+ 我的旅行足迹");
                        } else {
                            textVacation = myApiResult.getData().getVacation().get(0).getContent();
                            textViewVacation.setText(myApiResult.getData().getVacation().get(0).getContent());
                        }

                        //行业信息
                        if (myApiResult.getData().getIndustry().size() == 0) {
                            textViewHangye.setText("+ 添加行业信息");
                        } else {
                            textHangye = myApiResult.getData().getIndustry().get(0).getContent();
                            textViewHangye.setText(myApiResult.getData().getIndustry().get(0).getContent());
                        }

                        //工作领域
                        if (myApiResult.getData().getWorks().size() == 0) {
                            textViewWork.setText("+ 添加工作领域信息");
                        } else {
                            textWork = myApiResult.getData().getWorks().get(0).getContent();
                            textViewWork.setText(myApiResult.getData().getWorks().get(0).getContent());
                        }

                        //公司信息
                        if (StringUtil.isNotNull(myApiResult.getData().getUserBaseInfo().get(0).getCompany())) {
                            textCompany = myApiResult.getData().getUserBaseInfo().get(0).getCompany();
                            textViewCompany.setText(myApiResult.getData().getUserBaseInfo().get(0).getCompany());
                        } else {
                            textViewCompany.setText("+ 添加公司信息");
                        }

                        //家乡信息
                        if (StringUtil.isNotNull(myApiResult.getData().getUserBaseInfo().get(0).getHomeCity())) {
                            textViewHome.setText(myApiResult.getData().getUserBaseInfo().get(0).getHomeProvince() + "-" +
                                    myApiResult.getData().getUserBaseInfo().get(0).getHomeCity() + "-" +
                                    myApiResult.getData().getUserBaseInfo().get(0).getHomeDistrict());
                            mProvince = myApiResult.getData().getUserBaseInfo().get(0).getHomeProvince();
                            mCity = myApiResult.getData().getUserBaseInfo().get(0).getHomeCity();
                            mDistrict = myApiResult.getData().getUserBaseInfo().get(0).getHomeDistrict();
                            textHome = myApiResult.getData().getUserBaseInfo().get(0).getHomeProvince() + "-" +
                                    myApiResult.getData().getUserBaseInfo().get(0).getHomeCity() + "-" +
                                    myApiResult.getData().getUserBaseInfo().get(0).getHomeDistrict();
                        } else {
                            textViewHome.setText("+ 添加你的家乡信息");
                        }
                    }
                    //常去场所
                    mData.clear();
                    for (int i = 0; i < myApiResult.getData().getManagerStore().size(); i++) {
                        mData.add(myApiResult.getData().getManagerStore().get(i));
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_base_info, container, false);
        view.setTag(0);
        unbinder = ButterKnife.bind(this, view);
        userBean = UserCache.getInstance().getUser();
        initView();
        return view;
    }

    private ArrayList<CardBean> cardItem = new ArrayList<>();
    private ArrayList<CardBean> cardItem1 = new ArrayList<>();
    private void initView() {
        mPicker.init(UpdateBaseInfoFragment.this.getContext());
        for (int i = 145; i < 220; i++) {
            cardItem.add(new CardBean(i, i + ""));
        }
        for (int i = 40; i < 100; i++) {
            cardItem1.add(new CardBean(i, i + ""));
        }
        initCustomOptionPicker();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LinearLayoutManager layoutManager = new LinearLayoutManager(UpdateBaseInfoFragment.this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OftenPlaceAdapter(mData,UpdateBaseInfoFragment.this.getContext(),2);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_img){
                    ShopDetailActivity.start(UpdateBaseInfoFragment.this.getContext(),mData.get(position).getStoreId(),
                            mData.get(position).getStoreName(),mData.get(position).getKind(),null,"1",
                            mData.get(position).getCompanyName());
                }else if (view.getId() == R.id.iv_delete) {
                    showDelPlaceDialog(position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    //显示确定删除常去场所窗口
    private void showDelPlaceDialog(int position) {
        SelectDialog.show(UpdateBaseInfoFragment.this.getContext(), "提示", "是否确定解除常去场所信息", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delOftenPlace(position);
            }
        }).setCanCancel(true);
    }

    //删除常去场所
    private void delOftenPlace(int position) {
        showWaitDialog(UpdateBaseInfoFragment.this.getActivity());
        MQApi.updateMyUsers(mData.get(position).getId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());
//                        JSON.parseObject(s,new MyApiResult<String>().getClass());
                if (myApiResult.isOk()){
                    RxToast.showToast("解绑成功");
                    initData(1);
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.textView_name,R.id.ll_save, R.id.textView_birthday, R.id.textView_height, R.id.textView_weight, R.id.relativeLayout, R.id.textViewSign, R.id.textViewSport, R.id.textViewMusic, R.id.textViewFood, R.id.textViewMovie, R.id.textViewBook, R.id.textViewVacation, R.id.textViewHangye, R.id.textViewWork, R.id.textViewCompany, R.id.textViewHome})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView_name:
                showSetNameDialog();
                break;
            case R.id.textView_birthday:
                showSelectBirthdayDialog();
                break;
            case R.id.textView_height:
                pvCustomOptionsHeight.show();
                break;
            case R.id.textView_weight:
                pvCustomOptionsWeith.show();
                break;
            case R.id.relativeLayout:
                showAddPlaceDialog();
                break;
            case R.id.textViewSign:
                showInfoType(9,textViewSign ,textSign);
                // 1行业  2工作领域 3运动 4音乐 5食物 6电影 7书和动漫 8旅游足迹 9我的标签
                break;
            case R.id.textViewSport:
                showInfoType(3,textViewSport ,textSport);
                break;
            case R.id.textViewMusic:
                showInfoType(4,textViewMusic , textMusic);
                break;
            case R.id.textViewFood:
                showInfoType(5,textViewFood , textFood);
                break;
            case R.id.textViewMovie:
                showInfoType(6,textViewMovie , textMovie);
                break;
            case R.id.textViewBook:
                showInfoType(7,textViewBook , textBook);
                break;
            case R.id.textViewVacation:
                showInfoType(8,textViewVacation , textVacation);
                break;
            case R.id.textViewHangye:
                showInfoType(1,textViewHangye , textHangye);
                break;
            case R.id.textViewWork:
                showInfoType(2,textViewWork , textWork);
                break;
            case R.id.textViewCompany:
                showCompanyNameDialog();
                break;
            case R.id.textViewHome:
                showHomeSelectDialog();
                break;
            case R.id.ll_save:
                saveUserInfo();
                break;
        }
    }

    private void saveUserInfo() {
        showWaitDialog(UpdateBaseInfoFragment.this.getActivity());
        MQApi.updateUserInfo(userBean.getUId(), userName, birthday, height, weight, textCompany, mProvince, mCity, mDistrict,
                textSign, textSport, textMusic, textFood, textMovie, textBook, textVacation, textHangye, textWork, new SimpleCallBack<String>() {
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
                            RxToast.showToast("保存成功");
                        }else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }

    CityPickerView mPicker=new CityPickerView();

    private void showHomeSelectDialog() {
//添加默认的配置，不需要自己定义，当然也可以自定义相关熟悉，详细属性请看demo
        CityConfig cityConfig = new CityConfig.Builder()
                .provinceCyclic(false)//省份滚轮是否可以循环滚动
                .cityCyclic(false)//城市滚轮是否可以循环滚动
                .districtCyclic(false)//区县滚轮是否循环滚动
                .build();
        mPicker.setConfig(cityConfig);
//监听选择点击事件及返回结果
        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                //省份province
                //城市city
                //地区district
                mProvince = province.getName();
                mCity = city.getName();
                mDistrict = district.getName();
                textViewHome.setText(mProvince + "-" + mCity + "-" + mDistrict);
            }

            @Override
            public void onCancel() {

            }
        });
        //显示
        mPicker.showCityPicker( );
    }

    private void showInfoType(int type,TextView mTextView ,String mInfo) {
        showWaitDialog(UpdateBaseInfoFragment.this.getActivity());
        MQApi.getTagsList(userBean.getUId(),type, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<TagTypeBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<TagTypeBean>>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<TagTypeBean>>>(){});
                if (myApiResult.isOk()) {
                    signDatas.clear();
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        if (mInfo.contains(myApiResult.getData().get(i).getName())){
                            myApiResult.getData().get(i).setSelect(true);
                        }
                        signDatas.add(myApiResult.getData().get(i));
                    }
                    customDialogSign = CustomDialog.show(UpdateBaseInfoFragment.this.getContext(), R.layout.dialog_user_info, new CustomDialog.BindView() {
                        @Override
                        public void onBind(CustomDialog dialog, View rootView) {
                            TextView textViewTitle = rootView.findViewById(R.id.textViewTitle);
                            TextView textViewNew = rootView.findViewById(R.id.textViewShopName);
                            TextView textViewCancel = rootView.findViewById(R.id.textViewCancel);
                            TextView textViewSure = rootView.findViewById(R.id.textViewSure);
                            RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(UpdateBaseInfoFragment.this.getContext()));
                            SignTypeAdapter adapter = new SignTypeAdapter(signDatas);
                            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    signDatas.get(position).setSelect(!signDatas.get(position).isSelect());
                                    adapter.notifyItemChanged(position);
                                }
                            });
                            recyclerView.setAdapter(adapter);
                            textViewNew.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialogSign.doDismiss();
                                    showCreatSignDialog(type,mTextView);
                                }
                            });
                            textViewCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialogSign.doDismiss();
                                }
                            });
                            textViewSure.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialogSign.doDismiss();
                                    String sign = "";
                                    for (int i = 0; i < signDatas.size(); i++) {
                                        if (signDatas.get(i).isSelect()){
                                            sign = sign + signDatas.get(i).getName() + ",";
                                        }
                                    }
                                    if (StringUtil.isNotNull(sign)) {
                                        mTextView.setText(sign.substring(0, sign.length() - 1));
                                        switch (type){
                                            case 1:
                                                textHangye = sign.substring(0, sign.length() - 1);
                                                break;
                                            case 2:
                                                textWork = sign.substring(0, sign.length() - 1);
                                                break;
                                            case 3:
                                                textSport = sign.substring(0, sign.length() - 1);
                                                break;
                                            case 4:
                                                textMusic = sign.substring(0, sign.length() - 1);
                                                break;
                                            case 5:
                                                textFood = sign.substring(0, sign.length() - 1);
                                                break;
                                            case 6:
                                                textMovie = sign.substring(0, sign.length() - 1);
                                                break;
                                            case 7:
                                                textBook = sign.substring(0, sign.length() - 1);
                                                break;
                                            case 8:
                                                textVacation = sign.substring(0, sign.length() - 1);
                                                break;
                                            case 9:
                                                textSign = sign.substring(0, sign.length() - 1);
                                                break;
                                        }
                                    }else {
                                        switch (type){
                                            case 1:
                                                textHangye = "";
                                                mTextView.setText("+ 添加行业信息");
                                                break;
                                            case 2:
                                                textWork = "";
                                                mTextView.setText("+ 添加工作领域信息");
                                                break;
                                            case 3:
                                                textSport = "";
                                                mTextView.setText("+ 我喜欢的运动");
                                                break;
                                            case 4:
                                                textMusic = "";
                                                mTextView.setText("+ 我喜欢的音乐");
                                                break;
                                            case 5:
                                                textFood = "";
                                                mTextView.setText("+ 我喜欢的食物");
                                                break;
                                            case 6:
                                                textMovie = "";
                                                mTextView.setText("+ 我喜欢的电影");
                                                break;
                                            case 7:
                                                textBook = "";
                                                mTextView.setText("+ 我喜欢的书籍");
                                                break;
                                            case 8:
                                                textVacation = "";
                                                mTextView.setText("+ 我的旅行足迹");
                                                break;
                                            case 9:
                                                textSign = "";
                                                mTextView.setText("+ 我的个性标签");
                                                break;
                                        }
                                    }
                                }
                            });
                        }
                    }).setCanCancel(true);
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }

            }
        });

    }

    private void showCreatSignDialog(int type,TextView textView) {
        customDialogSign = CustomDialog.show(UpdateBaseInfoFragment.this.getContext(), R.layout.dialog_add_sign, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
                EditText editText = rootView.findViewById(R.id.editText);
                TextView textViewCancel = rootView.findViewById(R.id.textViewCancel);
                TextView textViewSure = rootView.findViewById(R.id.textViewSure);
                textViewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialogSign.doDismiss();
                    }
                });
                textViewSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtil.isNotNull(editText.getText().toString())) {
                            customDialogSign.doDismiss();
                            addUserTag(type,editText.getText().toString(),textView);
                        }else {
                            RxToast.showToast("请输入自定义标签");
                        }
                    }
                });
            }
        }).setCanCancel(true);
    }

    private void addUserTag(int type,String tag,TextView textView) {
        showWaitDialog(UpdateBaseInfoFragment.this.getActivity());
        MQApi.addUserTags(userBean.getUId(), tag, type, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<AddTagBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<AddTagBean>>(){}.getType());
//                        JSON.parseObject(s,new MyApiResult<String>().getClass());
                if (myApiResult.isOk()){
                    switch (type){
                        case 1:
                            if (StringUtil.isNotNull(textHangye)){
                                textHangye = textHangye + "," + myApiResult.getData().getContent();
                            }else {
                                textHangye = myApiResult.getData().getContent();
                            }
                            textView.setText(textHangye);
                            break;
                        case 2:
                            if (StringUtil.isNotNull(textWork)){
                                textWork = textWork + "," + myApiResult.getData().getContent();
                            }else {
                                textWork = myApiResult.getData().getContent();
                            }
                            textView.setText(textWork);
                            break;
                        case 3:
                            if (StringUtil.isNotNull(textSport)){
                                textSport = textSport + "," + myApiResult.getData().getContent();
                            }else {
                                textSport = myApiResult.getData().getContent();
                            }
                            textView.setText(textSport);
                            break;
                        case 4:
                            if (StringUtil.isNotNull(textMusic)){
                                textMusic = textMusic + "," + myApiResult.getData().getContent();
                            }else {
                                textMusic = myApiResult.getData().getContent();
                            }
                            textView.setText(textMusic);
                            break;
                        case 5:
                            if (StringUtil.isNotNull(textFood)){
                                textFood = textFood + "," + myApiResult.getData().getContent();
                            }else {
                                textFood = myApiResult.getData().getContent();
                            }
                            textView.setText(textFood);
                            break;
                        case 6:
                            if (StringUtil.isNotNull(textMovie)){
                                textMovie = textMovie + "," + myApiResult.getData().getContent();
                            }else {
                                textMovie = myApiResult.getData().getContent();
                            }
                            textView.setText(textMovie);
                            break;
                        case 7:
                            if (StringUtil.isNotNull(textBook)){
                                textBook = textBook + "," + myApiResult.getData().getContent();
                            }else {
                                textBook = myApiResult.getData().getContent();
                            }
                            textView.setText(textBook);
                            break;
                        case 8:
                            if (StringUtil.isNotNull(textVacation)){
                                textVacation = textVacation + "," + myApiResult.getData().getContent();
                            }else {
                                textVacation = myApiResult.getData().getContent();
                            }
                            textView.setText(textVacation);
                            break;
                        case 9:
                            if (StringUtil.isNotNull(textSign)){
                                textSign = textSign + "," + myApiResult.getData().getContent();
                            }else {
                                textSign = myApiResult.getData().getContent();
                            }
                            textView.setText(textSign);
                            break;
                    }
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private SimpleDateFormat dateFormat;
    private void showSelectBirthdayDialog() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(selectedDate.get(Calendar.YEAR)-100,selectedDate.get(Calendar.MONTH),selectedDate.get(Calendar.DATE));
        endDate.set(selectedDate.get(Calendar.YEAR)-16,selectedDate.get(Calendar.MONTH),selectedDate.get(Calendar.DATE));

        TimePickerView pvStartTime = new TimePickerBuilder(UpdateBaseInfoFragment.this.getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                textViewBirthday.setText(dateFormat.format(date));
                birthday = dateFormat.format(date);
            }
        }).setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setRangDate(startDate,endDate)
                .setDate(endDate)
                .build();

        pvStartTime.show();
    }

    private void showSetNameDialog() {
        InputDialog.show(UpdateBaseInfoFragment.this.getContext(), "设置昵称", "设置一个好听的名字吧", "确定", new InputDialogOkButtonClickListener() {
            @Override
            public void onClick(Dialog dialog, String inputText) {
                if (StringUtil.isNotNull(inputText)) {
                    userName = inputText;
                    textViewName.setText(inputText);
                    dialog.dismiss();
                }else {
                    RxToast.showToast("请您输入昵称!");
                }
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setInputInfo(new InputInfo()
                .setMAX_LENGTH(7) );
    }

    private void showCompanyNameDialog() {
        InputDialog.show(UpdateBaseInfoFragment.this.getContext(), "设置公司名字", "输入公司名字", "确定", new InputDialogOkButtonClickListener() {
            @Override
            public void onClick(Dialog dialog, String inputText) {
                if (StringUtil.isNotNull(inputText)) {
                    textCompany = inputText;
                    textViewCompany.setText(inputText);
                    dialog.dismiss();
                }else {
                    RxToast.showToast("请您输入公司名字");
                }
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setInputInfo(new InputInfo()
                .setMAX_LENGTH(10) );
    }

    CustomDialog customDialogPlace;
    private void showAddPlaceDialog() {
        customDialogPlace = CustomDialog.show(UpdateBaseInfoFragment.this.getContext(), R.layout.dialog_manage_yqzd, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
//                        //绑定布局
//                        ImageView btnOk = rootView.findViewById(R.id.btn_ok);
                EditText et_content = rootView.findViewById(R.id.et_content);
                TextView tv_cancel = rootView.findViewById(R.id.tv_cancel);
                TextView tv_sure = rootView.findViewById(R.id.tv_sure);
                TextView tv_title = rootView.findViewById(R.id.tv_title);
                TextView tv_tips = rootView.findViewById(R.id.tv_tips);
                et_content.setHint("请输入经理人手机号");
                tv_title.setText("常去场所");
                tv_tips.setText("提示：确定后，您将在该经理人的网店“可能邂逅的人”中显示");
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialogPlace.doDismiss();
                    }
                });
                tv_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bindUser(et_content.getText().toString().trim());
                        customDialogPlace.doDismiss();
                    }
                });
            }
        }).setCanCancel(true);
    }

    private void bindUser(String trim) {
        if (!StringUtil.isNotNull(trim)){
            RxToast.showToast("经理人号码不能为空");
            return;
        }

        showWaitDialog(UpdateBaseInfoFragment.this.getActivity());
        MQApi.addBinding(userBean.getUId(), 2, trim, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());
//                        JSON.parseObject(s,new MyApiResult<String>().getClass());
                if (myApiResult.isOk()){
                    RxToast.showToast("绑定成功!");
                    initData(1);
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private OptionsPickerView pvCustomOptionsHeight, pvCustomOptionsWeith;
    private void initCustomOptionPicker() {
        pvCustomOptionsHeight = new OptionsPickerBuilder(UpdateBaseInfoFragment.this.getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                height = cardItem.get(options1).getPickerViewText();
                textViewHeight.setText(height + "cm");
            }
        }).setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                tvSubmit.setText("请选择身高");
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvCustomOptionsHeight.returnData();
                        pvCustomOptionsHeight.dismiss();
                    }
                });
            }
        }).build();
        pvCustomOptionsHeight.setPicker(cardItem);//添加数据

        pvCustomOptionsWeith = new OptionsPickerBuilder(UpdateBaseInfoFragment.this.getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                weight = cardItem1.get(options1).getPickerViewText();
                textViewWeight.setText(weight + "kg");
            }
        }).setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
//                        final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                tvSubmit.setText("请选择体重");
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvCustomOptionsWeith.returnData();
                        pvCustomOptionsWeith.dismiss();
                    }
                });
            }
        }).build();
        pvCustomOptionsWeith.setPicker(cardItem1);//添加数据
    }
}

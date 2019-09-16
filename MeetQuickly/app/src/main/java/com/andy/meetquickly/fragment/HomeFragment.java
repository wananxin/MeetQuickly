package com.andy.meetquickly.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.HomeSearchActivity;
import com.andy.meetquickly.activity.home.ShopDetailActivity;
import com.andy.meetquickly.activity.user.OrderActivity;
import com.andy.meetquickly.activity.user.PublishEventActivity;
import com.andy.meetquickly.activity.user.UserInvitationPeopleActivity;
import com.andy.meetquickly.activity.user.manager.ManageOrderActivity;
import com.andy.meetquickly.activity.user.manager.UnpaidOrderActivity;
import com.andy.meetquickly.activity.user.partner.OpenPartnerTwoActivity;
import com.andy.meetquickly.activity.user.partner.PartnerActivity;
import com.andy.meetquickly.adapter.ViewPagerAdapter;
import com.andy.meetquickly.base.BaseFragment;
import com.andy.meetquickly.bean.HomeBannerBean;
import com.andy.meetquickly.bean.HomePagerBean;
import com.andy.meetquickly.bean.MarqueeBean;
import com.andy.meetquickly.bean.ShareInfoBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserInviteTotalBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.fragment.home.FindPeoplePlayFragment;
import com.andy.meetquickly.fragment.home.WherePlayFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.homeEndMessage;
import com.andy.meetquickly.message.screenShopMessage;
import com.andy.meetquickly.utils.DaoHangUtil;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.app.hubert.guide.model.RelativeGuide;
import com.appyvet.materialrangebar.RangeBar;
import com.flyco.tablayout.SlidingScaleTabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;
import com.kongzue.dialog.v2.CustomDialog;
import com.kongzue.dialog.v2.SelectDialog;
import com.lwj.widget.viewpagerindicator.ViewPagerIndicator;
import com.sunfusheng.marqueeview.MarqueeView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/19  15:28
 * 描述：     首页
 */
public class HomeFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.tablayout)
    SlidingScaleTabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.iv_screen)
    ImageView ivScreen;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.indicator_line)
    ViewPagerIndicator indicatorLine;
    @BindView(R.id.textViewSearch)
    LinearLayout textViewSearch;
    private ViewPagerAdapter vpAdapter;
    // TODO: Rename parameter arguments, choose names that match
    private int mKind = 0;    //所属类型 0:全部，1:商务KTV，2:酒吧，3:量贩KTV，4:清吧
    private int mDiscountLeft = 0; //最小折扣
    private int mDiscountRight = 10;  //最大折扣
    private int mActivityName = 0;  //活动 0全部 1有 2无
    private String mStoreId = "";  //活动 0全部 1有 2无
    private List<HomeBannerBean> mData = new ArrayList<>();
    private List<View> views;

    private String[] titles = {
            "去哪玩", "找TA玩"
    };
    private UserBean userBean;
    private LayoutInflater inflater;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        super.initData();
        MQApi.getHomeBanner(new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(String s) {
//                MyApiResult<List<HomeBannerBean>> myApiResult = JSON.parseObject(s, new TypeReference<MyApiResult<List<HomeBannerBean>>>(){});
                MyApiResult<List<HomeBannerBean>> obj = new Gson().fromJson(s, new TypeToken<MyApiResult<List<HomeBannerBean>>>() {
                }.getType());
                if (obj.isOk()) {
                    for (int i = 0; i < obj.getData().size(); i++) {
                        mData.add(obj.getData().get(i));
                    }
                    banner.setImages(mData);
                    banner.start();
                } else {
                    RxToast.showToast(obj.getMsg());
                }
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        ImmersionBar.with(this)
                .statusBarView(R.id.view)
                .navigationBarEnable(false)
                .init();
        userBean = UserCache.getInstance().getUser();
        collapsingToolbar.post(() -> {
            int offHeight = collapsingToolbar.getHeight() - ImmersionBar.getStatusBarHeight(HomeFragment.this.getActivity());
            appBarLayout.addOnOffsetChangedListener((appBarLayout1, i) -> {
                if (Math.abs(i) >= offHeight) {
                    ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init();
                } else {
                    ImmersionBar.with(this).statusBarDarkFont(false).init();
                }
            });
        });

        List<Fragment> fragments = new ArrayList<>();
        WherePlayFragment fragment1 = WherePlayFragment.newInstance(18, 0);
        FindPeoplePlayFragment fragment2 = FindPeoplePlayFragment.newInstance(18, 1);
//        FindPeoplePlayFragment fragment2 = FindPeoplePlayFragment.newInstance(18,2);
        fragments.add(fragment1);
        fragments.add(fragment2);
        FragmentManager fm = HomeFragment.this.getActivity().getSupportFragmentManager();
        viewpager.setAdapter(new MyFragmentPagerAdapt(fm, fragments));
        viewpager.setOffscreenPageLimit(1);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    ivScreen.setVisibility(View.VISIBLE);
                } else {
                    ivScreen.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tablayout.setViewPager(viewpager);
        viewpager.setCurrentItem(0);

        initBanner();
        initMarquee();
        initViewPagerData();

        if (!"1".equals(userBean.getIsFirstLanding())){
            showNewbieGuide();
        }

    }

    private void initViewPagerData() {
        inflater = LayoutInflater.from(HomeFragment.this.getContext());
        views = new ArrayList<View>();
        MQApi.getHomePageListInfo(userBean.getUId(),
                RxSPTool.getContent(HomeFragment.this.getContext(), Constant.KEY_LONGITUDE),
                RxSPTool.getContent(HomeFragment.this.getContext(), Constant.KEY_LATITUDE),
                new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        RxToast.showToast(e.toString());
                    }

                    @Override
                    public void onSuccess(String s) {
                        MyApiResult<HomePagerBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<HomePagerBean>>() {
                        }.getType());
                        if (myApiResult.isOk()) {
                            HomePagerBean homePagerBean = myApiResult.getData();
                            initViewPagerInfo(homePagerBean);
                        } else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }

    private boolean isLoadData = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isLoadData) {
            initMarquee();
            initViewPagerData();
        }
    }

    private void initViewPagerInfo(HomePagerBean homePagerBean) {
        views.clear();
        if ((null != homePagerBean.getManagerOrdersList()) && (homePagerBean.getManagerOrdersList().size() > 0)) {
            View view1 = inflater.inflate(R.layout.view_home_manage_order, null);
            TextView textViewNumb = view1.findViewById(R.id.textViewNumb);
            textViewNumb.setText("您有" + homePagerBean.getManagerOrdersList().size() + "个新的订单正在进行");
            TextView textViewSeat = view1.findViewById(R.id.textViewSeat);
            textViewSeat.setText("包厢/台号：" + homePagerBean.getManagerOrdersList().get(0).getTables()
                    + "（到店时间：" + homePagerBean.getManagerOrdersList().get(0).getTargetDate() + " " +
                    homePagerBean.getManagerOrdersList().get(0).getReachTime() + "）");
            TextView textViewName = view1.findViewById(R.id.textViewName);
            if ("1".equals(homePagerBean.getManagerOrdersList().get(0).getSex())) {
                textViewName.setText("客户尊称：" + homePagerBean.getManagerOrdersList().get(0).getName() + "先生");
            } else {
                textViewName.setText("客户尊称：" + homePagerBean.getManagerOrdersList().get(0).getName() + "女士");
            }

            ImageView imageViewInto = view1.findViewById(R.id.imageViewInto);
            imageViewInto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ManageOrderActivity.start(HomeFragment.this.getContext());
                }
            });

            views.add(view1);
        }
        if ("2".equals(userBean.getUserRole())) {
            View view2 = inflater.inflate(R.layout.view_home_manage_no_order, null);

            ImageView imageViewIntoPublish = view2.findViewById(R.id.imageViewIntoShop1);
            imageViewIntoPublish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublishEventActivity.start(HomeFragment.this.getContext());
                }
            });

            ImageView imageViewIntoUnPay = view2.findViewById(R.id.imageViewIntoShop2);
            imageViewIntoUnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UnpaidOrderActivity.start(HomeFragment.this.getContext());
                }
            });

            TextView textViewUnPay = view2.findViewById(R.id.textViewCompanyName2);
            textViewUnPay.setText("未支付订单（" + homePagerBean.getUnpaid() + "）");

            views.add(view2);
        }
        if (null != homePagerBean.getPartnerInfo()) {
            View view3 = inflater.inflate(R.layout.view_home_extension, null);
            TextView textViewNewProfit = view3.findViewById(R.id.textViewShopName);
            TextView textViewTixian = view3.findViewById(R.id.textViewTixian);
            TextView textViewNewPeople = view3.findViewById(R.id.textViewManageName);
            TextView textViewAllPeople = view3.findViewById(R.id.textViewAllPeople);
            textViewNewProfit.setText(homePagerBean.getPartnerInfo().getAmount());
            textViewTixian.setText(homePagerBean.getPartnerInfo().getWalletBalance());
            textViewNewPeople.setText(homePagerBean.getPartnerInfo().getSumYestoday());
            textViewAllPeople.setText(homePagerBean.getPartnerInfo().getGroupCount());
            ImageView imageViewInto = view3.findViewById(R.id.imageViewInto);
            imageViewInto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("2".equals(userBean.getIsOpenPartner())) {
//                    OpenPartnerActivity.start(UserFragment.this.getActivity());
                        PartnerActivity.start(HomeFragment.this.getActivity());
                    } else {
                        OpenPartnerTwoActivity.start(HomeFragment.this.getActivity());
                    }
                }
            });
            ImageView imageViewQRCode = view3.findViewById(R.id.imageViewQRCode);
            imageViewQRCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getUserInviteTotal();
                }
            });
            views.add(view3);
        }
        if ((null != homePagerBean.getUsersOrdersList()) && (homePagerBean.getUsersOrdersList().size() > 0)) {
            View view4 = inflater.inflate(R.layout.view_home_user_order, null);
            TextView textViewNumb = view4.findViewById(R.id.textViewNumb);
            TextView textViewShopName = view4.findViewById(R.id.textViewShopName);
            TextView textViewSeatNumb = view4.findViewById(R.id.textViewSeatNumb);
            TextView textViewManageName = view4.findViewById(R.id.textViewManageName);
            TextView textViewManagePhone = view4.findViewById(R.id.textViewManagePhone);
            ImageView imageViewQRCode = view4.findViewById(R.id.imageViewQRCode);
            ImageView imageViewCallPhone = view4.findViewById(R.id.imageViewCallPhone);
            ImageView imageViewInto = view4.findViewById(R.id.imageViewInto);
            textViewNumb.setText("您有" + homePagerBean.getUsersOrdersList().size() + "个新的订单正在进行");
            textViewShopName.setText(homePagerBean.getUsersOrdersList().get(0).getCompanyName());
            textViewSeatNumb.setText("（包厢台号：" + homePagerBean.getUsersOrdersList().get(0).getTables() + ")");
            textViewManageName.setText(homePagerBean.getUsersOrdersList().get(0).getStoreName() + "经理");
            textViewManagePhone.setText("(" + homePagerBean.getUsersOrdersList().get(0).getStoreMobile() + ")");
            imageViewInto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderActivity.start(HomeFragment.this.getContext());
                }
            });
            imageViewQRCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //导航
                    if (StringUtil.isNotNull(RxSPTool.getContent(HomeFragment.this.getContext(), Constant.KEY_LATITUDE))) {
                        showDaoHangDialog(homePagerBean.getUsersOrdersList().get(0).getLatitude(),
                                homePagerBean.getUsersOrdersList().get(0).getLongitude(),
                                homePagerBean.getUsersOrdersList().get(0).getCompanyName());
                    } else {
                        RxToast.showToast("您未打开定位权限");
                    }
                }
            });
            imageViewCallPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打电话
                    showCallPhoneDialog(homePagerBean.getUsersOrdersList().get(0).getStoreMobile());
                }
            });
            views.add(view4);
        }
        if ((null != homePagerBean.getManagerStore()) && (homePagerBean.getManagerStore().size() > 1)) {
            //获取View对象
            View view5 = inflater.inflate(R.layout.view_home_user_no_order, null);
            TextView textViewCompanyName1 = view5.findViewById(R.id.textViewCompanyName1);
            TextView textViewCompanyName2 = view5.findViewById(R.id.textViewCompanyName2);
            TextView textViewStoreName1 = view5.findViewById(R.id.textViewStoreName1);
            TextView textViewStoreName2 = view5.findViewById(R.id.textViewStoreName2);
            TextView textViewDistance1 = view5.findViewById(R.id.textViewDistance1);
            TextView textViewDistance2 = view5.findViewById(R.id.textViewDistance2);
            textViewCompanyName1.setText(homePagerBean.getManagerStore().get(0).getCompanyName());
            textViewCompanyName2.setText(homePagerBean.getManagerStore().get(1).getCompanyName());
            textViewStoreName1.setText("(" + homePagerBean.getManagerStore().get(0).getStoreName() + "经理)");
            textViewStoreName2.setText("(" + homePagerBean.getManagerStore().get(1).getStoreName() + "经理)");
            textViewDistance1.setText(homePagerBean.getManagerStore().get(0).getDistance() + "km");
            textViewDistance2.setText(homePagerBean.getManagerStore().get(1).getDistance() + "km");

            ImageView imageViewIntoShop1 = view5.findViewById(R.id.imageViewIntoShop1);
            imageViewIntoShop1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShopDetailActivity.start(HomeFragment.this.getContext(),
                            homePagerBean.getManagerStore().get(0).getStoreId(),
                            homePagerBean.getManagerStore().get(0).getStoreName(),
                            homePagerBean.getManagerStore().get(0).getKind(),
                            homePagerBean.getManagerStore().get(0).getStoreCoupon(),
                            homePagerBean.getManagerStore().get(0).getIsCoupon(),
                            homePagerBean.getManagerStore().get(0).getCompanyName());
                }
            });
            ImageView imageViewIntoShop2 = view5.findViewById(R.id.imageViewIntoShop2);
            imageViewIntoShop2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShopDetailActivity.start(HomeFragment.this.getContext(),
                            homePagerBean.getManagerStore().get(1).getStoreId(),
                            homePagerBean.getManagerStore().get(1).getStoreName(),
                            homePagerBean.getManagerStore().get(1).getKind(),
                            homePagerBean.getManagerStore().get(1).getStoreCoupon(),
                            homePagerBean.getManagerStore().get(1).getIsCoupon(),
                            homePagerBean.getManagerStore().get(1).getCompanyName()
                    );
                }
            });

            //view对象放在List<view>中
            views.add(view5);
        }
        //List<view>放在适配器ViewPagerAdapter中
        vpAdapter = new ViewPagerAdapter(views, HomeFragment.this.getContext());
        //获取ViewPage,设置适配器;
        viewPager.setAdapter(vpAdapter);
        indicatorLine.setViewPager(viewPager);
    }

    private void showDaoHangDialog(String latitude, String longitude, String companyName) {
        SelectDialog.show(HomeFragment.this.getContext(), "导航", "是否导航到：" + companyName, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (StringUtil.isNotNull(RxSPTool.getString(HomeFragment.this.getContext(), Constant.KEY_LATITUDE))) {
                    DaoHangUtil.getApi(HomeFragment.this.getContext(), latitude,
                            longitude,
                            companyName,
                            RxSPTool.getString(HomeFragment.this.getContext(), Constant.KEY_LATITUDE),
                            RxSPTool.getString(HomeFragment.this.getContext(), Constant.KEY_LONGITUDE));
                } else {
                    RxToast.showToast("请打开应用定位权限并重启应用");
                }
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    private void showCallPhoneDialog(String storeMobile) {
        SelectDialog.show(HomeFragment.this.getContext(), "拨打电话", "是否拨打经理人电话：" + storeMobile, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RxDeviceTool.dial(HomeFragment.this.getContext(), storeMobile);
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    private void initBanner() {
        //初始化Banner设置
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(mData);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置轮播时间
        banner.setDelayTime(5000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @OnClick({R.id.textViewSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewSearch:
                HomeSearchActivity.start(HomeFragment.this.getContext());
                break;
        }
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            HomeBannerBean homeBannerBean = (HomeBannerBean) path;
            //Glide 加载图片简单用法
            ImageLoaderUtil.getInstance().loadImage(context, homeBannerBean.getUrl(), imageView);
        }
    }

    private void initMarquee() {
        MQApi.getUserMessage(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<MarqueeBean>> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<List<MarqueeBean>>>() {
                }.getType());
                if (myApiResult.isOk()) {
                    List<String> messages = new ArrayList<>();
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        messages.add(myApiResult.getData().get(i).getContent());
                    }
                    marqueeView.startWithList(messages);
                }
            }
        });
    }

    private void getUserInviteTotal() {
        showWaitDialog(HomeFragment.this.getActivity());
        MQApi.getUserInviteTotal(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<UserInviteTotalBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<UserInviteTotalBean>>() {
                }.getType());
                if (myApiResult.isOk()) {
                    showQrCodeDialog(myApiResult.getData().getGroupCount());
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private View rootView;

    private void showQrCodeDialog(String numb) {
        customDialog = CustomDialog.show(HomeFragment.this.getActivity(), R.layout.dialog_ercode_shard, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View view) {
                rootView = view;
                ImageView iv_head = rootView.findViewById(R.id.iv_user_head);
                ImageView iv_qrcode = rootView.findViewById(R.id.iv_qrcode);
                TextView tv_shard = rootView.findViewById(R.id.tv_shard);
                TextView tv_name = rootView.findViewById(R.id.tv_name);
                TextView tv_save = rootView.findViewById(R.id.tv_save);
                TextView tv_numb = rootView.findViewById(R.id.textViewNumb);
                if ("0".equals(numb)) {
                    tv_numb.setText("");
                } else {
                    tv_numb.setText("已邀请(" + numb + ")");
                    tv_numb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserInvitationPeopleActivity.start(HomeFragment.this.getContext(), numb);
                        }
                    });
                }
                ImageLoaderUtil.getInstance().loadCircleImage(HomeFragment.this.getContext(), userBean.getFace(), iv_head);
                ImageLoaderUtil.getInstance().loadImage(HomeFragment.this.getContext(), userBean.getQrCode(), iv_qrcode);
                tv_name.setText(userBean.getNickName());
                tv_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveViewToImg(rootView);
                        customDialog.doDismiss();
                    }
                });

                tv_shard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                        showShareDialog();
                    }
                });
            }
        }).setCanCancel(true);

    }

    private void showShareDialog() {
        customDialog = CustomDialog.show(HomeFragment.this.getContext(), R.layout.dialog_shard, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
//                        //绑定布局
//                        ImageView btnOk = rootView.findViewById(R.id.btn_ok);
                TextView textViewFriend = rootView.findViewById(R.id.tv_share_friend);
                TextView textViewMoment = rootView.findViewById(R.id.tv_share_moment);
                ImageView imageViewFriend = rootView.findViewById(R.id.iv_share_friend);
                ImageView imageViewMoment = rootView.findViewById(R.id.iv_share_moment);
                TextView tv_cancel = rootView.findViewById(R.id.tv_cancel);
                textViewFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
//                        share(1);
                        getShareInfo(1);
                    }
                });
                textViewMoment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
//                        share(2);
                        getShareInfo(2);
                    }
                });
                imageViewFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
//                        share(1);
                        getShareInfo(1);
                    }
                });
                imageViewMoment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
//                        share(2);
                        getShareInfo(2);
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                    }
                });
            }
        }).setCanCancel(true);
    }

    private void getShareInfo(int type) {
        showWaitDialog(HomeFragment.this.getActivity());
        MQApi.userShare(userBean.getUId(), 2, "", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<ShareInfoBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<ShareInfoBean>>() {
                }.getType());
                if (myApiResult.isOk()) {
                    share(type, myApiResult.getData());
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }


    private UMImage thumb;
    private UMWeb web;

    private void share(int type, ShareInfoBean shareInfoBean) {
        if (thumb == null) {
            thumb = new UMImage(HomeFragment.this.getContext(), R.mipmap.ic_shard_logo);
        }
        if (web == null) {
            web = new UMWeb(shareInfoBean.getShareUrl());
            web.setTitle(shareInfoBean.getTitle());//标题
            web.setThumb(thumb);  //缩略图
            web.setDescription(shareInfoBean.getDetail());//描述
        }
        if (type == 1) {
            new ShareAction(HomeFragment.this.getActivity())
                    .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                    .setCallback(shareListener)//回调监听器
                    .withMedia(web)
                    .share();
        } else {
            new ShareAction(HomeFragment.this.getActivity())
                    .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                    .setCallback(shareListener)//回调监听器
                    .withMedia(web)
                    .share();
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            RxToast.showToast("成功了");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            RxToast.showToast("失败了");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            RxToast.showToast("取消了");
        }
    };

    private void saveViewToImg(View rootView) {
        try {
            if (ActivityCompat.checkSelfPermission(HomeFragment.this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(HomeFragment.this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                ActivityCompat.requestPermissions(HomeFragment.this.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
//                mLDialog.setDialogText("正在保存图片...");
//                mLDialog.show();
                showWaitDialog(HomeFragment.this.getActivity(), "正在保存图片...");
                saveMyBitmap(createViewBitmap(rootView));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    //使用IO流将bitmap对象存到本地指定文件夹
    public void saveMyBitmap(final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String filePath = Environment.getExternalStorageDirectory().getPath();
                File file = new File(filePath + "/DCIM/Camera/" + Calendar.getInstance().getTimeInMillis() + ".png");
                try {
                    file.createNewFile();


                    FileOutputStream fOut = null;
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);


                    Message msg = Message.obtain();
                    msg.obj = file.getPath();
                    handler.sendMessage(msg);
                    //Toast.makeText(PayCodeActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String picFile = (String) msg.obj;
            String[] split = picFile.split("/");
            String fileName = split[split.length - 1];
            try {
                MediaStore.Images.Media.insertImage(HomeFragment.this.getContext().getContentResolver()
                        , picFile, fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            HomeFragment.this.getContext().sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                    + picFile)));
            RxToast.showToast("图片保存图库成功");
            dissmissWaitDialog();
        }
    };

    CustomDialog customDialog;
    private Dialog mScreenShopDialog;

    @OnClick(R.id.iv_screen)
    public void onViewClicked() {
//
        if (null == mScreenShopDialog) {
            mScreenShopDialog = new Dialog(HomeFragment.this.getContext(), R.style.dialog_bottom_full);
            mScreenShopDialog.setCanceledOnTouchOutside(true); //手指触碰到外界取消
            mScreenShopDialog.setCancelable(true);             //可取消 为true
            Window window = mScreenShopDialog.getWindow();      // 得到dialog的窗体
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.share_animation);
            View view = View.inflate(HomeFragment.this.getContext(), R.layout.dialog_screen_shop, null); //获取布局视图
            ImageView imageViewCancel = view.findViewById(R.id.iv_cancel);
            imageViewCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mScreenShopDialog != null) {
                        mScreenShopDialog.dismiss();
                    }
                }
            });

            RadioButton radioButtonAll = view.findViewById(R.id.radioButtonAll);
            RadioButton radioButtonShangwu = view.findViewById(R.id.radioButtonShangwu);
            RadioButton radioButtonLiangfan = view.findViewById(R.id.radioButtonLiangfan);
            RadioButton radioButtonJiuba = view.findViewById(R.id.radioButtonJiuba);
            RadioButton radioButtonQingba = view.findViewById(R.id.radioButtonQingba);
            radioButtonAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        radioButtonShangwu.setChecked(false);
                        radioButtonLiangfan.setChecked(false);
                        radioButtonJiuba.setChecked(false);
                        radioButtonQingba.setChecked(false);
                        mKind = 0;
                    }
                }
            });
            radioButtonShangwu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        radioButtonAll.setChecked(false);
                        radioButtonLiangfan.setChecked(false);
                        radioButtonJiuba.setChecked(false);
                        radioButtonQingba.setChecked(false);
                        mKind = 1;
                    }
                }
            });
            radioButtonLiangfan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        radioButtonAll.setChecked(false);
                        radioButtonShangwu.setChecked(false);
                        radioButtonJiuba.setChecked(false);
                        radioButtonQingba.setChecked(false);
                        mKind = 3;
                    }
                }
            });
            radioButtonJiuba.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        radioButtonAll.setChecked(false);
                        radioButtonShangwu.setChecked(false);
                        radioButtonLiangfan.setChecked(false);
                        radioButtonQingba.setChecked(false);
                        mKind = 2;
                    }
                }
            });
            radioButtonQingba.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        radioButtonAll.setChecked(false);
                        radioButtonShangwu.setChecked(false);
                        radioButtonLiangfan.setChecked(false);
                        radioButtonJiuba.setChecked(false);
                        mKind = 4;
                    }
                }
            });
            RadioButton radioButton1 = view.findViewById(R.id.radioButton1);
            radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mActivityName = 0;
                    }
                }
            });
            RadioButton radioButton2 = view.findViewById(R.id.radioButton2);
            radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mActivityName = 1;
                    }
                }
            });
            RadioButton radioButton3 = view.findViewById(R.id.radioButton3);
            radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mActivityName = 2;
                    }
                }
            });
            RangeBar rangeBar = view.findViewById(R.id.rangebar);
            TextView textView = view.findViewById(R.id.textViewZhekou);
            rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                @Override
                public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                    textView.setText("(" + leftPinIndex + "~" + rightPinIndex + ")");
                    mDiscountLeft = leftPinIndex;
                    mDiscountRight = rightPinIndex;
                }

                @Override
                public void onTouchStarted(RangeBar rangeBar) {

                }

                @Override
                public void onTouchEnded(RangeBar rangeBar) {

                }
            });
            TextView textViewSure = view.findViewById(R.id.tv_sure);
            textViewSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStoreId = "";
                    EventBus.getDefault().post(new screenShopMessage(mKind, mDiscountLeft, mDiscountRight, mActivityName, mStoreId));

                    if (mScreenShopDialog != null) {
                        mScreenShopDialog.dismiss();
                    }

                }
            });

            window.setContentView(view);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
        }
        mScreenShopDialog.show();
    }

    private void showNewbieGuide() {
        LinearLayout view = (LinearLayout) tablayout.getChildAt(0);
        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(600);
        enterAnimation.setFillAfter(true);

        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(600);
        exitAnimation.setFillAfter(true);

        NewbieGuide.with(HomeFragment.this.getActivity())
                .setLabel("listener")
                .alwaysShow(true)//总是显示，调试时可以打开
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        EventBus.getDefault().post(new homeEndMessage(""));
                    }
                })
                .addGuidePage(
                        GuidePage.newInstance()
                                .addHighLight(textViewSearch, HighLight.Shape.ROUND_RECTANGLE, 20, 5,
                                        new RelativeGuide(R.layout.view_guide_search,Gravity.BOTTOM,50))
//                                .setLayoutRes(R.layout.view_guide_simple)
                                .setEnterAnimation(enterAnimation)
                )
                .addGuidePage(
                        GuidePage
                                .newInstance()
                                .addHighLight(view.getChildAt(0), HighLight.Shape.OVAL, 5, 5,
//                                        new RelativeGuide(R.layout.view_guide_whereplay,Gravity.TOP,50))
                                        null)
                                .setLayoutRes(R.layout.view_guide_whereplay)
                )
                .addGuidePage(
                        GuidePage
                                .newInstance()
                                .addHighLight(view.getChildAt(1), HighLight.Shape.OVAL, 5, 5,
//                                        new RelativeGuide(R.layout.view_guide_whoplay,Gravity.TOP,50))
                                        null)
                                .setLayoutRes(R.layout.view_guide_whoplay)
                )
                .show();

    }

    class MyFragmentPagerAdapt extends FragmentPagerAdapter {

        private FragmentManager fragmetnmanager;  //创建FragmentManager
        private List<Fragment> listfragment; //创建一个List<Fragment>

        public MyFragmentPagerAdapt(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.fragmetnmanager = fm;
            this.listfragment = list;
        }

        @Override
        public Fragment getItem(int i) {
            return listfragment.get(i); //返回第几个fragment
        }

        @Override
        public int getCount() {
            return listfragment.size(); //总共有多少个fragment
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            View view = (View) object;
            int i = (int) view.getTag();
            return i;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        isLoadData = true;
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        marqueeView.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        marqueeView.stopFlipping();
    }
}

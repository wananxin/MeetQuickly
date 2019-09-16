package com.andy.meetquickly.activity.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.appointment.UseCouponActivity;
import com.andy.meetquickly.activity.user.ManageShopActivity;
import com.andy.meetquickly.adapter.ReportAdapter;
import com.andy.meetquickly.base.BaseInvasionActivity;
import com.andy.meetquickly.bean.ReportBean;
import com.andy.meetquickly.bean.ShareInfoBean;
import com.andy.meetquickly.bean.ShopImgBean;
import com.andy.meetquickly.bean.ShopInfoBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.fragment.home.WherePlayFragment;
import com.andy.meetquickly.fragment.shopinfo.ShopConsumeFragment;
import com.andy.meetquickly.fragment.shopinfo.ShopEventFragment;
import com.andy.meetquickly.fragment.shopinfo.ShopInfoFragment;
import com.andy.meetquickly.fragment.shopinfo.ShopMeetFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.receiveCouponMessage;
import com.andy.meetquickly.message.showGiftMessage;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SlidingScaleTabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;
import com.kongzue.dialog.v2.CustomDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopDetailActivity extends BaseInvasionActivity {

    @BindView(R.id.view)
    Toolbar view;
    @BindView(R.id.ll_title_bar)
    LinearLayout llTitleBar;
    @BindView(R.id.tablayout)
    SlidingScaleTabLayout tablayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_title_bar)
    TextView tvTitleBar;
    @BindView(R.id.textViewNumb)
    TextView textViewNumb;
    @BindView(R.id.textViewTv)
    TextView textViewTv;
    @BindView(R.id.constraintLayout_coupon)
    ConstraintLayout constraintLayoutCoupon;

    private String storeId;
    private String storeName;
    private String companyName;

    private int[] colors = {
            Color.RED, Color.WHITE, Color.CYAN, Color.GREEN
    };
    private String[] titles = {
            "资料", "活动", "消费参考", "可能邂逅的人"
    };

    private List<ShopImgBean> mData = new ArrayList<>();
    private List<ReportBean> reportBeans = new ArrayList<>();
    private UMImage thumb;
    private UMWeb web;
    private String reportContent = "";
    private UserBean userBean;
    private ShopInfoBean.StoreCouponBean storeCouponBean;
    private String isCoupon;
    private CustomDialog customDialog1;
    private TextView textViewLingqu;
    private String kind;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_detail;
    }

    @Override
    public void initView() {
        setSwipeBackEnable(false);
        userBean = UserCache.getInstance().getUser();
        storeId = this.getIntent().getStringExtra("storeId");
        storeName = this.getIntent().getStringExtra("storeName");
        companyName = this.getIntent().getStringExtra("companyName");
        kind = this.getIntent().getStringExtra("kind");
        if (null == kind){
            getShopInfo(storeId);
        }
        storeCouponBean = (ShopInfoBean.StoreCouponBean) this.getIntent().getSerializableExtra("storeCouponBean");
        isCoupon = this.getIntent().getStringExtra("isCoupon");
        tvTitleBar.setText(companyName+"("+storeName+"经理)");
        constraintLayoutCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReceiveCoupon();
            }
        });

        if (null == storeCouponBean) {
            constraintLayoutCoupon.setVisibility(View.GONE);
        } else {
            constraintLayoutCoupon.setVisibility(View.VISIBLE);
            textViewNumb.setText("￥" + storeCouponBean.getCouponAmountX());
            if ("1".equals(isCoupon)){
                constraintLayoutCoupon.setBackgroundResource(R.mipmap.ic_shop_coupon_unreceive);
                textViewTv.setText("已领取");
            }else {
                constraintLayoutCoupon.setBackgroundResource(R.mipmap.ic_shop_coupon_receive);
                textViewTv.setText("领取");
            }
        }


        List<Fragment> fragments = new ArrayList<>();
        ShopInfoFragment fragment1 = ShopInfoFragment.newInstance(storeId);
        ShopEventFragment fragment2 = ShopEventFragment.newInstance(storeId);
        ShopConsumeFragment fragment3 = ShopConsumeFragment.newInstance(storeId);
        ShopMeetFragment fragment4 = ShopMeetFragment.newInstance(storeId);
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        FragmentManager fm = this.getSupportFragmentManager();
        viewpager.setAdapter(new MyFragmentPagerAdapt(fm, fragments));
        viewpager.setOffscreenPageLimit(4);
        tablayout.setViewPager(viewpager);
        viewpager.setCurrentItem(0);

        mImmersionBar.destroy();
        ImmersionBar.with(this)
                .statusBarView(R.id.view)
                .navigationBarEnable(false)
                .init();
        collapsingToolbar.post(() -> {
            int offHeight = collapsingToolbar.getHeight() - ImmersionBar.getStatusBarHeight(ShopDetailActivity.this);
            appBarLayout.addOnOffsetChangedListener((appBarLayout1, i) -> {
                if (Math.abs(i) >= offHeight) {
                    llTitleBar.setVisibility(View.VISIBLE);
                    ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).fitsSystemWindows(false).init();
                } else {
                    llTitleBar.setVisibility(View.GONE);
                    ImmersionBar.with(this).statusBarDarkFont(false).init();
                }
            });
        });
        initBanner();
        initData();
    }

    private void getShopInfo(String storeId) {
        kind = "1";
        MQApi.getShopsList(userBean.getUId(), storeId, 1, 0, 0, 10, 0,
                RxSPTool.getContent(ShopDetailActivity.this, Constant.KEY_LONGITUDE),
                RxSPTool.getContent(ShopDetailActivity.this, Constant.KEY_LATITUDE), new SimpleCallBack<String>() {
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
                                kind = shopInfoBean.getKind();
                            }
                        }
                    }
                });
    }

    private void showReceiveCoupon() {
        customDialog1 = CustomDialog.show(ShopDetailActivity.this, R.layout.dialog_receive_coupon, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
                TextView textViewMoney = rootView.findViewById(R.id.textViewMoney);
                textViewLingqu = rootView.findViewById(R.id.textViewLingqu);
                TextView textViewTime = rootView.findViewById(R.id.textViewTime);
                TextView textViewNumb = rootView.findViewById(R.id.textViewNumb);
                TextView textViewYuding = rootView.findViewById(R.id.textViewYuding);
                TextView textViewBiLi = rootView.findViewById(R.id.textViewBiLi);
                textViewBiLi.setText("1.此代金券按酒水消费金额的10%进行抵现，最高可抵现"+storeCouponBean.getCouponAmountX()+"元");
                textViewMoney.setText(storeCouponBean.getCouponAmountX() + "元");
                textViewTime.setText("有效日期："+storeCouponBean.getBeginTime()+"~"+storeCouponBean.getEndTime());
                textViewNumb.setText("剩余"+storeCouponBean.getHavaRecevied()+"/"+storeCouponBean.getAvailableNo()+"张");
                textViewLingqu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lingquCoupon();
                    }
                });
                if ("1".equals(isCoupon)){
                    textViewLingqu.setText("已领取");
                    textViewLingqu.setEnabled(false);
                }
                textViewYuding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog1.doDismiss();
                        UseCouponActivity.start(ShopDetailActivity.this, 1, kind, storeId,companyName,storeName);
                    }
                });

            }
        }).setCanCancel(true);
    }

    private void lingquCoupon() {
        showWaitDialog(ShopDetailActivity.this);
        MQApi.userReceiveShopCoupon(userBean.getUId(), storeId, storeCouponBean.getIdX(), storeCouponBean.getCouponAmountX(), new SimpleCallBack<String>() {
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
                if (myApiResult.isOk()){
                    textViewLingqu.setText("已领取");
                    constraintLayoutCoupon.setBackgroundResource(R.mipmap.ic_shop_coupon_unreceive);
                    textViewTv.setText("已领取");
                    constraintLayoutCoupon.setEnabled(false);
                    EventBus.getDefault().post(new receiveCouponMessage(storeCouponBean.getStoreIdX()));
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void initBanner() {
        //初始化Banner设置
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(mData);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //设置轮播时间
        banner.setDelayTime(3000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @OnClick({R.id.iv_more, R.id.iv_back,R.id.bt_sure,R.id.iv_back_black})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_more:
                showMoreDialog();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_sure:
                UseCouponActivity.start(ShopDetailActivity.this, 1, kind, storeId,companyName,storeName);
                break;
            case R.id.iv_back_black:
                finish();
                break;
        }
    }

    CustomDialog customDialog;

    private void showMoreDialog() {
        customDialog = CustomDialog.show(ShopDetailActivity.this, R.layout.dialog_add_reserve, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
//                        //绑定布局
//                        ImageView btnOk = rootView.findViewById(R.id.btn_ok);
                TextView tv_one = rootView.findViewById(R.id.tv_one);
                TextView tv_two = rootView.findViewById(R.id.tv_two);
                TextView tv_cancel = rootView.findViewById(R.id.tv_cancel);
                tv_one.setText("分享此网店");
                tv_two.setText("举报此网店");
                tv_one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                        showShareDialog();
                    }
                });
                tv_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                        getReportContent();
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

    private void getReportContent() {
        MQApi.getReportContentList("report_content", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<ReportBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<ReportBean>>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<ReportBean>>>() {});
                if (myApiResult.isOk()) {
                    reportBeans.clear();
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        reportBeans.add(myApiResult.getData().get(i));
                    }
                    showReportDialog();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void showReportDialog() {
        reportContent = "";
        //活动
        customDialog = CustomDialog.show(mContext, R.layout.dialog_report, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
//                        //绑定布局
//                        ImageView btnOk = rootView.findViewById(R.id.btn_ok);
                RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                ReportAdapter recycleAdapter = new ReportAdapter(reportBeans);
                recycleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        reportContent = reportBeans.get(position).getTypeName();
                        recycleAdapter.setSelect(reportContent);
                        recycleAdapter.notifyDataSetChanged();
                    }
                });
//设置布局管理器
                recyclerView.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
                layoutManager.setOrientation(OrientationHelper.VERTICAL);
//设置Adapter
                recyclerView.setAdapter(recycleAdapter);
                TextView tv_cancel = rootView.findViewById(R.id.tv_cancel);
                TextView tv_sure = rootView.findViewById(R.id.tv_sure);
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                    }
                });
                tv_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (reportContent.isEmpty()) {
                            RxToast.showToast("请选择举报内容");
                        } else {
                            reportShop();
                            customDialog.doDismiss();
                        }
                    }
                });
//                        //绑定事件
//                        btnOk.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                //Example：让对话框消失
//                                dialog.doDismiss();
//                            }
//                        });
            }
        });
    }

    private void reportShop() {
        String Uid;
        if (null != userBean) {
            Uid = userBean.getUId();
        } else {
            Uid = "";
        }
        MQApi.addReportShop(Uid, storeId, reportContent, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult myApiResult = JSON.parseObject(s, new MyApiResult().getClass());
                if (myApiResult.isOk()) {
                    RxToast.showToast("投诉举报成功");
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void showShareDialog() {
        customDialog = CustomDialog.show(mContext, R.layout.dialog_shard, new CustomDialog.BindView() {
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

    private void getShareInfo(int type){
        showWaitDialog(ShopDetailActivity.this);
        MQApi.userShare(userBean.getUId(), 1, storeId, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<ShareInfoBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<ShareInfoBean>>(){}.getType());
                if (myApiResult.isOk()){
                    share(type,myApiResult.getData());
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void share(int type,ShareInfoBean shareInfoBean) {
        if (thumb == null) {
            thumb = new UMImage(this, R.mipmap.ic_shard_logo);
        }
        if (web == null) {
            web = new UMWeb(shareInfoBean.getShareUrl());
            web.setTitle(shareInfoBean.getTitle());//标题
            web.setThumb(thumb);  //缩略图
            web.setDescription(shareInfoBean.getDetail());//描述
        }
        if (type == 1) {
            new ShareAction(ShopDetailActivity.this)
                    .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                    .setCallback(shareListener)//回调监听器
                    .withMedia(web)
                    .share();
        } else {
            new ShareAction(ShopDetailActivity.this)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
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
            ShopImgBean shopImgBean = (ShopImgBean) path;
            //Glide 加载图片简单用法
            ImageLoaderUtil.getInstance().loadImage(context, shopImgBean.getUrl(), imageView);
        }
    }

    public static void start(Context context, String storeId, String storeName,String kind, ShopInfoBean.StoreCouponBean storeCouponBean, String isCoupon,String companyName) {
        Intent starter = new Intent(context, ShopDetailActivity.class);
        starter.putExtra("storeId", storeId);
        starter.putExtra("storeName", storeName);
        starter.putExtra("kind", kind);
        starter.putExtra("storeCouponBean", storeCouponBean);
        starter.putExtra("isCoupon", isCoupon);
        starter.putExtra("companyName", companyName);
        context.startActivity(starter);
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
    public void initData() {
        MQApi.getShopImgListByStoreId(storeId, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<ShopImgBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<ShopImgBean>>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<ShopImgBean>>>() {});
                if (myApiResult.isOk()) {
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        mData.add(myApiResult.getData().get(i));
                    }
                    banner.setImages(mData);
                    banner.start();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initView();
    }
}

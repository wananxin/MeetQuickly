package com.andy.meetquickly.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.andy.meetquickly.activity.user.UpdataUserAllInfoActivity;
import com.andy.meetquickly.adapter.ReportAdapter;
import com.andy.meetquickly.adapter.UserVisitPeopleAdapter;
import com.andy.meetquickly.base.BaseInvasionActivity;
import com.andy.meetquickly.bean.ReportBean;
import com.andy.meetquickly.bean.UserBaseBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserMediaBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.fragment.userinfo.UserInfoFragment;
import com.andy.meetquickly.fragment.userinfo.UserMomentFragment;
import com.andy.meetquickly.fragment.userinfo.UserVideoFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SlidingScaleTabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;
import com.kongzue.dialog.v2.CustomDialog;
import com.vondear.rxtool.view.RxToast;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

public class UserDataActivity extends BaseInvasionActivity {

    @BindView(R.id.tablayout)
    SlidingScaleTabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.view)
    Toolbar view;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.ll_title_bar)
    LinearLayout llTitleBar;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_yp)
    TextView tvYp;
    @BindView(R.id.iv_follow)
    ImageView ivFollow;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_visit_numb)
    TextView tvVisitNumb;
    @BindView(R.id.recyclerView_visit)
    RecyclerView recyclerViewVisit;
    @BindView(R.id.iv_visit_more)
    ImageView ivVisitMore;
    @BindView(R.id.tv_titlt_bar)
    TextView tvTitltBar;

    private List<UserBaseBean.UserVisitBean> mData = new ArrayList<>();
    private List<UserMediaBean> mMediaData = new ArrayList<>();

    private String[] titles = {
            "动态", "资料", "视频"
    };
    private String uId;
    private UserVisitPeopleAdapter adapter;
    private UserBean userBean;
    private String nickName;
    private String userFace;
    private boolean isLoad = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_data;
    }

    @Override
    public void initView() {
        setSwipeBackEnable(false);
        uId = this.getIntent().getStringExtra("uId");
        userBean = UserCache.getInstance().getUser();
        List<Fragment> fragments = new ArrayList<>();
        UserMomentFragment fragment1 = UserMomentFragment.newInstance(uId);
        UserInfoFragment fragment2 = UserInfoFragment.newInstance(uId);
//        ShopInfoFragment fragment2 = new ShopInfoFragment();
        UserVideoFragment fragment3 = UserVideoFragment.newInstance(uId);
//        FindPeoplePlayFragment fragment2 = FindPeoplePlayFragment.newInstance(18,2);
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        FragmentManager fm = this.getSupportFragmentManager();
        viewpager.setAdapter(new MyFragmentPagerAdapt(fm, fragments));
        viewpager.setOffscreenPageLimit(3);
        tablayout.setViewPager(viewpager);
        viewpager.setCurrentItem(0);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewVisit.setLayoutManager(linearLayoutManager);
        adapter = new UserVisitPeopleAdapter(mData, mContext);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserDataActivity.start(UserDataActivity.this, mData.get(position).getUId());
            }
        });
        recyclerViewVisit.setAdapter(adapter);

        mImmersionBar.destroy();
        ImmersionBar.with(this)
                .statusBarView(R.id.view)
                .navigationBarEnable(false)
                .init();
        collapsingToolbar.post(() -> {
            int offHeight = collapsingToolbar.getHeight() - ImmersionBar.getStatusBarHeight(UserDataActivity.this);
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

    private void initBanner() {
        //初始化Banner设置
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(mMediaData);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //设置轮播时间
        banner.setDelayTime(3000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
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
            UserMediaBean shopImgBean = (UserMediaBean) path;
            //Glide 加载图片简单用法
            ImageLoaderUtil.getInstance().loadImage(context, shopImgBean.getUrl(), imageView);
        }
    }

    @Override
    public void initData() {
        String Uid;
        if (null != userBean) {
            Uid = userBean.getUId();
        } else {
            Uid = "";
        }
        MQApi.getUserBaseInfo(uId, Uid, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<UserBaseBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<UserBaseBean>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<UserBaseBean>>(){});
                if (myApiResult.isOk()) {
                    nickName = myApiResult.getData().getNickName();
                    tvName.setText(myApiResult.getData().getNickName());
                    tvTitltBar.setText(myApiResult.getData().getNickName());
                    tvYp.setText("YP：" + myApiResult.getData().getUid());
                    tvDistance.setText(myApiResult.getData().getDistance() + "km");
                    if ("1".equals(myApiResult.getData().getSex())) {
                        ivSex.setImageResource(R.mipmap.ic_men);
                    } else if ("2".equals(myApiResult.getData().getSex())) {
                        ivSex.setImageResource(R.mipmap.ic_women);
                    } else {
                        ivSex.setImageResource(R.mipmap.ic_women);
                    }
                    tvAge.setText(myApiResult.getData().getAge() + "");
                    tvLevel.setText("LV" + myApiResult.getData().getLevel());
                    tvVisitNumb.setText("（" + myApiResult.getData().getVisitCount() + "）");
                    if ("1".equals(myApiResult.getData().getIsAttention())) {
                        ivFollow.setImageResource(R.mipmap.ic_user_yiguanzhu);
                        ivFollow.setEnabled(false);
                    } else {
                        ivFollow.setImageResource(R.mipmap.ic_user_guanzhu);
                    }

                    for (int i = 0; i < myApiResult.getData().getUserVisit().size(); i++) {
                        mData.add(myApiResult.getData().getUserVisit().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    if (isLoad) {
                        if (mMediaData.size() == 0) {
                            UserMediaBean userMediaBean = new UserMediaBean();
                            userMediaBean.setUrl(myApiResult.getData().getFace());
                            mMediaData.add(userMediaBean);
                            banner.setImages(mMediaData);
                            banner.start();
                        }
                    } else {
                        isLoad = true;
                        userFace = myApiResult.getData().getFace();
                    }
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });

        MQApi.getUserImageOrVideoList(uId, 1, 1, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<UserMediaBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<UserMediaBean>>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<UserMediaBean>>>(){});
                if (myApiResult.isOk()) {
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        mMediaData.add(myApiResult.getData().get(i));
                    }
                    if (isLoad) {
                        if (mMediaData.size() == 0) {
                            UserMediaBean userMediaBean = new UserMediaBean();
                            userMediaBean.setUrl(userFace);
                            mMediaData.add(userMediaBean);
                        }
                    } else {
                        isLoad = true;
                    }
                    banner.setImages(mMediaData);
                    banner.start();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @OnClick({R.id.iv_more, R.id.iv_back, R.id.iv_follow, R.id.iv_visit_more, R.id.iv_back_black, R.id.bt_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_more:
                showMoreDialog();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_follow:
                followUser();
                break;
            case R.id.iv_visit_more:
                UserVisibListActivity.start(mContext, 1, uId);
                break;
            case R.id.iv_back_black:
                finish();
                break;
            case R.id.bt_sure:
                RongIM.getInstance().startPrivateChat(UserDataActivity.this, uId, nickName);
                break;
        }
    }

    CustomDialog customDialog;

    private void showMoreDialog() {
        customDialog = CustomDialog.show(UserDataActivity.this, R.layout.dialog_add_reserve, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
//                        //绑定布局
//                        ImageView btnOk = rootView.findViewById(R.id.btn_ok);
                TextView tv_one = rootView.findViewById(R.id.tv_one);
                TextView tv_two = rootView.findViewById(R.id.tv_two);
                TextView tv_cancel = rootView.findViewById(R.id.tv_cancel);
                tv_one.setText("举报TA");
                tv_two.setText("屏蔽TA动态");
                tv_one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                        getReportContent();
                    }
                });
                tv_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                        ShieldUserMoment();
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

    private List<ReportBean> reportBeans = new ArrayList<>();

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

    private String reportContent;

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
                            reportUser();
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

    private void reportUser() {
        String Uid;
        if (null != userBean) {
            Uid = userBean.getUId();
        } else {
            Uid = "";
        }
        MQApi.addReportUserHomePage(Uid, uId, reportContent, new SimpleCallBack<String>() {
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

    private void ShieldUserMoment() {
        showWaitDialog(UserDataActivity.this);
        MQApi.shieldTaComments(userBean.getUId(), uId, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                }.getType());
                if (myApiResult.isOk()) {
                    RxToast.showToast("屏蔽成功");
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void followUser() {
        showWaitDialog(UserDataActivity.this);
        MQApi.followUser(userBean.getUId(), uId, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                }.getType());
                if (myApiResult.isOk()) {
                    ivFollow.setImageResource(R.mipmap.ic_user_yiguanzhu);
                    ivFollow.setEnabled(false);
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initView();
    }

    public static void start(Context context, String uId) {
        if (StringUtil.isNotNull(uId)) {
            if (uId.equals(UserCache.getInstance().getUser().getUId())) {
                UpdataUserAllInfoActivity.start(context);
            } else {
                Intent starter = new Intent(context, UserDataActivity.class);
                starter.putExtra("uId", uId);
                context.startActivity(starter);
            }
        }
    }
}

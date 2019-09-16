package com.andy.meetquickly.activity.user;

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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.UserDataActivity;
import com.andy.meetquickly.activity.home.UserVisibListActivity;
import com.andy.meetquickly.adapter.UserVisitPeopleAdapter;
import com.andy.meetquickly.base.BaseInvasionActivity;
import com.andy.meetquickly.bean.UserBaseBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.fragment.shopinfo.ShopInfoFragment;
import com.andy.meetquickly.fragment.updateUserInfo.UpdateBaseInfoFragment;
import com.andy.meetquickly.fragment.updateUserInfo.UpdateMomentFragment;
import com.andy.meetquickly.fragment.updateUserInfo.UpdatePicFragment;
import com.andy.meetquickly.fragment.updateUserInfo.UpdateVideoFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.updatePicMessage;
import com.andy.meetquickly.message.updateVideoMessage;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SlidingScaleTabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdataUserAllInfoActivity extends BaseInvasionActivity {

    @BindView(R.id.iv_user_head)
    ImageView ivUserHead;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_yp)
    TextView tvYp;
    @BindView(R.id.iv_follow)
    ImageView ivFollow;
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
    @BindView(R.id.iv_back_black)
    ImageView ivBackBlack;
    @BindView(R.id.tv_titlt_bar)
    TextView tvTitltBar;
    @BindView(R.id.ll_title_bar)
    LinearLayout llTitleBar;
    @BindView(R.id.tablayout)
    SlidingScaleTabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.textView105)
    TextView textView105;
    @BindView(R.id.view)
    Toolbar view;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    private List<UserBaseBean.UserVisitBean> mData = new ArrayList<>();
    private String[] titles = {
            "资料", "动态", "图片", "视频"
    };
    private UserVisitPeopleAdapter adapter;
    private UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_updata_user_all_info;
    }

    @Override
    public void initView() {
        userBean = UserCache.getInstance().getUser();
        List<Fragment> fragments = new ArrayList<>();
        UpdateBaseInfoFragment fragment1 = new UpdateBaseInfoFragment();
        UpdateMomentFragment fragment2 = new UpdateMomentFragment();
        UpdatePicFragment fragment3 = new UpdatePicFragment();
        UpdateVideoFragment fragment4 = UpdateVideoFragment.newInstance(userBean.getUId());
//        FindPeoplePlayFragment fragment2 = FindPeoplePlayFragment.newInstance(18,2);
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        FragmentManager fm = this.getSupportFragmentManager();
        viewpager.setAdapter(new MyFragmentPagerAdapt(fm, fragments));
        viewpager.setOffscreenPageLimit(4);
        tablayout.setViewPager(viewpager);
        viewpager.setCurrentItem(0);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivBackBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivMore.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewVisit.setLayoutManager(linearLayoutManager);
        adapter = new UserVisitPeopleAdapter(mData, mContext);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserDataActivity.start(UpdataUserAllInfoActivity.this, mData.get(position).getUId());
            }
        });
        recyclerViewVisit.setAdapter(adapter);

        mImmersionBar.destroy();
        ImmersionBar.with(this)
                .statusBarView(R.id.view)
                .navigationBarEnable(false)
                .init();
        collapsingToolbar.post(() -> {
            int offHeight = collapsingToolbar.getHeight() - ImmersionBar.getStatusBarHeight(UpdataUserAllInfoActivity.this);
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
        initData();
    }

    @Override
    public void initData() {
        MQApi.getUserBaseInfo(userBean.getUId(), "", new SimpleCallBack<String>() {
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
                    ImageLoaderUtil.getInstance().loadImage(mContext, myApiResult.getData().getFace(), ivUserHead);
                    tvName.setText(myApiResult.getData().getNickName());
                    tvTitltBar.setText(myApiResult.getData().getNickName());
                    tvYp.setText("YP：" + myApiResult.getData().getUid());
                    if (myApiResult.getData().getSex().equals("1")) {
                        ivSex.setImageResource(R.mipmap.ic_men);
                    } else if (myApiResult.getData().getSex().equals("2")) {
                        ivSex.setImageResource(R.mipmap.ic_women);
                    } else {
                        ivSex.setImageResource(R.mipmap.ic_women);
                    }
                    tvAge.setText(myApiResult.getData().getAge() + "");
                    tvLevel.setText("LV" + myApiResult.getData().getLevel());
                    tvVisitNumb.setText("（" + myApiResult.getData().getVisitCount() + "）");

                    for (int i = 0; i < myApiResult.getData().getUserVisit().size(); i++) {
                        mData.add(myApiResult.getData().getUserVisit().get(i));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @OnClick(R.id.iv_visit_more)
    public void onViewClicked() {
        UserVisibListActivity.start(UpdataUserAllInfoActivity.this, 1, userBean.getUId());
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

    public static void start(Context context) {
        Intent starter = new Intent(context, UpdataUserAllInfoActivity.class);
        context.startActivity(starter);
    }

    private List<LocalMedia> resultSelectList = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    resultSelectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : resultSelectList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    EventBus.getDefault().post(new updatePicMessage(resultSelectList));
                    break;
                case VIDEO_TRIM_REQUEST_CODE:
                    //剪切回来的文件
                    String videoPath = data.getStringExtra("cutPath");
                    EventBus.getDefault().post(new updateVideoMessage(videoPath, 1));

                    break;
                case VIDEO_MY_REQUEST_CODE:
                    //拍摄返回
                    String pa1 = data.getStringExtra("video_path");
                    EventBus.getDefault().post(new updateVideoMessage(pa1, 0));
                    break;
                case VIDEO_REQUESTCODE:
                    //选择返回
                    String pa2 = data.getStringExtra("video_path");
                    EventBus.getDefault().post(new updateVideoMessage(pa2, 0));
                    break;
            }
        }
    }

    public static final int VIDEO_TRIM_REQUEST_CODE = 23;
    public static final int VIDEO_MY_REQUEST_CODE = 1992;
    private final static int VIDEO_REQUESTCODE = 22;


}

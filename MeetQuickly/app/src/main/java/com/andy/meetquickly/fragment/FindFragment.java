package com.andy.meetquickly.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.find.MomentInteractionActivity;
import com.andy.meetquickly.activity.find.PublishMomentActivity;
import com.andy.meetquickly.base.BaseFragment;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.fragment.find.EventFragment;
import com.andy.meetquickly.fragment.find.FocusFragment;
import com.andy.meetquickly.fragment.find.NearFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.screenMomentMessage;
import com.andy.meetquickly.message.screenShopMessage;
import com.appyvet.materialrangebar.RangeBar;
import com.flyco.tablayout.SlidingScaleTabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/19  15:26
 * 描述：     发现
 */
public class FindFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.tablayout)
    SlidingScaleTabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    @BindView(R.id.iv_screen)
    ImageView ivScreen;
    Unbinder unbinder1;
    UserBean userBean;
    private int[] colors = {
            Color.RED, Color.WHITE, Color.CYAN
    };
    private String[] titles = {
            "附近", "活动", "关注"
    };
    private Badge badge;
    private int screenSex = 0;


    public FindFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    protected void initView() {
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();
        userBean = UserCache.getInstance().getUser();
        List<Fragment> fragments = new ArrayList<>();
        NearFragment fragment1 = new NearFragment();
        EventFragment fragment2 = new EventFragment();
        FocusFragment fragment3 = new FocusFragment();
//        FindPeoplePlayFragment fragment2 = FindPeoplePlayFragment.newInstance(18,2);
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        FragmentManager fm = getChildFragmentManager();
//        FragmentManager fm = FindFragment.this.getActivity().getSupportFragmentManager();
        viewpager.setAdapter(new MyFragmentPagerAdapt(fm, fragments));
        viewpager.setOffscreenPageLimit(1);
        tablayout.setViewPager(viewpager);
        viewpager.setCurrentItem(0);
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
    }

    @Override
    protected void initData() {
        MQApi.getUnreadMessageTotal(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<Integer> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<Integer>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<Integer>>(){});
                if (myApiResult.isOk()) {
                    if (myApiResult.getData() != 0){
                        badge = new QBadgeView(FindFragment.this.getContext()).bindTarget(ivMessage).setBadgeNumber(myApiResult.getData());
//                        badge.hide(false);
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.iv_message, R.id.tv_publish,R.id.iv_screen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_message:
                MomentInteractionActivity.start(FindFragment.this.getContext());
                if (badge != null) {
                    badge.hide(true);
                }
                break;
            case R.id.tv_publish:
                PublishMomentActivity.start(FindFragment.this.getContext());
//                YuYueMeetPeopleActivity.start(FindFragment.this.getContext());
                break;
            case R.id.iv_screen:
                showScreenDialog();
                break;
        }
    }

    private Dialog mScreenMomentDialog;

    private void showScreenDialog() {
        if (null == mScreenMomentDialog){
            mScreenMomentDialog = new Dialog(FindFragment.this.getContext(), R.style.dialog_bottom_full);
            mScreenMomentDialog.setCanceledOnTouchOutside(true); //手指触碰到外界取消
            mScreenMomentDialog.setCancelable(true);             //可取消 为true
            Window window = mScreenMomentDialog.getWindow();      // 得到dialog的窗体
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.share_animation);
            View view = View.inflate(FindFragment.this.getContext(), R.layout.dialog_screen_moment, null); //获取布局视图
            ImageView imageViewCancel = view.findViewById(R.id.iv_cancel);
            imageViewCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mScreenMomentDialog != null) {
                        mScreenMomentDialog.dismiss();
                    }
                }
            });

            RadioButton radioButton0 = view.findViewById(R.id.radioButton0);
            radioButton0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        screenSex = 0;
                    }
                }
            });
            RadioButton radioButton1 = view.findViewById(R.id.radioButton1);
            radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        screenSex = 2;
                    }
                }
            });
            RadioButton radioButton2 = view.findViewById(R.id.radioButton2);
            radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        screenSex = 1;
                    }
                }
            });
            TextView textViewSure = view.findViewById(R.id.textViewSure);
            textViewSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EventBus.getDefault().post(new screenMomentMessage(screenSex));

                    if (mScreenMomentDialog != null) {
                        mScreenMomentDialog.dismiss();
                    }

                }
            });

            window.setContentView(view);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
        }
        mScreenMomentDialog.show();
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
    public void onDestroyView() {
        super.onDestroyView();
    }
}

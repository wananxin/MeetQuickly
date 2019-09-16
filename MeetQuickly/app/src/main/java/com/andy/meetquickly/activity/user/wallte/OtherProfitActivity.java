package com.andy.meetquickly.activity.user.wallte;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.fragment.profit.CouponProfitFragment;
import com.andy.meetquickly.fragment.profit.OtherProfitFragment;
import com.andy.meetquickly.fragment.profit.WithProfitFragment;
import com.flyco.tablayout.SlidingScaleTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherProfitActivity extends BaseActivity {

    @BindView(R.id.tablayout)
    SlidingScaleTabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private String[] titles = {
            "礼物收益", "代金券收益"
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_other_profit;
    }

    @Override
    public void initView() {
        setBarTitle("其他收益");
        List<Fragment> fragments = new ArrayList<>();
        OtherProfitFragment fragment1 = OtherProfitFragment.newInstance(1);
        CouponProfitFragment fragment2 = CouponProfitFragment.newInstance(2);
        fragments.add(fragment1);
        fragments.add(fragment2);
        FragmentManager fm = getSupportFragmentManager();
        viewpager.setAdapter(new MyFragmentPagerAdapt(fm, fragments));
        viewpager.setOffscreenPageLimit(3);
        tablayout.setViewPager(viewpager);
        viewpager.setCurrentItem(0);
    }

    @Override
    public void initData() {

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

    public static void start(Context context) {
        Intent starter = new Intent(context, OtherProfitActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

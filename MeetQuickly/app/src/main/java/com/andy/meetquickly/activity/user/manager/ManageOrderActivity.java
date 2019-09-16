package com.andy.meetquickly.activity.user.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.user.OrderActivity;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.fragment.order.ManageOrderCancelFragment;
import com.andy.meetquickly.fragment.order.ManageOrderFinishFragment;
import com.andy.meetquickly.fragment.order.ManageOrderStartFragment;
import com.andy.meetquickly.fragment.order.OrderFinishFragment;
import com.andy.meetquickly.fragment.order.OrderStartFragment;
import com.flyco.tablayout.SlidingScaleTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageOrderActivity extends BaseActivity {

    @BindView(R.id.tablayout)
    SlidingScaleTabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private String[] titles = {
            "进行中", "已完成", "已取消"
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_manage_order;
    }

    @Override
    public void initView() {
        setBarTitle("我的接单");
        List<Fragment> fragments = new ArrayList<>();
        ManageOrderStartFragment fragment1 = new ManageOrderStartFragment();
        ManageOrderFinishFragment fragment2 = new ManageOrderFinishFragment();
        ManageOrderCancelFragment fragment3 = new ManageOrderCancelFragment();
//        FindPeoplePlayFragment fragment2 = FindPeoplePlayFragment.newInstance(18,2);
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        FragmentManager fm = getSupportFragmentManager();
        viewpager.setAdapter(new MyFragmentPagerAdapt(fm,fragments));
        viewpager.setOffscreenPageLimit(3);
        tablayout.setViewPager(viewpager);
        viewpager.setCurrentItem(0);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
        Intent starter = new Intent(context, ManageOrderActivity.class);
        context.startActivity(starter);
    }

}

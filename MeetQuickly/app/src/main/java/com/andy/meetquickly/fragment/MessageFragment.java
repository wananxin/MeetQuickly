package com.andy.meetquickly.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.message.FriendSearchActivity;
import com.andy.meetquickly.base.BaseFragment;
import com.andy.meetquickly.fragment.message.FriendFragment;
import com.andy.meetquickly.fragment.message.UserMessageFragment;
import com.flyco.tablayout.SlidingScaleTabLayout;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/19  15:28
 * 描述：     消息
 */
public class MessageFragment extends BaseFragment {
    @BindView(R.id.tablayout)
    SlidingScaleTabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    // TODO: Rename parameter arguments, choose names that match
    private int[] colors = {
            Color.RED, Color.CYAN
    };
    private String[] titles = {
            "消息", "好友"
    };


    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void initView() {
        super.initView();
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();
        List<Fragment> fragments = new ArrayList<>();
        UserMessageFragment fragment1 = new UserMessageFragment();
        FriendFragment fragment2 = new FriendFragment();
        fragments.add(fragment1);
        fragments.add(fragment2);
        FragmentManager fm = getChildFragmentManager();
        viewpager.setAdapter(new MyFragmentPagerAdapt(fm, fragments));
        viewpager.setOffscreenPageLimit(2);
        tablayout.setViewPager(viewpager);
        viewpager.setCurrentItem(0);
    }

    @OnClick(R.id.iv_serach)
    public void onViewClicked() {
        FriendSearchActivity.start(MessageFragment.this.getContext());
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

}

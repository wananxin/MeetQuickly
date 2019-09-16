package com.andy.meetquickly.activity.user.partner;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.user.wallte.WithProfitActivity;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.TeamGoalsBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.fragment.coupon.CouponTypeFragment;
import com.andy.meetquickly.fragment.partner.PartnerTypeFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.flyco.tablayout.SlidingScaleTabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PartnerActivity extends BaseActivity {

    @BindView(R.id.iv_user_head)
    ImageView ivUserHead;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_type)
    TextView tvUserType;
    @BindView(R.id.tv_partner_people)
    TextView tvPartnerPeople;
    @BindView(R.id.tablayout)
    SlidingScaleTabLayout tablayout;
    @BindView(R.id.tv_sck)
    TextView tvSck;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private String[] titles = {
//            "全部", "直推", "一度", "二度"
            "全部", "直推", "一度"
    };
    private UserBean userBean;
    private TeamGoalsBean teamGoalsBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_partner;
    }

    @Override
    public void initView() {
        setBarTitle("合伙人");
        setRightTextAndClick("收益", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WithProfitActivity.start(mContext);
            }
        });
        userBean = UserCache.getInstance().getUser();
        ImageLoaderUtil.getInstance().loadCircleImage(mContext,userBean.getFace(),ivUserHead);
        tvUserName.setText(userBean.getNickName());
        List<Fragment> fragments = new ArrayList<>();
        PartnerTypeFragment fragment1 = PartnerTypeFragment.newInstance(0);
        PartnerTypeFragment fragment2 = PartnerTypeFragment.newInstance(1);
        PartnerTypeFragment fragment3 = PartnerTypeFragment.newInstance(2);
//        PartnerTypeFragment fragment4 = PartnerTypeFragment.newInstance(3);
//        FindPeoplePlayFragment fragment2 = FindPeoplePlayFragment.newInstance(18,2);
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
//        fragments.add(fragment4);
        FragmentManager fm = getSupportFragmentManager();
        viewpager.setAdapter(new MyFragmentPagerAdapt(fm, fragments));
        viewpager.setOffscreenPageLimit(3);
        tablayout.setViewPager(viewpager);
        viewpager.setCurrentItem(0);
//        initData();
    }

    @OnClick({R.id.iv_user_head, R.id.tv_sck})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_user_head:
                RxToast.showToast("用户头衔");
                break;
            case R.id.tv_sck:
                RxToast.showToast("素材库");
                break;
        }
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
        showWaitDialog(PartnerActivity.this);
        MQApi.getUserTeamGoalsInfo(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<TeamGoalsBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<TeamGoalsBean>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<TeamGoalsBean>>(){});
                if (myApiResult.isOk()) {
                    teamGoalsBean = myApiResult.getData();
                    tvUserType.setText("(" + teamGoalsBean.getGroupName() + ")");
                    tvPartnerPeople.setText(teamGoalsBean.getResult());
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PartnerActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.utils.StringUtil;
import com.vondear.rxtool.view.RxToast;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class SafeActivity extends BaseActivity {

    private UserBean userBean;
    @Override
    public int getLayoutId() {
        return R.layout.activity_safe;
    }

    @Override
    public void initView() {
        setBarTitle("账号与安全");
        userBean = UserCache.getInstance().getUser();
    }

    @Override
    public void initData() {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SafeActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_xgmm, R.id.rl_ssmm, R.id.rl_txmm, R.id.rl_zjxx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_xgmm:
                //修改密码
                UpdateUserPsdActivity.start(mContext);
                break;
            case R.id.rl_ssmm:
                //手势密码
                userBean = UserCache.getInstance().getUser();
                if (StringUtil.isNotNull(userBean.getGesturesPassword())){
                    GestureActivity.start(mContext,0,"修改手势密码","请输入旧手势密码","");
                } else {
                    GestureActivity.start(mContext, 1, "设置手势密码","绘制解锁图案", "");
                }
                break;
            case R.id.rl_txmm:
                //提现密码
                UpdateCashPsdActivity.start(mContext);
                break;
            case R.id.rl_zjxx:
                //证件信息
                userBean = UserCache.getInstance().getUser();
                //实名认证
                if ("1".equals(userBean.getIsCertification())){
                    RxToast.showToast("正在审核...请稍后");
                } else {
                    UserAuthenticationActivity.start(mContext);
                }
                break;
        }
    }
}

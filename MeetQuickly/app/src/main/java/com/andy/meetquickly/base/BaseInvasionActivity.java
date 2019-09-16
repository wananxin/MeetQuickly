package com.andy.meetquickly.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.andy.meetquickly.Interf.BaseViewInterface;
import com.andy.meetquickly.R;
import com.andy.meetquickly.common.AppManager;
import com.gyf.barlibrary.ImmersionBar;
import com.kongzue.dialog.v2.WaitDialog;
import com.vondear.rxtool.RxActivityTool;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/18 17:46
 * 描述：
 */
public abstract class BaseInvasionActivity extends SwipeBackActivity implements BaseViewInterface {

    protected ImmersionBar mImmersionBar;
    public static Context mContext;
    public Unbinder unbinder;
    public abstract int getLayoutId();
    public abstract void initView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(true);
        AppManager.getAppManager().addActivity(this);
        RxActivityTool.addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
        RxActivityTool.finishActivity();
        AppManager.getAppManager().finishActivity(this);
    }

    public void showWaitDialog(Activity activity){
        WaitDialog.show(activity, "载入中...");
    }

    public void showWaitDialog(Activity activity,String msg){
        WaitDialog.show(activity, msg);
    }

    public void dissmissWaitDialog(){
        WaitDialog.dismiss();
    }

    public void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

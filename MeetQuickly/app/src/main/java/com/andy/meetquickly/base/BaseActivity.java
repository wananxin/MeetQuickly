package com.andy.meetquickly.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.Interf.BaseViewInterface;
import com.andy.meetquickly.R;
import com.andy.meetquickly.common.AppManager;
import com.andy.meetquickly.utils.KeyboardUtils;
import com.andy.meetquickly.utils.LogUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.kongzue.dialog.v2.WaitDialog;
import com.vondear.rxtool.RxActivityTool;
import com.vondear.rxtool.RxKeyboardTool;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/18 17:46
 * 描述：
 */
public abstract class BaseActivity extends SwipeBackActivity implements BaseViewInterface {

    protected ImmersionBar mImmersionBar;
    protected Context mContext;
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
        mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        unbinder = ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
//        KeyboardUtils.hideSoftInput(this);
        RxKeyboardTool.hideSoftInput(this);
        super.onDestroy();
        unbinder.unbind();
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
//        RxActivityTool.finishActivity();
//        AppManager.getAppManager().finishActivity(this);
    }


    public void setBarTitle(String title){
        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxKeyboardTool.hideSoftInput((Activity) mContext);
                finish();
            }
        });
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(title);
    }

    public void setRightTextAndClick(String text, View.OnClickListener onClickListener){
        TextView tvRightText = (TextView) findViewById(R.id.tv_right_text_button);
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText(text);
        tvRightText.setOnClickListener(onClickListener);
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

    @Override
    protected void onPause() {
        RxKeyboardTool.hideSoftInput((Activity) mContext);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

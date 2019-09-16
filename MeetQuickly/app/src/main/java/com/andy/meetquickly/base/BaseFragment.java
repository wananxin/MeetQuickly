package com.andy.meetquickly.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andy.meetquickly.R;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.SimpleImmersionFragment;
import com.kongzue.dialog.v2.WaitDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/18 17:51
 * 描述：
 */
public abstract class BaseFragment extends SimpleImmersionFragment {
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    protected Activity mActivity;
    protected View mRootView;

    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
//        ImmersionBar.setTitleBar(mActivity, toolbar);
        initView();
        initData();
        setListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * Gets layout id.
     *
     * @return the layout id
     */
    protected abstract int getLayoutId();


    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true).init();
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * view与数据绑定
     */
    protected void initView() {

    }

    /**
     * 设置监听
     */
    protected void setListener() {

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
}

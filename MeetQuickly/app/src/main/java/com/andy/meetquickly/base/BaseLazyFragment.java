package com.andy.meetquickly.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.SimpleImmersionFragment;
import com.kongzue.dialog.v2.WaitDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/18 17:51
 * 描述：
 */
public abstract class BaseLazyFragment extends Fragment {


    protected boolean isViewInitiated = false;
    protected boolean isVisibleToUser = false;
    protected boolean isDataInitiated = false;

    View view;

    public BaseLazyFragment() {
        super();
    }

    /**
     * @param isVisibleToUser 注意：setUserVisibleHint()方法是在onCreate()方法、
     *                        甚至是onAttach()方法之前调用的，所以它里面不能有任何对Context上下文对象的调用！
     *                        getUserVisibleHint()返回的变量，是App提供的代表此Fragment当前对于用户是否可见的一个布尔值；
     *                        <p>
     *                        setUserVisibleHint()在Fragment创建时会先被调用一次，传入isVisibleToUser = false
     *                        如果当前Fragment可见，那么setUserVisibleHint()会再次被调用一次，传入isVisibleToUser = true
     *                        如果Fragment从可见->不可见，那么setUserVisibleHint()也会被调用，传入isVisibleToUser = false
     *                        总结：setUserVisibleHint()除了Fragment的可见状态发生变化时会被回调外，在new Fragment()时也会被回调
     *                        如果我们需要在 Fragment 可见与不可见时干点事，用这个的话就会有多余的回调了，那么就需要重新封装一个
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData(false);//false:获取数据一次；true：每次可见时都会强制获取数据
        prepareDoSomeThing();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isViewInitiated = true;

        //防止事件穿透
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        prepareFetchData(false);
        prepareDoSomeThing();

    }


    public abstract void fetchData();//获取数据，执行一次

    public abstract void doItEvery();//每次进入页面可见时都会执行

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

    public boolean prepareDoSomeThing() {
        if (isVisibleToUser && isViewInitiated) {
            doItEvery();
            return true;
        }
        return false;
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

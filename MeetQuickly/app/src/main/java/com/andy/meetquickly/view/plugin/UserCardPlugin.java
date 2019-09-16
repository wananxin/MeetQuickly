package com.andy.meetquickly.view.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.message.SelectFriendActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/9 14:30
 * 描述：
 */
public class UserCardPlugin implements IPluginModule {


    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.ic_plugin_card);
    }

    @Override
    public String obtainTitle(Context context) {
        return "名片";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        SelectFriendActivity.start((Activity) rongExtension.getContext());
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}



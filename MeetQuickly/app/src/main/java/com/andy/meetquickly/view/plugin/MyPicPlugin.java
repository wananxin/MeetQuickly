package com.andy.meetquickly.view.plugin;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.andy.meetquickly.R;

import io.rong.imkit.plugin.ImagePlugin;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/9 19:00
 * 描述：
 */
public class MyPicPlugin extends ImagePlugin {

    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.ic_plugin_pic);
    }

    @Override
    public String obtainTitle(Context context) {
        return "相册";
    }
}

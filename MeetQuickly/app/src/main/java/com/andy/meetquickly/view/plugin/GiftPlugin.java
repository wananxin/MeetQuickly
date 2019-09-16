package com.andy.meetquickly.view.plugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.andy.meetquickly.R;
import com.andy.meetquickly.message.showGiftMessage;
import com.andy.meetquickly.message.updatePicMessage;

import org.greenrobot.eventbus.EventBus;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/9 14:30
 * 描述：
 */
public class GiftPlugin implements IPluginModule {


    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.ic_plugin_gift);
    }

    @Override
    public String obtainTitle(Context context) {
        return "礼物";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        EventBus.getDefault().post(new showGiftMessage(""));
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}



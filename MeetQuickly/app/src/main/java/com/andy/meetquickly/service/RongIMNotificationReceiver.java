package com.andy.meetquickly.service;

import android.content.Context;

import com.andy.meetquickly.utils.LogUtil;

import java.util.logging.Logger;

import io.rong.push.PushType;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/6/21 16:43
 * 描述：
 */
public class RongIMNotificationReceiver extends PushMessageReceiver {

    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        return false;
    }

    /**
     * 第三方push状态回调
     *
     * @param pushType   push类型
     * @param action     当前的操作，连接或者获取token
     * @param resultCode 返回的错误码
     */
    @Override
    public void onThirdPartyPushState(PushType pushType, String action, long resultCode) {
        super.onThirdPartyPushState(pushType, action, resultCode);
        LogUtil.e("RongLog" +":"+ action + ":" + resultCode + ":" + pushType.getName());
    }


}
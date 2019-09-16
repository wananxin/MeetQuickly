package com.andy.meetquickly.view;

import android.content.Context;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.activity.home.UserDataActivity;
import com.andy.meetquickly.bean.MessageCardBean;
import com.andy.meetquickly.view.plugin.UserCardMessage;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/7/1 18:10
 * 描述：
 */
public class MyConversationClickListener implements RongIM.ConversationClickListener {
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
        UserDataActivity.start(context, userInfo.getUserId());
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        if ("RCD:CardMsg".equals(message.getObjectName())) {
            UserCardMessage userCardMessage = (UserCardMessage) message.getContent();
            MessageCardBean messageCardBean = new MessageCardBean();
            messageCardBean = JSON.parseObject(userCardMessage.getExtra(), MessageCardBean.class);
            UserDataActivity.start(context, messageCardBean.getUid());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }
}

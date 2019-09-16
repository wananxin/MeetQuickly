package com.andy.meetquickly.view.plugin;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.UserDataActivity;
import com.andy.meetquickly.bean.MeesageGiftBean;
import com.andy.meetquickly.bean.MessageCardBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/9 14:34
 * 描述：
 */
@ProviderTag(
        messageContent = UserCardMessage.class
)
public class UserCardItemProvider extends IContainerItemProvider.MessageProvider<UserCardMessage> {

    public Context context;

    public UserCardItemProvider() {

    }

    @Override
    public void bindView(View view, int i, UserCardMessage apkMessage, UIMessage message) {
//根据需求，适配数据
        ViewHolder holder = (ViewHolder) view.getTag();

        try {
            MessageCardBean messageCardBean = new MessageCardBean();
            messageCardBean = JSON.parseObject(apkMessage.getExtra(),MessageCardBean.class);
            holder.tvUserName.setText(messageCardBean.getNickName());
            holder.tvUserNumb.setText("YP号："+messageCardBean.getUid());
            MessageCardBean finalMessageCardBean = messageCardBean;
            ImageLoaderUtil.getInstance().loadCircleImage(context,messageCardBean.getFace(),holder.ivUserHead);
            if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
                //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
            } else {
                //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
            }
        }catch (Exception e){

        }



    }

    @Override
    public Spannable getContentSummary(UserCardMessage userCardMessage) {

        return new SpannableString(userCardMessage.getContent());
    }

    @Override
    public void onItemClick(View view, int i, UserCardMessage userCardMessage, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        //这就是展示在会话界面的自定义的消息的布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommend_friend, null);
        ViewHolder holder = new ViewHolder();
        holder.tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        holder.tvUserNumb = (TextView) view.findViewById(R.id.tv_user_numb);
        holder.ivUserHead = view.findViewById(R.id.iv_user_head);
        view.setTag(holder);
        this.context = context;
        return view;
    }

    private static class ViewHolder {
        TextView tvUserName, tvUserNumb;
        ImageView ivUserHead;
    }
}

package com.andy.meetquickly.view.plugin;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.MomentMediaAdapter;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.andy.meetquickly.utils.LogUtil;
import com.andy.meetquickly.bean.MeesageGiftBean;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/9 14:34
 * 描述：
 */
@ProviderTag(
        messageContent = GiftMessage.class
)
public class GiftItemProvider extends IContainerItemProvider.MessageProvider<GiftMessage> {

    public Context context;

    public GiftItemProvider() {

    }

    @Override
    public void bindView(View view, int i, GiftMessage apkMessage, UIMessage message) {
//根据需求，适配数据
        ViewHolder holder = (ViewHolder) view.getTag();

        try {
            MeesageGiftBean meesageGiftBean = new MeesageGiftBean();
            meesageGiftBean = JSON.parseObject(apkMessage.getExtra(),MeesageGiftBean.class);
            holder.tvGiftNumb.setText("x" + meesageGiftBean.getGiftNum());
            holder.tvGiftName.setText(meesageGiftBean.getGiftData().getName());
            ImageLoaderUtil.getInstance().loadImage(context,meesageGiftBean.getGiftData().getUrl(),holder.ivGift);
            if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
                //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
                message.getSenderUserId();
                message.getTargetId();
                holder.tvUserName.setText( "您向"+RongUserInfoManager.getInstance().getUserInfo(message.getTargetId()).getName()+"赠送了");
            } else {
                //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
                holder.tvUserName.setText(RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId()).getName()+"向您赠送了");
            }
        }catch (Exception e){

        }



    }

    @Override
    public Spannable getContentSummary(GiftMessage giftMessage) {
        return new SpannableString(giftMessage.getContent());
    }

    @Override
    public void onItemClick(View view, int i, GiftMessage giftMessage, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        //这就是展示在会话界面的自定义的消息的布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_gift, null);
        ViewHolder holder = new ViewHolder();
        holder.tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        holder.tvGiftName = (TextView) view.findViewById(R.id.tv_gift_name);
        holder.tvGiftNumb = (TextView) view.findViewById(R.id.tv_gift_numb);
        holder.ivGift = view.findViewById(R.id.iv_gift);
        view.setTag(holder);
        this.context = context;
        return view;
    }

    private static class ViewHolder {
        TextView tvUserName, tvGiftName, tvGiftNumb;
        ImageView ivGift;
    }
}

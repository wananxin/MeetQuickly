package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.UserFriendBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class MyFraiendSimpleAdapter extends BaseQuickAdapter<UserFriendBean, BaseViewHolder> {

    private Context context;
    private int type;
    //1,为新的朋友，时间隐藏，显示按钮，文字为同意
    //2，为我的关注，时间隐藏，按钮显示，文字为取消
    //3，为我的粉丝，时间隐藏，按钮隐藏
    //4，为管理网店，时间隐藏，按钮显示，文字为解绑

    public MyFraiendSimpleAdapter(Context context, @Nullable List<UserFriendBean> data, int type) {
        super(R.layout.item_invitation_people, data);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserFriendBean item) {
        helper.setText(R.id.tv_name, item.getNickName())
                .setText(R.id.tv_age, String.valueOf(item.getAge()));
        if (type == 1) {
            helper.setVisible(R.id.tv_time, false)
                    .setVisible(R.id.tv_button, true)
                    .setText(R.id.tv_button, "同意")
                    .addOnClickListener(R.id.tv_button);
        } else if (type == 2) {
            helper.setVisible(R.id.tv_time, false)
                    .setVisible(R.id.tv_button, true)
                    .setText(R.id.tv_button, "取消")
                    .addOnClickListener(R.id.tv_button);
        } else if (type == 3) {
            helper.setVisible(R.id.tv_time, false)
                    .setVisible(R.id.tv_button, false)
                    .setText(R.id.tv_button, "取消")
                    .addOnClickListener(R.id.tv_button);
        } else if (type == 4) {
            helper.setVisible(R.id.tv_time, false)
                    .setVisible(R.id.tv_button, true)
                    .setText(R.id.tv_button, "解绑")
                    .addOnClickListener(R.id.tv_button);
        }

        ImageView imageView = helper.getView(R.id.iv_user_head);
        ImageLoaderUtil.getInstance().loadCircleImage(context, item.getFace(), imageView);
        ImageView ivSex = helper.getView(R.id.iv_sex);
        if ("1".equals(item.getSex())) {
            ivSex.setImageResource(R.mipmap.ic_men);
        } else {
            ivSex.setImageResource(R.mipmap.ic_women);
        }
    }
}

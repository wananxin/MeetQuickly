package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

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
public class MyFraiendAdapter extends BaseQuickAdapter<UserFriendBean,BaseViewHolder> {

    private Context context;


    public MyFraiendAdapter(Context context,@Nullable List<UserFriendBean> data) {
        super(R.layout.item_user_friend, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserFriendBean item) {
        helper.setText(R.id.tv_username,item.getNickName())
                .setText(R.id.tv_age,String.valueOf(item.getAge()))
        .addOnClickListener(R.id.iv_user_head);
        ImageView imageView = helper.getView(R.id.iv_user_head);
        ImageLoaderUtil.getInstance().loadCircleImage(context,item.getFace(),imageView);
        ImageView ivSex = helper.getView(R.id.iv_sex);
        if ("1".equals(item.getSex())){
            ivSex.setImageResource(R.mipmap.ic_men);
        }else {
            ivSex.setImageResource(R.mipmap.ic_women);
        }
    }
}

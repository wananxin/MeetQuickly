package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.CouponBean;
import com.andy.meetquickly.bean.UserBean;
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
public class UserVisitAdapter extends BaseQuickAdapter<UserFriendBean, BaseViewHolder> {

    private Context context;
    private String mID;
    public UserVisitAdapter(@Nullable List<UserFriendBean> data , Context context,String mID) {
        super(R.layout.item_user_visit, data);
        this.context = context;
        this.mID = mID;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserFriendBean item) {
        ImageView imageViewHead = helper.getView(R.id.iv_user_head);
        ImageLoaderUtil.getInstance().loadCircleImage(context,item.getFace(),imageViewHead);
        ImageView imageViewSex = helper.getView(R.id.iv_sex);
        if ("1".equals(item.getSex())){
            imageViewSex.setImageResource(R.mipmap.ic_men);
        }else {
            imageViewSex.setImageResource(R.mipmap.ic_women);
        }
        helper.setText(R.id.tv_username,item.getNickName())
                .setText(R.id.tv_age,String.valueOf(item.getAge()));
        TextView textView = helper.getView(R.id.textViewGuanzhu);
        if ("1".equals(item.getIsAttention())){
            textView.setText("已关注");
        }else {
            textView.setText("关注");
        }

        if (item.getUid().equals(mID)){
            textView.setVisibility(View.GONE);
        }else {
            textView.setVisibility(View.VISIBLE);
        }

        helper.addOnClickListener(R.id.textViewGuanzhu);
    }
}

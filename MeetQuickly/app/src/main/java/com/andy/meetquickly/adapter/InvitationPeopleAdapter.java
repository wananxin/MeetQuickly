package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.UserBaseBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class InvitationPeopleAdapter extends BaseQuickAdapter<UserBaseBean,BaseViewHolder> {

    private Context context;
    public InvitationPeopleAdapter(@Nullable List<UserBaseBean> data , Context context) {
        super(R.layout.item_invitation_people, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBaseBean item) {
        helper.setText(R.id.tv_name,item.getNickName())
                .setText(R.id.tv_age,String.valueOf(item.getAge()))
                .setText(R.id.tv_time,item.getRegistTime())
                .addOnClickListener(R.id.iv_user_head);

        ImageView imageViewHead = helper.getView(R.id.iv_user_head);
        ImageView imageViewSex = helper.getView(R.id.iv_sex);

        ImageLoaderUtil.getInstance().loadCircleImage(context,item.getFace(),imageViewHead);
        if ("1".equals(item.getSex())){
            imageViewSex.setImageResource(R.mipmap.ic_men);
        }else {
            imageViewSex.setImageResource(R.mipmap.ic_women);
        }
    }
}

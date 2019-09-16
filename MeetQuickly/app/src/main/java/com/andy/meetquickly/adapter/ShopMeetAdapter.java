package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class ShopMeetAdapter extends BaseQuickAdapter<UserBean,BaseViewHolder> {


    private Context context;
    public ShopMeetAdapter(@Nullable List<UserBean> data, Context context) {
        super(R.layout.item_shop_meet_people, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBean item) {
        ImageView iv_head = helper.getView(R.id.iv_head);
        ImageView iv_sex = helper.getView(R.id.iv_sex);
        helper.setText(R.id.tv_name,item.getNickName())
                .setText(R.id.tv_age,item.getAge()+"")
                .setText(R.id.tv_level,"LV"+item.getLevel())
                .setText(R.id.tv_numb,item.getHeight()+"cm   "+item.getWeight()+"kg");
        ImageLoaderUtil.getInstance().loadRoundImage(context,item.getFace(),iv_head,20);
        if ("1".equals(item.getSex())){
            iv_sex.setImageResource(R.mipmap.ic_men);
        }else if ("2".equals(item.getSex())){
            iv_sex.setImageResource(R.mipmap.ic_women);
        }else {
            iv_sex.setImageResource(R.mipmap.ic_women);
        }


    }
}

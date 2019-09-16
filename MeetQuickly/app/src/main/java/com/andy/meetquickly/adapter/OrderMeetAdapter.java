package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.OrderDetailBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class OrderMeetAdapter extends BaseQuickAdapter<OrderDetailBean.UserInfoBean,BaseViewHolder> {

    private Context context;
    public OrderMeetAdapter(@Nullable List<OrderDetailBean.UserInfoBean> data, Context context) {
        super(R.layout.item_order_meet, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailBean.UserInfoBean item) {
        helper.setText(R.id.tv_name,item.getNickName())
                .setText(R.id.tv_age,String.valueOf(item.getAge()));
        ImageView imageViewPic = helper.getView(R.id.iv_pic);
        ImageLoaderUtil.getInstance().loadImage(context,item.getFace(),imageViewPic);
        ImageView imageViewSex = helper.getView(R.id.iv_sex);
        if ("1".equals(item.getSex())){
            imageViewSex.setImageResource(R.mipmap.ic_men);
        }else {
            imageViewSex.setImageResource(R.mipmap.ic_women);
        }
    }
}

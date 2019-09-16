package com.andy.meetquickly.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.GiftBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class GiftSelectAdapter extends BaseQuickAdapter<GiftBean,BaseViewHolder> {

    public Context context;
    public GiftSelectAdapter(@Nullable List<GiftBean> data,Context context) {
        super(R.layout.item_select_gift, data);
        this.context = context;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder helper, GiftBean item) {

        helper.setText(R.id.tv_gift_price,item.getMoney()+"换币")
                .setText(R.id.tv_gift_name,item.getName());
        ImageView imageView = helper.getView(R.id.iv_gift);
        ImageLoaderUtil.getInstance().loadImage(context,item.getUrl(),imageView);
        ConstraintLayout constraintLayout = helper.getView(R.id.constraint);
        if (item.isSelect()){
            constraintLayout.setBackgroundColor(Color.parseColor("#eeeeee"));
        }else {
            constraintLayout.setBackgroundColor(Color.WHITE);
        }

    }
}

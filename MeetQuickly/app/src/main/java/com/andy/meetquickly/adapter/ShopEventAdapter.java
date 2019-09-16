package com.andy.meetquickly.adapter;

import android.support.annotation.Nullable;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.ShopEventBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class ShopEventAdapter extends BaseQuickAdapter<ShopEventBean,BaseViewHolder> {

    public ShopEventAdapter(@Nullable List<ShopEventBean> data) {
        super(R.layout.item_shop_event, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopEventBean item) {
        helper.setText(R.id.tv_content,item.getName())
                .setText(R.id.tv_time,"活动期限："+item.getStartTime()+"-"+item.getEndTime());


    }
}

package com.andy.meetquickly.adapter;

import android.support.annotation.Nullable;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.ShopPlaceBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class ShopPlaceAdapter extends BaseQuickAdapter<ShopPlaceBean,BaseViewHolder> {

    public ShopPlaceAdapter(@Nullable List<ShopPlaceBean> data) {
        super(R.layout.item_shop_place, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopPlaceBean item) {
        helper.setText(R.id.tv_name,item.getCompanyName());
    }
}

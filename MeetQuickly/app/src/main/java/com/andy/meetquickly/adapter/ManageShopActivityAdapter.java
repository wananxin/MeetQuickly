package com.andy.meetquickly.adapter;

import android.support.annotation.Nullable;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.UserShopDetailBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class ManageShopActivityAdapter extends BaseQuickAdapter<UserShopDetailBean.StoreDiscountsTypeBean,BaseViewHolder> {

    public ManageShopActivityAdapter(@Nullable List<UserShopDetailBean.StoreDiscountsTypeBean> data) {
        super(R.layout.item_manage_event, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserShopDetailBean.StoreDiscountsTypeBean item) {

        helper.setText(R.id.tv_time,item.getStartTime() + "至" + item.getEndTime())
                .setText(R.id.tv_content,item.getName());

    }
}

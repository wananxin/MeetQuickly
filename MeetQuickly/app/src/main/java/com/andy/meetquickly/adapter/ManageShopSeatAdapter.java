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
public class ManageShopSeatAdapter extends BaseQuickAdapter<UserShopDetailBean.StoreTableTypeBean,BaseViewHolder> {

    public ManageShopSeatAdapter(@Nullable List<UserShopDetailBean.StoreTableTypeBean> data) {
        super(R.layout.item_seat_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserShopDetailBean.StoreTableTypeBean item) {
        helper.setText(R.id.tv1,item.getName()+"("+item.getMinPeople()+"~"+item.getMaxPeople()+"人)")
                .setText(R.id.tv2,item.getBoxFee())
                .setText(R.id.tv3,item.getMinCharge())
                .setText(R.id.tv4,item.getRemark());
    }
}

package com.andy.meetquickly.adapter;

import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.UserShopDetailBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class ShopSeatTypeAdapter extends BaseQuickAdapter<UserShopDetailBean.StoreTableTypeBean,BaseViewHolder> {

    private int[] colors = {
            R.mipmap.bg_consume1, R.mipmap.bg_consume2, R.mipmap.bg_consume3, R.mipmap.bg_consume4,R.mipmap.bg_consume5
    };
    public ShopSeatTypeAdapter(@Nullable List<UserShopDetailBean.StoreTableTypeBean> data) {
        super(R.layout.item_shop_seat_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserShopDetailBean.StoreTableTypeBean item) {
        helper.setText(R.id.textViewSeatAndPeople,item.getName()+"("+item.getMinPeople()+"~"+item.getMaxPeople()+")")
                .setText(R.id.textViewBoxFree,"包厢费：￥" + item.getBoxFee())
                .setText(R.id.textViewMinFree,"低 消：￥" + item.getMinCharge())
                .setText(R.id.textViewMenu,item.getRemark());

        int position = helper.getAdapterPosition();
        ConstraintLayout constraintLayout = helper.getView(R.id.constraintLayout);
        constraintLayout.setBackgroundResource(colors[position%5]);


    }
}

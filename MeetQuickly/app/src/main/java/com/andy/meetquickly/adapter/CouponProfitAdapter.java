package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.OrderDetailBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class CouponProfitAdapter extends BaseQuickAdapter<OrderDetailBean,BaseViewHolder> {

    private Context context;
    public CouponProfitAdapter(@Nullable List<OrderDetailBean> data,Context context) {
        super(R.layout.item_coupon_profit, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailBean item) {
        helper.setText(R.id.tv_seat,"（包厢/台号："+item.getTables()+"）")
                .setText(R.id.tv_time,"订单日期:"+item.getTables())
                .setText(R.id.tv_numb,"+"+item.getFinalCashReturn())
                .setText(R.id.textViewOrderNumb,"订单号："+item.getOrderId())
        .addOnClickListener(R.id.tv_button);

        if ("1".equals(item.getSex())){
            helper.setText(R.id.tv_user_name,item.getName()+"先生");
        }else {
            helper.setText(R.id.tv_user_name,item.getName()+"女士");
        }
    }
}

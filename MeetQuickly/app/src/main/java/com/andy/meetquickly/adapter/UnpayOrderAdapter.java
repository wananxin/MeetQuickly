package com.andy.meetquickly.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.UnpayOrderBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class UnpayOrderAdapter extends BaseQuickAdapter<UnpayOrderBean,BaseViewHolder> {

    public UnpayOrderAdapter(@Nullable List<UnpayOrderBean> data) {
        super(R.layout.item_unpay_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UnpayOrderBean item) {
        helper.setText(R.id.textViewMoney,item.getPaymentAmount())
                .setText(R.id.textViewTime,item.getCreateTime())
                .setText(R.id.textViewNumb,"订单号："+item.getOrderId());
        ImageView imageView = helper.getView(R.id.imageViewSelect);
        if (item.isSelect()){
            imageView.setImageResource(R.mipmap.ic_bt_select);
        }else {
            imageView.setImageResource(R.mipmap.ic_bt_unselect);
        }
    }
}

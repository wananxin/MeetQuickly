package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
public class OrderStartAdapter extends BaseQuickAdapter<OrderDetailBean,BaseViewHolder> {

    private Context context;
    public OrderStartAdapter(@Nullable List<OrderDetailBean> data, Context context) {
        super(R.layout.item_order_ing, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailBean item) {
        helper.addOnClickListener(R.id.iv_phone)
                .addOnClickListener(R.id.tv_cancel)
                .addOnClickListener(R.id.tv_ok)
                .addOnClickListener(R.id.iv_navigation)
        .setText(R.id.tv_shop_name,item.getCompanyName())
        .setText(R.id.tv_name,"("+item.getStoreName()+"经理)")
        .setText(R.id.tv_time,"到店时间："+item.getTargetDate() + " "+item.getReachTime())
        .setText(R.id.tv_numb,"包厢/台号："+item.getTables())
        .setText(R.id.tv_money,"应抵实际消费：￥" + item.getCouponMoney());

        TextView textViewCoupon = helper.getView(R.id.tv_coupon);
        if ("1".equals(item.getUseCoupon())){
            textViewCoupon.setVisibility(View.VISIBLE);
            textViewCoupon.setText("预使用代金券" + item.getCoupon());
        }else {
            textViewCoupon.setVisibility(View.GONE);
        }

        TextView textViewCancel = helper.getView(R.id.tv_cancel);
        TextView textViewOk = helper.getView(R.id.tv_ok);
        if ("1".equals(item.getReachStore())){
            textViewCancel.setVisibility(View.VISIBLE);
            textViewOk.setText("确认到店");
        }else {
            textViewCancel.setVisibility(View.GONE);
            textViewOk.setText("已到店");
        }

        ImageView imageView = helper.getView(R.id.iv_head);
        ImageLoaderUtil.getInstance().loadCircleImage(context,item.getCoverImg(),imageView);
    }
}

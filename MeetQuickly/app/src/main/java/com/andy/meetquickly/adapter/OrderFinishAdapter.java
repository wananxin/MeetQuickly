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
public class OrderFinishAdapter extends BaseQuickAdapter<OrderDetailBean,BaseViewHolder> {

    private Context context;
    public OrderFinishAdapter(@Nullable List<OrderDetailBean> data, Context context) {
        super(R.layout.item_order_end, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailBean item) {
        helper.addOnClickListener(R.id.tv_cancel)
                .addOnClickListener(R.id.tv_ok)
                .setText(R.id.tv_shop_name,item.getCompanyName())
                .setText(R.id.tv_name,"("+item.getStoreName()+"经理)")
                .setText(R.id.tv_time,"到店时间："+item.getTargetDate() + " "+item.getReachTime())
                .setText(R.id.tv_numb,"包厢/台号："+item.getTables())
                .setText(R.id.tv_money,"应抵实际消费：￥" + item.getCouponMoney());

        ImageView imageView = helper.getView(R.id.iv_head);
        ImageLoaderUtil.getInstance().loadCircleImage(context,item.getCoverImg(),imageView);

        TextView textViewCoupon = helper.getView(R.id.tv_coupon);
        if ("2".equals(item.getUseCoupon())){
            textViewCoupon.setVisibility(View.VISIBLE);
            textViewCoupon.setText("代金券已抵现" + item.getCoupon());
        }else {
            textViewCoupon.setVisibility(View.GONE);
        }

        if ("1".equals(item.getIsEvaluate())){
            helper.setText(R.id.tv_ok,"已评价");
        }else {
            helper.setText(R.id.tv_ok,"评价");
        }
    }
}

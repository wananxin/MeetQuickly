package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.OrderDetailBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class ManageOrderAdapter extends BaseQuickAdapter<OrderDetailBean, BaseViewHolder> {

    private Context context;
    private int type;//1为进行中的  2为已完成的  3为取消的

    public ManageOrderAdapter(@Nullable List<OrderDetailBean> data, Context context, int type) {
        super(R.layout.item_order_end, data);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailBean item) {

        helper.addOnClickListener(R.id.tv_cancel)
                .addOnClickListener(R.id.tv_ok)
                .addOnClickListener(R.id.tv_manage_bt)
                .addOnClickListener(R.id.tv_manage_pay)
                .setText(R.id.tv_shop_name, item.getName())
                .setText(R.id.tv_time, "到店时间：" + item.getTargetDate() + " " + item.getReachTime())
                .setText(R.id.tv_numb, "包厢/台号：" + item.getTables())
                .setText(R.id.tv_money, "应抵实际消费：￥" + item.getCouponMoney())
                .setGone(R.id.tv_cancel, false)
                .setGone(R.id.tv_ok, false)
                .setVisible(R.id.tv_manage_bt, true);

        ImageView imageView = helper.getView(R.id.iv_head);
        ImageLoaderUtil.getInstance().loadCircleImage(context, item.getFace(), imageView);
        TextView textViewSex = helper.getView(R.id.tv_name);
        if ("1".equals(item.getSex())) {
            textViewSex.setText("(先生)");
        } else {
            textViewSex.setText("(女生)");
        }
        TextView textViewButton = helper.getView(R.id.tv_manage_bt);
        TextView textViewPay = helper.getView(R.id.tv_manage_pay);
        textViewPay.setVisibility(View.GONE);
        if (type == 1) {
            if ("0".equals(item.getManagerVerify())) {
                textViewButton.setText("上传账单");
            } else {
                textViewButton.setText("已传账单");
            }
            if ("1".equals(item.getIsPayment())) {
                textViewPay.setText("未支付" + item.getPaymentAmount());
                textViewPay.setVisibility(View.VISIBLE);
            } else if ("2".equals(item.getIsPayment())) {
                textViewPay.setVisibility(View.VISIBLE);
                textViewPay.setText("已支付");
            }
        } else {
            textViewButton.setText("删除");
        }

        TextView textViewCoupon = helper.getView(R.id.tv_coupon);
        if (StringUtil.isNotNull(item.getUseCoupon())) {
            textViewCoupon.setVisibility(View.VISIBLE);
            textViewCoupon.setText("使用代金券" + item.getCoupon());
        } else {
            textViewCoupon.setVisibility(View.VISIBLE);
            textViewCoupon.setText("未使用代金券");
        }

        if (type == 1 || type == 3) {
            helper.setGone(R.id.tv_fanli, false);
        } else {
            helper.setGone(R.id.tv_fanli, true);
            if ("2".equals(item.getUseCoupon())) {
                helper.setText(R.id.tv_fanli, "此单代金券收入" + item.getCoupon());
            } else {
                helper.setText(R.id.tv_fanli, "此单代金券收入0");
            }
        }

        if (2 == type) {
            helper.setText(R.id.tv_state, "订单已完成");
        } else if (3 == type) {
            helper.setText(R.id.tv_state, "顾客已取消");
        } else {
            if ("2".equals(item.getReachStore())) {
                helper.setText(R.id.tv_state, "顾客已确认到店");
            } else {
                helper.setText(R.id.tv_state, "待顾客确认到店");
            }
        }
    }
}

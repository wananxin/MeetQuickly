package com.andy.meetquickly.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.CouponBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class WelfareCenterAdapter extends BaseQuickAdapter<CouponBean, BaseViewHolder> {

    private Context context;

    public WelfareCenterAdapter(@Nullable List<CouponBean> data, Context context) {
        super(R.layout.item_welfare_center, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponBean item) {
        ImageView imageView = helper.getView(R.id.imageViewType);
        ImageView imageViewIsSelect = helper.getView(R.id.imageViewIsSelect);

        if ("8".equals(item.getUseScope())) {
            imageView.setImageResource(R.mipmap.bt_kuaibi_bg);
        } else if ("9".equals(item.getUseScope())) {
            imageView.setImageResource(R.mipmap.bt_xianjin_bg);
        } else {
            imageView.setImageResource(R.mipmap.bt_coupon_bg);
        }

        helper.addOnClickListener(R.id.imageViewIsSelect);
        if ("1".equals(item.getIfReceive())) {
            imageViewIsSelect.setImageResource(R.mipmap.ic_welfare_select);
        } else {
            imageViewIsSelect.setImageResource(R.mipmap.ic_welfare_unselect);
        }

        if ("1".equals(item.getUseType())) {
            helper.setText(R.id.textViewTime, "使用日期：永久有效");
        } else {
            helper.setText(R.id.textViewTime, "使用日期：" + item.getBeginTime() + "至" + item.getEndTime());
        }

        helper.setText(R.id.textViewMoney, item.getCouponAmount());

        if ("1".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限商务KTV使用");
        } else if ("2".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限酒吧使用");
        } else if ("3".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限量贩KTV使用");
        } else if ("4".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限清吧使用");
        } else if ("5".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "全平台无限制使用");
        } else if ("6".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限" + item.getCompanyName() + "("+item.getStoreName() +"经理)" + "使用");
        } else if ("7".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限" + item.getCompanyName() + "("+item.getStoreName() +"经理)" + "使用");
        } else if ("8".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "可用于向好友赠送礼物");
        } else if ("9".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "可在钱包中进行提现");
        }
    }
}

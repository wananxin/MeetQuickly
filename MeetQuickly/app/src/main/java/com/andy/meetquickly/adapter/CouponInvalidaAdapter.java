package com.andy.meetquickly.adapter;

import android.support.annotation.Nullable;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.CouponBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class CouponInvalidaAdapter extends BaseQuickAdapter<CouponBean,BaseViewHolder> {

    public CouponInvalidaAdapter(@Nullable List<CouponBean> data) {
        super(R.layout.item_coupon_invalid, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponBean item) {

        helper.setText(R.id.textViewMoney, item.getCouponAmount());
        // 1:商务KTV，2:酒吧，3:量贩KTV，4:清吧 5.无限制 6.固定网店 7.网店领取
        if ("1".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限商务KTV使用");
            helper.setVisible(R.id.textViewMengkan,false);
            helper.setVisible(R.id.textViewStoreName,false);
        } else if ("2".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限酒吧使用");
            helper.setVisible(R.id.textViewMengkan,false);
            helper.setVisible(R.id.textViewStoreName,false);

        } else if ("3".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限量贩KTV使用");
            helper.setVisible(R.id.textViewMengkan,false);
            helper.setVisible(R.id.textViewStoreName,false);

        } else if ("4".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限清吧使用");
            helper.setVisible(R.id.textViewMengkan,false);
            helper.setVisible(R.id.textViewStoreName,false);

        } else if ("5".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "平台内任意网店均可使用");
            helper.setVisible(R.id.textViewMengkan,true);
            helper.setVisible(R.id.textViewStoreName,false);

        } else if ("6".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限"+item.getCompanyName()+"使用");
            helper.setText(R.id.textViewStoreName, "("+item.getStoreName()+"经理)");
            helper.setVisible(R.id.textViewMengkan,false);
            helper.setVisible(R.id.textViewStoreName,true);

        } else if ("7".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限"+item.getCompanyName()+"使用");
            helper.setText(R.id.textViewStoreName, "("+item.getStoreName()+"经理)");
            helper.setVisible(R.id.textViewMengkan,false);
            helper.setVisible(R.id.textViewStoreName,true);

        }

        if ("1".equals(item.getUseType())){
            helper.setText(R.id.textViewTime,"有效日期：永久有效");
        }else {
            helper.setText(R.id.textViewTime,"有效日期：" + item.getBeginTime() + "至" + item.getEndTime());
        }

    }
}

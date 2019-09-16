package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.ShopInfoBean;
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
public class WherePlayAdapter extends BaseQuickAdapter<ShopInfoBean,BaseViewHolder> {

    private Context context;

    public WherePlayAdapter(@Nullable List<ShopInfoBean> data, Context context) {
        super(R.layout.item_shop_information, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopInfoBean item) {
        helper.setText(R.id.tv_name,item.getCompanyName())
                .setText(R.id.tv_small_name,"(" + item.getStoreName()+"经理)")
                .setText(R.id.tv_distance,item.getDistance()+"km")
        .addOnClickListener(R.id.constraintLayout_coupon)
        .addOnClickListener(R.id.iv_more)
        .addOnClickListener(R.id.tv_reserve);

        if (StringUtil.isNotNull(item.getActivityName())){
            helper.setText(R.id.tv_event,"活动："+item.getActivityName());
        }else {
            helper.setText(R.id.tv_event,"活动：无");
        }

        if (StringUtil.isNotNull(item.getDiscount())){
            helper.setText(R.id.tv_zhekou,"酒水："+item.getDiscount() + "折");
        }else {
            helper.setText(R.id.tv_zhekou,"酒水：无折扣");
        }

        if (StringUtil.isNotNull(item.getCashCouponReturn())){
            helper.setText(R.id.tv_fanli,"代金券："+item.getCashCouponReturn());
        }else {
            helper.setText(R.id.tv_fanli,"代金券：无代金券");
        }

        if (StringUtil.isNotNull(item.getCashCouponProportion())){
            helper.setText(R.id.tv_xiaofeibilie,"代金券抵消费比例："+item.getCashCouponProportion());
        }else {
            helper.setText(R.id.tv_xiaofeibilie,"代金券抵消费比例：无");
        }


        TextView tv_daijia = helper.getView(R.id.tv_daijia);
        if ((null == item.getIsFreeDriving()) || "0".equals(item.getIsFreeDriving())){
            tv_daijia.setVisibility(View.GONE);
        }else {
            tv_daijia.setVisibility(View.VISIBLE);
        }
        ImageView iv_shop = helper.getView(R.id.iv_shop);
        ImageLoaderUtil.getInstance().loadRoundImage(context,item.getCoverImg(),iv_shop,20);
        RecyclerView recyclerView = helper.getView(R.id.recyclerView_meet);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        ShopMeetPeopleAdapter adapter = new ShopMeetPeopleAdapter(item.getUserInfo(),mContext);
        recyclerView.setAdapter(adapter);

        ConstraintLayout constraintLayout = helper.getView(R.id.constraintLayout_coupon);
        if (null == item.getStoreCoupon()){
            constraintLayout.setVisibility(View.GONE);
        }else {
            constraintLayout.setVisibility(View.VISIBLE);
            if ("0".equals(item.getIsCoupon())){
                constraintLayout.setBackgroundResource(R.mipmap.ic_shop_coupon_receive);
                helper.setText(R.id.textViewNumb,"￥"+item.getStoreCoupon().getCouponAmountX())
                        .setText(R.id.textViewTv,"领取");
            }else {
                constraintLayout.setBackgroundResource(R.mipmap.ic_shop_coupon_unreceive);
                helper.setText(R.id.textViewNumb,"￥"+item.getStoreCoupon().getCouponAmountX())
                        .setText(R.id.textViewTv,"已领取");
            }

        }

    }
}

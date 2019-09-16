package com.andy.meetquickly.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.RechargeBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class RechargeAdapter extends BaseQuickAdapter<RechargeBean,BaseViewHolder> {

    private Context context;
    private int selectPosition;

    public RechargeAdapter(@Nullable List<RechargeBean> data,Context context,int selectPosition) {
        super(R.layout.item_recharge_numb, data);
        this.context = context;
        this.selectPosition = selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeBean item) {
        helper.setText(R.id.tv_numb,item.getBillNumb())
                .setText(R.id.tv_money,item.getBillMoney());

        int offset = helper.getLayoutPosition();
        ConstraintLayout constraintLayout = helper.getView(R.id.constraint);
        TextView tv_numb = helper.getView(R.id.tv_numb);
        TextView tv_money = helper.getView(R.id.tv_money);

        if(offset == selectPosition){
            constraintLayout.setBackgroundResource(R.drawable.shape_bg_recharge_select);
            setTextColor(tv_money,R.color.white);
            setTextColor(tv_numb,R.color.white);
        }else {
            constraintLayout.setBackgroundResource(R.drawable.shape_bg_recharge_unselect);
            setTextColor(tv_money,R.color.themeColor);
            setTextColor(tv_numb,R.color.themeColor);
        }

    }

    public void setTextColor(TextView textView , int color){
        Resources resource = (Resources) context.getResources();
        ColorStateList csl = (ColorStateList) resource.getColorStateList(color);
        if (csl != null) {
            textView.setTextColor(csl);
        }
    }
}

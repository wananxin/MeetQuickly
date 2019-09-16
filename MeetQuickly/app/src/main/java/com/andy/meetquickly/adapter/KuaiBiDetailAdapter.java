package com.andy.meetquickly.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.QuicklyMoneyBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class KuaiBiDetailAdapter extends BaseQuickAdapter<QuicklyMoneyBean,BaseViewHolder> {

    private Context context;

    public KuaiBiDetailAdapter(@Nullable List<QuicklyMoneyBean> data, Context context) {
        super(R.layout.item_quickly_money, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, QuicklyMoneyBean item) {
        helper.setText(R.id.tv_depict,item.getDes())
                .setText(R.id.tv_time,item.getTime());

        TextView textView = helper.getView(R.id.tv_numb);
        if ("1".equals(item.getType()) || "4".equals(item.getType())){
            textView.setText("+" + item.getIncome());
            setTextColor(textView,R.color.appBlue);
        }else {
            textView.setText("-"+item.getExpense());
            setTextColor(textView,R.color.themeColor);
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

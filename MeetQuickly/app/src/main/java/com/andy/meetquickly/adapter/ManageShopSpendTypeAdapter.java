package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.UserShopDetailBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：我的网店消费类型RecyclerViewAdapter
 */
public class ManageShopSpendTypeAdapter extends BaseQuickAdapter<UserShopDetailBean.SpendTypeListBean,BaseViewHolder> {

    private Context context;
    public ManageShopSpendTypeAdapter(@Nullable List<UserShopDetailBean.SpendTypeListBean> data, Context context) {
        super(R.layout.item_manage_meal, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserShopDetailBean.SpendTypeListBean item) {
        helper.setText(R.id.tv_name,item.getName())
                .setText(R.id.tv_content,item.getRemark())
                .setText(R.id.tv_price,"￥"+item.getCost());
        ImageView imageView = helper.getView(R.id.iv_pic);
        ImageLoaderUtil.getInstance().loadImage(context,item.getDrinksImg(),imageView);
        TextView textView = helper.getView(R.id.tv_price_company);
        if (null == item.getUnit()){
            textView.setText("￥"+item.getPrice());
        }else {
            textView.setText("￥"+item.getPrice()+"/"+item.getUnit());
        }
    }
}

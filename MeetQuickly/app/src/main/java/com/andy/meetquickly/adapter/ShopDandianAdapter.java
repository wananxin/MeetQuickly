package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.UserShopDetailBean;
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
public class ShopDandianAdapter extends BaseQuickAdapter<UserShopDetailBean.SpendTypeListBean,BaseViewHolder> {

    private Context context;
    public ShopDandianAdapter(@Nullable List<UserShopDetailBean.SpendTypeListBean> data, Context context) {
        super(R.layout.item_shop_dandian, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserShopDetailBean.SpendTypeListBean item) {

        helper.setText(R.id.textViewName,item.getName())
                .setText(R.id.textViewContent,item.getRemark());

        ImageView imageView = helper.getView(R.id.iv_pic);
        ImageLoaderUtil.getInstance().loadImage(context,item.getDrinksImg(),imageView);
        if (StringUtil.isNotNull(item.getUnit())){
            helper.setText(R.id.textViewPrice,item.getPrice() + "/" + item.getUnit());
        }else {
            helper.setText(R.id.textViewPrice,item.getPrice());
        }



    }
}

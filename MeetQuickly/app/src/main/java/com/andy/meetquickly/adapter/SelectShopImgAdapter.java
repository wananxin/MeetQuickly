package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.StoreImgTypeBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class SelectShopImgAdapter extends BaseQuickAdapter<StoreImgTypeBean,BaseViewHolder> {

    private Context context;
    public SelectShopImgAdapter(@Nullable List<StoreImgTypeBean> data, Context context) {
        super(R.layout.item_select_pic_type, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreImgTypeBean item) {
        helper.setText(R.id.tv_position,"样式"+(helper.getLayoutPosition()+1));
        ImageView imageView = helper.getView(R.id.iv_pic);
        ImageLoaderUtil.getInstance().loadImage(mContext,item.getImgUrl(),imageView);

    }
}

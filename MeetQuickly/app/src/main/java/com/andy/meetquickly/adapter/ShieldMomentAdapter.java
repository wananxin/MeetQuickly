package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.ShieldMomentBean;
import com.andy.meetquickly.bean.ShieldPhoneBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class ShieldMomentAdapter extends BaseQuickAdapter<ShieldMomentBean,BaseViewHolder> {

    private Context context;
    public ShieldMomentAdapter(@Nullable List<ShieldMomentBean> data, Context context) {
        super(R.layout.item_shield_phone, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShieldMomentBean item) {
        helper.setText(R.id.textViewName,item.getNickName())
                .addOnClickListener(R.id.textViewCancle);
        ImageLoaderUtil.getInstance().loadCircleImage(context,item.getFace(),helper.getView(R.id.imageView));
    }
}

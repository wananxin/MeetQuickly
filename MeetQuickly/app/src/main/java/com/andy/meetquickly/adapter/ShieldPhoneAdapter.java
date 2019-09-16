package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
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
public class ShieldPhoneAdapter extends BaseQuickAdapter<ShieldPhoneBean,BaseViewHolder> {

    private Context context;
    public ShieldPhoneAdapter(@Nullable List<ShieldPhoneBean> data, Context context) {
        super(R.layout.item_shield_phone, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShieldPhoneBean item) {
        helper.setText(R.id.textViewName,item.getShieldName())
                .setText(R.id.textViewPhone,item.getShieldMobile())
                .addOnClickListener(R.id.textViewCancle);
    }
}

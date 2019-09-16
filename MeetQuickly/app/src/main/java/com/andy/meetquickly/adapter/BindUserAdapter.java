package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.ShopBindUserBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class BindUserAdapter extends BaseQuickAdapter<ShopBindUserBean, BaseViewHolder> {

    private Context context;

    public BindUserAdapter(@Nullable List<ShopBindUserBean> data, Context context) {
        super(R.layout.item_bind_user, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopBindUserBean item) {
        ImageView imageView = helper.getView(R.id.iv_user_head);
        ImageLoaderUtil.getInstance().loadCircleImage(context, item.getCoverImg(), imageView);
        helper.setText(R.id.tv_user_name, item.getCompanyName())
                .setText(R.id.textViewXing, "(" + item.getStoreName() + "经理)")
                .setText(R.id.tv_time, item.getCreateTime())
                .addOnClickListener(R.id.tv_button);

        if ("1".equals(item.getStatus())) {
            helper.setText(R.id.tv_button, "已同意");
        } else {
            helper.setText(R.id.tv_button, "同意");
        }

    }
}

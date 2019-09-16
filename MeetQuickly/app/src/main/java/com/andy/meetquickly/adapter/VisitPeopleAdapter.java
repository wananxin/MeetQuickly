package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.MomnetListBean;
import com.andy.meetquickly.bean.ShopDetailBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class VisitPeopleAdapter extends BaseQuickAdapter<ShopDetailBean.StoreVisitBean,BaseViewHolder> {

    private Context context;
    public VisitPeopleAdapter(@Nullable List<ShopDetailBean.StoreVisitBean> data , Context context) {
        super(R.layout.item_round_head, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopDetailBean.StoreVisitBean item) {
        ImageView iv_head = helper.getView(R.id.iv_head);
        ImageLoaderUtil.getInstance().loadCircleImage(context,item.getFace(),iv_head);
    }
}

package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.EventCenterBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class EventCenterAdapter extends BaseQuickAdapter<EventCenterBean,BaseViewHolder> {

    private Context context;
    public EventCenterAdapter(@Nullable List<EventCenterBean> data, Context context) {
        super(R.layout.item_event_center, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, EventCenterBean item) {
        ImageView imageView = helper.getView(R.id.imageView);
        ImageLoaderUtil.getInstance().loadImage(context,item.getUrl(),imageView);
    }
}

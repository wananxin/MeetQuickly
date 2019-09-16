package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.StoreEventBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class FindEventAdapter extends BaseQuickAdapter<StoreEventBean, BaseViewHolder> {

    private Context context;
    private int type;
    public FindEventAdapter(@Nullable List<StoreEventBean> data, Context context ,int type) {
        super(R.layout.item_moment_event, data);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreEventBean item) {
        helper.setText(R.id.tv_time, item.getExpireTime() + " 结束")
                .setText(R.id.tv_content, item.getContent())
                .addOnClickListener(R.id.tv_yuding)
                .addOnClickListener(R.id.tv_delete);
        ImageView imageView = helper.getView(R.id.iv_head);
        ImageLoaderUtil.getInstance().loadCircleImage(context, item.getCoverImg(), imageView);
        TextView tvState = helper.getView(R.id.tv_situation);
        TextView tvYuDing = helper.getView(R.id.tv_yuding);
        TextView tvDelete = helper.getView(R.id.tv_delete);
        if (item.getIsEnd().equals("1")) {
            tvState.setText("此活动已结束");
            tvYuDing.setVisibility(View.GONE);
        } else {
            tvState.setText("正在火热预定中，仅剩" + item.getOrderNum() + "个名额");
            tvYuDing.setVisibility(View.VISIBLE);
        }

        if (item.getStatus().equals("2")){
            tvState.setText("此活动申请被拒绝");
        }

        if (type == 1){
            tvYuDing.setVisibility(View.GONE);
            if (item.getIsEnd().equals("1")) {
                tvDelete.setVisibility(View.VISIBLE);
            }else {
                tvDelete.setVisibility(View.GONE);
            }
        }else {
            tvDelete.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = helper.getView(R.id.recyclerView);

        if (item.getType().equals("1")) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
            StoreMediaAdapter adapter;
            adapter = new StoreMediaAdapter(item.getStoreActivityImgs(), context, 1);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
        }

    }
}

package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.CommentBean;
import com.andy.meetquickly.bean.VideoCommentBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class VideoCommentAdapter extends BaseQuickAdapter<VideoCommentBean,BaseViewHolder> {

    private Context context;
    public VideoCommentAdapter(@Nullable List<VideoCommentBean> data, Context context) {
        super(R.layout.item_moment_comment, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoCommentBean item) {
        helper.setText(R.id.tv_name,item.getNickName())
                .setText(R.id.tv_content,item.getContent())
                .setText(R.id.tv_time,item.getCreateTime());

        ImageView imageView = helper.getView(R.id.iv_head);
        ImageLoaderUtil.getInstance().loadCircleImage(context,item.getFace(),imageView);

    }
}

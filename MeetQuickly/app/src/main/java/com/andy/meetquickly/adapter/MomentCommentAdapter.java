package com.andy.meetquickly.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.CommentBean;
import com.andy.meetquickly.utils.CommentUtil;
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
public class MomentCommentAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

    private Context context;

    public MomentCommentAdapter(@Nullable List<CommentBean> data, Context context) {
        super(R.layout.item_moment_comment, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentBean item) {
        helper.setText(R.id.tv_name, item.getNickName())
                .setText(R.id.tv_content, item.getContent())
                .setText(R.id.tv_time, item.getCreateTime())
                .addOnClickListener(R.id.iv_head);

        if (StringUtil.isNotNull(item.getToUid())) {
            SpannableString spannableString = new SpannableString("@" + item.getToNickName() + ":" + item.getContent());
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#E24333"));
            spannableString.setSpan(colorSpan, 0, item.getToNickName().length() + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.tv_content, spannableString);
        } else {
            helper.setText(R.id.tv_content, item.getContent());
        }

        ImageView imageView = helper.getView(R.id.iv_head);
        ImageLoaderUtil.getInstance().loadCircleImage(context, item.getFace(), imageView);

    }

}

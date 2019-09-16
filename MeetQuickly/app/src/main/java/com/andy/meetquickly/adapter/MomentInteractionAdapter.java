package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.MomnetListBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class MomentInteractionAdapter extends BaseQuickAdapter<MomnetListBean,BaseViewHolder> {

    private Context context;
    public MomentInteractionAdapter(@Nullable List<MomnetListBean> data , Context context) {
        super(R.layout.item_moment_interaction, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MomnetListBean item) {
        ImageView imageViewHead = helper.getView(R.id.imageViewHead);
        ImageLoaderUtil.getInstance().loadCircleImage(context,item.getFace(),imageViewHead);
        helper.setText(R.id.textViewName,item.getNickName())
                .setText(R.id.textViewAge,String.valueOf(item.getAge()))
                .setText(R.id.textViewContent,item.getReplyContent())
                .setText(R.id.textViewTime,item.getCreateTime())
                .setText(R.id.textViewDistance,item.getDistance()+"km")
        .addOnClickListener(R.id.cardView)
        .addOnClickListener(R.id.textViewHuifu);
        ImageView imageViewSex = helper.getView(R.id.imageViewSex);
        if ("1".equals(item.getSex())){
            imageViewSex.setImageResource(R.mipmap.ic_men);
        }else {
            imageViewSex.setImageResource(R.mipmap.ic_women);
        }
        ImageView imageViewPic = helper.getView(R.id.iv_pic);
        TextView textViewShuoShuo = helper.getView(R.id.tv_shuoshuo);
        ImageView imageViewVideo = helper.getView(R.id.iv_video);

        if ("0".equals(item.getType())){
            //纯文字说说
            imageViewPic.setVisibility(View.GONE);
            imageViewVideo.setVisibility(View.GONE);
            textViewShuoShuo.setVisibility(View.VISIBLE);
            textViewShuoShuo.setText(item.getContent());
        }else if ("1".equals(item.getType())){
            imageViewPic.setVisibility(View.VISIBLE);
            imageViewVideo.setVisibility(View.GONE);
            textViewShuoShuo.setVisibility(View.GONE);
            ImageLoaderUtil.getInstance().loadImage(context,item.getUserDynamicFiles().get(0).getUrl(),imageViewPic);
        }else {
            imageViewPic.setVisibility(View.VISIBLE);
            imageViewVideo.setVisibility(View.VISIBLE);
            textViewShuoShuo.setVisibility(View.GONE);
            ImageLoaderUtil.getInstance().loadImage(context,item.getUserDynamicFiles().get(0).getUrl(),imageViewPic);
        }
    }
}

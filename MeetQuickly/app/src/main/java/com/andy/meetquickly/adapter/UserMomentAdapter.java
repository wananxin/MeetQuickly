package com.andy.meetquickly.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.find.SimpleVideoPlayActivity;
import com.andy.meetquickly.bean.MomnetListBean;
import com.andy.meetquickly.utils.CommentUtil;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.luck.picture.lib.PictureSelector;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class UserMomentAdapter extends BaseQuickAdapter<MomnetListBean,BaseViewHolder> {

    private Context context;
    private int type;  //1为看别人的   2为自己的
    public UserMomentAdapter(@Nullable List<MomnetListBean> data, Context context ,int type) {
        super(R.layout.item_user_data_moment, data);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, MomnetListBean item) {
        ImageView iv_praise = helper.getView(R.id.iv_parse);
        if (item.getIsThumbsUp().equals("1")) {
            iv_praise.setImageResource(R.mipmap.ic_praise_select);
        } else {
            iv_praise.setImageResource(R.mipmap.ic_praise_unselect);
        }
        helper.addOnClickListener(R.id.iv_more)
                .addOnClickListener(R.id.iv_parse)
                .addOnClickListener(R.id.tv_delete)
                .addOnClickListener(R.id.relativeLayoutOne)
                .setText(R.id.tv_time, item.getCreateTime())
                .setText(R.id.tv_content, item.getContent())
                .setText(R.id.tv_comment_numb, item.getCommentNum())
                .setText(R.id.tv_prase_numb, item.getPraiseNum());

        if (StringUtil.isNotNull(item.getContent())){
            helper.setGone(R.id.tv_content,true);
        }else {
            helper.setGone(R.id.tv_content,false);
        }

        if (type == 1){
            helper.setGone(R.id.tv_delete,false)
            .setGone(R.id.iv_more,true);
        }else {
            helper.setGone(R.id.tv_delete,true)
                    .setGone(R.id.iv_more,false);
        }

        RecyclerView recyclerView = helper.getView(R.id.recyclerView_pic);
        recyclerView.setVisibility(View.GONE);
        RelativeLayout relativeLayoutOne = helper.getView(R.id.relativeLayoutOne);
        ImageView imageViewOne = helper.getView(R.id.imageViewOne);
        ImageView imageViewVideoOne = helper.getView(R.id.imageViewVideoOne);
        relativeLayoutOne.setVisibility(View.GONE);
        if (item.getUserDynamicFiles().size() > 0) {
            if (item.getUserDynamicFiles().size() == 1){
                relativeLayoutOne.setVisibility(View.VISIBLE);
                if (item.getType().equals("1")){
                    //图片
                    ImageLoaderUtil.getInstance().loadImage(context,item.getUserDynamicFiles().get(0).getUrl(),imageViewOne);
                    imageViewVideoOne.setVisibility(View.GONE);
                }else {
                    //视频
                    ImageLoaderUtil.getInstance().loadImage(context,item.getUserDynamicFiles().get(0).getUrl(),imageViewOne);
                    imageViewVideoOne.setVisibility(View.VISIBLE);
                }
            }else {
                //大于0再去加载recycler
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                MomentMediaAdapter adapter;
                if (item.getType().equals("1")) {
                    //图片
                    adapter = new MomentMediaAdapter(item.getUserDynamicFiles(), context, 1);
                    adapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            List<String> lists = new ArrayList<>();
                            for (int i = 0; i < item.getUserDynamicFiles().size(); i++) {
                                lists.add(item.getUserDynamicFiles().get(i).getThumb());
                            }
                            ImagePreview.getInstance()
                                    .setContext(context)
                                    .setIndex(position)
                                    .setImageList(lists)
                                    .setEnableDragClose(true)
                                    .setErrorPlaceHolder(R.drawable.load_failed)
                                    .start();
                        }
                    });
                } else {
                    //视频
                    adapter = new MomentMediaAdapter(item.getUserDynamicFiles(), context, 2);
                    adapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                        VideoPlayActivity.start(mContext,item.getUserDynamicFiles().get(position).getUrl());
//                        PictureSelector.create((Activity) mContext).externalPictureVideo(item.getUserDynamicFiles().get(position).getUrl());
                            SimpleVideoPlayActivity.start(mContext, item.getUserDynamicFiles().get(position).getUrl());
                        }
                    });
                }
                recyclerView.setAdapter(adapter);
            }

        }
    }
}

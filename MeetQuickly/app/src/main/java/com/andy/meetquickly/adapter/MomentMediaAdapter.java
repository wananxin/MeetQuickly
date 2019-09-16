package com.andy.meetquickly.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.MomnetListBean;
import com.andy.meetquickly.utils.CommentUtil;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class MomentMediaAdapter extends BaseQuickAdapter<MomnetListBean.UserDynamicFilesBean,BaseViewHolder> {

    private Context context;
    private int type;  //1,代表图片，2代表视频
    public MomentMediaAdapter(@Nullable List<MomnetListBean.UserDynamicFilesBean> data , Context context,int type) {
        super(R.layout.item_moment_media, data);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, MomnetListBean.UserDynamicFilesBean item) {
        ImageView imageView_pic = helper.getView(R.id.iv_pic);
        ImageView imageView_video = helper.getView(R.id.iv_video);
        if (type == 1){
            imageView_video.setVisibility(View.GONE);
            ImageLoaderUtil.getInstance().loadImage(context,item.getThumb(),imageView_pic);
        }else {
            imageView_video.setVisibility(View.VISIBLE);
            ImageLoaderUtil.getInstance().loadImage(context,item.getUrl(),imageView_pic);
        }
    }

}

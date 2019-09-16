package com.andy.meetquickly.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.MomnetListBean;
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
public class StoreMediaAdapter extends BaseQuickAdapter<StoreEventBean.StoreActivityImgsBean,BaseViewHolder> {

    private Context context;
    private int type;  //1,代表图片，2代表视频
    public StoreMediaAdapter(@Nullable List<StoreEventBean.StoreActivityImgsBean> data , Context context, int type) {
        super(R.layout.item_moment_media, data);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreEventBean.StoreActivityImgsBean item) {
        ImageView imageView_pic = helper.getView(R.id.iv_pic);
        ImageView imageView_video = helper.getView(R.id.iv_video);
        if (type == 1){
            imageView_video.setVisibility(View.GONE);
            ImageLoaderUtil.getInstance().loadImage(context,item.getImgUrl(),imageView_pic);
        }else {
            imageView_video.setVisibility(View.VISIBLE);
            imageView_pic.setImageBitmap(getVideoThumb(item.getUrl()));
        }

    }

    public static Bitmap getVideoThumb(String path) {

        MediaMetadataRetriever media = new MediaMetadataRetriever();

        media.setDataSource(path);

        return media.getFrameAtTime();

    }
}

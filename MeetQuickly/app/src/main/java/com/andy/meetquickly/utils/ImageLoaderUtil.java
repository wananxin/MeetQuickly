package com.andy.meetquickly.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/26 9:35
 * 描述：
 */
public class ImageLoaderUtil {
    static ImageLoaderUtil instance;

    public ImageLoaderUtil() {
    }

    public static ImageLoaderUtil getInstance() {
        if (null == instance) {
            synchronized (ImageLoaderUtil.class) {
                if (null == instance) {
                    instance = new ImageLoaderUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 普通加载
     * @param context
     * @param url
     * @param imageView
     */
    public  void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.img_error_place)
                .error(R.mipmap.img_error_place);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * 加载圆形图片
     * @param context
     * @param url
     * @param imageView
     */
    public  void loadCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()
                .placeholder(R.mipmap.img_error_place_round)
                .error(R.mipmap.img_error_place_round);
        Glide.with(context).load(url).apply(options).into(imageView);
    }
    /**
     * 加载圆形带圆边图片
     * @param context
     * @param url
     * @param imageView
     */
//    public  void loadCircleWithFrameImage(Context context, String url, ImageView imageView,int borderWidth,int borderColor) {
//        RequestOptions options = RequestOptions
//                .bitmapTransform(new GlideCircleWithFrameTransform(borderWidth,borderColor))
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher);
//        Glide.with(context).load(url).apply(options).into(imageView);
//    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public  void loadRoundImage(Context context, String url, ImageView imageView ,int radio) {
        RequestOptions options = RequestOptions
                .bitmapTransform(new RoundedCorners(radio))
                .centerCrop()
                .placeholder(R.mipmap.img_error_place)
                .error(R.mipmap.img_error_place);

        Glide.with(context).load(url).apply(options).into(imageView);
    }
}

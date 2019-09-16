package com.andy.meetquickly.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/25 15:12
 * 描述：
 */
public class CommentUtil {

    public static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String stringNotNull(String s) {
        if (null == s) {
            return "";
        } else {
            return s;
        }
    }

    public static void getLocation(Context context, LocationClient locationClient, BDAbstractLocationListener listener) {
        LocationClient mLocationClient = null;
        mLocationClient = locationClient;     //声明LocationClient类
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式。高精度。低功耗，仅设备
        option.setCoorType("BD09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);//可选。默认0，即仅定位一次，设置发起定位请求的间隔须要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否须要地址信息，默认不须要
        option.setOpenGps(false);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当GPS有效时依照1S/1次频率输出GPS结果
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE。并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选。默认false，设置是否收集CRASH信息，默认收集
        mLocationClient.setLocOption(option);
        //开启定位
        mLocationClient.registerLocationListener(listener);
        mLocationClient.start();
    }

    public static String getReachData(String string) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        long lt = new Long(string);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String getStandardDate(String mTime) {

        int dateTime = Integer.parseInt(mTime);
        StringBuffer sb = new StringBuffer();

        long t = Long.parseLong(String.valueOf(dateTime));
        long time = System.currentTimeMillis() - (t * 1000);
        long mill = (long) Math.ceil(time / 1000);//秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        long month = (long) Math.ceil(time / 30 / 24 / 60 / 60 / 1000.0f);// 月前

        long year = (long) Math.ceil(time / 12 / 30 / 24 / 60 / 60 / 1000.0f);// 年前

        Calendar cal = Calendar.getInstance();

        long showYear = cal.get(Calendar.YEAR);

        long showMonth = cal.get(Calendar.MONTH);

        long showDay = cal.get(Calendar.DATE);

        long showHour = cal.get(Calendar.HOUR_OF_DAY);

        long showMint = cal.get(Calendar.MINUTE);

        if (month - 1 > 0) {
//            if (month >= 12) {
//                sb.append(showYear + "." + showMonth + "." + showDay + " " + showHour + ":" + showMint);
//            } else {
//                sb.append(month + "个月");
//            }
            sb.append(timeStamp2Date(mTime, null));
        } else if (day - 1 > 0) {
//            if (day >= 30) {
//                sb.append("1个月");
//            } else {
//                sb.append(day + "天");
//            }
            sb.append(timeStamp2Date(mTime, null));
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("昨天");
            } else {
                sb.append(hour + "个小时前");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1个小时前");
            } else {
                sb.append(minute + "分钟前");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟前");
            } else {
                sb.append(mill + "秒前");
            }
        } else {
            sb.append("刚刚");
        }
//        if (month <= 12 && !sb.toString().equals("刚刚")) {
//            sb.append("前");
//        }
        return sb.toString();
    }

    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    //网络视频获取第一帧图片
    public static Bitmap createVideoThumbnail(String filePath, int kind) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (filePath.startsWith("http://")
                    || filePath.startsWith("https://")
                    || filePath.startsWith("widevine://")) {
                retriever.setDataSource(filePath, new Hashtable<String, String>());
            } else {
                retriever.setDataSource(filePath);
            }
            bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC); //retriever.getFrameAtTime(-1);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
                ex.printStackTrace();
            }
        }

        if (bitmap == null) {
            return null;
        }

        if (kind == MediaStore.Images.Thumbnails.MINI_KIND) {//压缩图片 开始处
            // Scale down the bitmap if it's too large.
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int max = Math.max(width, height);
            if (max > 512) {
                float scale = 512f / max;
                int w = Math.round(scale * width);
                int h = Math.round(scale * height);
                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            }//压缩图片 结束处
        } else if (kind == MediaStore.Images.Thumbnails.MICRO_KIND) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                    96,
                    96,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }


    //本地视频获取第一帧图片
    public static Bitmap getVideoThumb(String path) {

        MediaMetadataRetriever media = new MediaMetadataRetriever();

        media.setDataSource(path);

        return media.getFrameAtTime();

    }

}

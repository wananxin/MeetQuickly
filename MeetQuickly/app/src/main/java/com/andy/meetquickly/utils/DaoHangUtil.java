package com.andy.meetquickly.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 作者: 唐勇
 * 时间: 2017/6/26
 * 类名: 调用第三方导航
 */

public class DaoHangUtil {


    public static void getApi(Context context, String latLng, String longitude, String adds, String mlat, String mlon) {
        if (isInstallByRead("com.baidu.BaiduMap")) {
            String origin = "latlng:" + mlat + "," + mlon + "|name:我的位置";
            String destination = "latlng:" + latLng + "," + longitude + "|name:" + adds;
            AMapUtil.goToBaiduNaviActivity(context, origin, destination, "driving", "", "", "", "gcj02", "", "yourCompanyName|yourAppName");
        } else if(isInstallByRead("com.tencent.map")){
            double mmlat = Double.parseDouble(mlat);
            double mmlon = Double.parseDouble(mlon);
            double llatLng = Double.parseDouble(latLng);
            double llongitude = Double.parseDouble(longitude);
            AMapUtil.goToTenCentNaviActivity(context,"drive","我的位置",mmlat,mmlon,adds,llatLng,llongitude);
        } else if (isInstallByRead("com.autonavi.minimap")) {
            AMapUtil.goToGaodeNaviActivity(context, "meetquickly", null, latLng + "", longitude + "", "0", "2");
        } else {
            Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }


    /**
     * 根据包名检测某个APP是否安装
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/27,13:02
     * <h3>UpdateTime</h3> 2016/6/27,13:02
     * <h3>CreateAuthor</h3> luzhenbang
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param packageName 包名
     * @return true 安装 false 没有安装
     */
    public static boolean isInstallByRead(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

}

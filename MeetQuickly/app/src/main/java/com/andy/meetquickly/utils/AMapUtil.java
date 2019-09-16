package com.andy.meetquickly.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.net.URISyntaxException;

/**
 * 作者: 唐勇
 * 时间: 2017/6/23
 * 类名:
 */

public class AMapUtil {
    /**
     * 启动高德App进行导航
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param sourceApplication 必填 第三方调用应用名称。如 amap
     * @param poiname 非必填 POI 名称
     * @param lat 必填 纬度
     * @param lon 必填 经度
     * @param dev 必填 是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     * @param style 必填 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
     */
    public static  void goToGaodeNaviActivity(Context context, String sourceApplication , String poiname , String lat , String lon , String dev , String style){
        StringBuffer stringBuffer  = new StringBuffer("androidamap://navi?sourceApplication=")
                .append(sourceApplication);
        if (!TextUtils.isEmpty(poiname)){
            stringBuffer.append("&poiname=").append(poiname);
        }
        stringBuffer.append("&lat=").append(lat)
                .append("&lon=").append(lon)
                .append("&dev=").append(dev)
                .append("&style=").append(style);

        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }

    /**
     * 腾讯导航
     *  file:///E:/someDownCode/IntentMapGuide-master/%E8%85%BE%E8%AE%AF%E5%9C%B0%E5%9B%BE_URI%E8%A7%84%E8%8C%83%E6%96%87%E6%A1%A3_150417/URI%E8%A7%84%E8%8C%83%E6%96%87%E6%A1%A3.html
     * qqmap://map/routeplan?type=drive&from=天坛南门&fromcoord=39.873145,116.413306&to=国家大剧院&tocoord=39.907380,116.388501
     * coord=39.904956,116.389449	lat<纬度>,lng<经度>
     * //移动端启动腾讯地图App，并显示从出发点[天坛南门] 到 [目的地坐标(国家大剧院)] 的驾车路线规划
     * @param context
     * @param type 路线规划方式参数：公交 bus   驾车 drive    步行 walk
     * @param from
     * @param fromLat
     * @param fromLon
     * @param to
     * @param toLat
     * @param toLon
     */
    public static void goToTenCentNaviActivity(Context context, String type, String from, double fromLat, double fromLon, String to, double toLat, double toLon) {
        String locationStr = "qqmap://map/routeplan?type="
                + type
                +"&from="
                + from
                + "&fromcoord="
                + fromLat
                + ","
                + fromLon
                + "&to="
                + to
                + "&tocoord="
                + toLat
                + ","
                + toLon;
        System.out.println("tencent-location:" + locationStr);
        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(locationStr));
        intent.setPackage("com.tencent.map");
        context.startActivity(intent);
    }

    /**
     * 启动BaiduApp进行导航
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/27,11:23
     * <h3>UpdateTime</h3> 2016/6/27,11:23
     * <h3>CreateAuthor</h3> luzhenbang
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param context 上下文
     * @param origin 必选  起点名称或经纬度，或者可同时提供名称和经纬度，此时经纬度优先级高，将作为导航依据，名称只负责展示。例如： latlng:34.264642646862,108.95108518068|name:我家
     * @param destination 必选 终点名称或经纬度，或者可同时提供名称和经纬度，此时经纬度优先级高，将作为导航依据，名称只负责展示。
     * @param mode 必选 导航模式，固定为transit、driving、walking，分别表示公交、驾车和步行
     * @param region 必选 城市名或县名 当给定region时，认为起点和终点都在同一城市，除非单独给定起点或终点的城市。
     * @param origin_region 必选  起点所在城市或县
     * @param destination_region 必选  终点所在城市或县
     * @param coord_type 可选 坐标类型，可选参数，默认为bd09经纬度坐标。
     * @param zoom 可选 展现地图的级别，默认为视觉最优级别。
     * @param src 必选 调用来源，规则：companyName|appName。
     */
    public static  void goToBaiduNaviActivity(Context context, String origin , String destination  , String mode , String region , String origin_region , String destination_region
            , String coord_type , String zoom , String src){
        StringBuffer stringBuffer  = new StringBuffer("intent://map/direction?origin=");
        stringBuffer.append(origin)
                .append("&destination=").append(destination)
                .append("&mode=").append(mode);
        if (!TextUtils.isEmpty(region)){
            stringBuffer.append("&region=").append(region);
        }
        if (!TextUtils.isEmpty(origin_region)){
            stringBuffer.append("&origin_region=").append(origin_region);
        }
        if (!TextUtils.isEmpty(destination_region)){
            stringBuffer.append("&destination_region=").append(destination_region);
        }
        if (!TextUtils.isEmpty(coord_type)){
            stringBuffer.append("&coord_type=").append(coord_type);
        }
        if (!TextUtils.isEmpty(zoom)){
            stringBuffer.append("&zoom=").append(zoom);
        }
        stringBuffer.append("&src=").append(src).append("#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
        String intentString = stringBuffer.toString();
        try {
            Intent intent  = Intent.getIntent(intentString);
            context.startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

}

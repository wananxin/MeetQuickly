package com.andy.meetquickly.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
public class Constant {
    public static boolean DEBUG = true;
//    public static String PUBLIC_ADDRESS = "http://60.205.189.58:8080/"; //测试地址
    public static String PUBLIC_ADDRESS = "http://www.woquyule.com:8080/"; //正式地址
//    public static String PUBLIC_ADDRESS = "http://192.168.1.104:8080/"; //康哥地  址
//    public static String PUBLIC_ADDRESS = "http://192.168.1.116:8080/"; //周俊地址
    public static String API_SERVER=PUBLIC_ADDRESS + "kuaihou-platform/api/";

    public static String KEY_LONGITUDE = "key_Longitude";
    public static String KEY_LATITUDE = "key_Latitude";
    public static String KEY_ADDRESS = "key_long";
    public static String KEY_VERSION = "key_version";

    public static String WECHART_ID = "wx453ff6a33045b858";

    /*用户协议*/
    public static String USER_AGREEMENT = "http://www.woquyule.com:8080/rapidMeet/login/userAgreement.html";

    /*合伙人权益*/
    public static String PARTNER_EQUITY_AGREEMENT = "http://www.woquyule.com:8080/rapidMeet/login/partnerEquityAgreement.html";

    /*开店协议*/
    public static String SHOP_AGREEMENT = "http://www.woquyule.com:8080/rapidMeet/login/shopAgreement.html";

    /*提现协议*/
    public static String WITH_DRAW_AGREEMENT = "http://www.woquyule.com:8080/rapidMeet/login/withdrawDepositAgreement.html";

    /*提现规则*/
    public static String CASH_RULE = "http://www.woquyule.com:8080/rapidMeet/login/withdrawDepositRule.html";

}

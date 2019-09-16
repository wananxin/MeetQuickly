package com.andy.meetquickly.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/17 16:06
 * 描述：
 */
public class WechartPayBean {


    @Override
    public String toString() {
        return "WechartPayBean{" +
                "appid='" + appid + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", packageX='" + packageX + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", sign='" + sign + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    /**
     * appid : wx453ff6a33045b858
     * noncestr : 402881e76ac4d341016ac4d3414e0000
     * package : Sign=WXPay
     * partnerid : 1535217171
     * prepayid : wx1716053236450415fc4905210990618864
     * sign : B9F0DDDFB7FF0A584D05C80246BA3478
     * timestamp : 1558080341
     */

    private String appid;
    private String noncestr;
    @SerializedName("package")
    private String packageX;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

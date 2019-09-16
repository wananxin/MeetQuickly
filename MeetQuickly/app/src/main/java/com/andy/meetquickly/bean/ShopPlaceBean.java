package com.andy.meetquickly.bean;

import com.andy.meetquickly.view.indexbar.Abbreviated;
import com.andy.meetquickly.view.indexbar.ContactsUtils;

import java.io.Serializable;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/23 10:01
 * 描述：
 */
public class ShopPlaceBean implements Abbreviated, Serializable,Comparable<ShopPlaceBean>{


    /**
     * address : 芙蓉路
     * area : 开福区
     * city : 长沙
     * companyName : 万代
     * createId : null
     * createName : null
     * createTime : 2019-03-12 18:03:12
     * id : 1000
     * latitude : 113.018967
     * longitude : 28.242271
     * province : 湖南
     * remark : 好玩的
     */

    private String address;
    private String area;
    private String city;
    private String companyName;
    private Object createId;
    private Object createName;
    private String createTime;
    private String id;
    private String latitude;
    private String longitude;
    private String province;
    private String remark;
    private  String mAbbreviation;
    private  String mInitial;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;

    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
        this.mAbbreviation = ContactsUtils.getAbbreviation(companyName);
        this.mInitial = mAbbreviation.substring(0, 1);
    }

    public Object getCreateId() {
        return createId;
    }

    public void setCreateId(Object createId) {
        this.createId = createId;
    }

    public Object getCreateName() {
        return createName;
    }

    public void setCreateName(Object createName) {
        this.createName = createName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int compareTo(ShopPlaceBean r) {
        if (mAbbreviation.equals(r.mAbbreviation)) {
            return 0;
        }
        boolean flag;
        if ((flag = mAbbreviation.startsWith("#")) ^ r.mAbbreviation.startsWith("#")) {
            return flag ? 1 : -1;
        }
        return getInitial().compareTo(r.getInitial());
    }

    @Override
    public String getInitial() {
        return mInitial;
    }
}

package com.andy.meetquickly.bean;

import java.io.Serializable;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/6/13 20:07
 * 描述：
 */
public class ShopBindUserBean implements Serializable {


    /**
     * companyName : 万代
     * coverImg : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/6/11/402881e76b467c07016b467c07010000.jpg
     * createTime : 2019-05-28 16:13:24
     * id : 24
     * isUnbind : 1
     * managerId : null
     * status : 2
     * storeId : 1105
     * storeName : 小丹的小店
     * total : 2
     * type : 2
     * uId : 20005637
     */

    private String companyName;
    private String coverImg;
    private String createTime;
    private String id;
    private String isUnbind;
    private Object managerId;
    private String status;
    private String storeId;
    private String storeName;
    private String total;
    private String type;
    private String uId;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
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

    public String getIsUnbind() {
        return isUnbind;
    }

    public void setIsUnbind(String isUnbind) {
        this.isUnbind = isUnbind;
    }

    public Object getManagerId() {
        return managerId;
    }

    public void setManagerId(Object managerId) {
        this.managerId = managerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }
}

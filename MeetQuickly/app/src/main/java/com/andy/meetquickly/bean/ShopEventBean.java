package com.andy.meetquickly.bean;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/29 14:49
 * 描述：
 */
public class ShopEventBean {


    /**
     * addTime : 2018-11-22 15:11:01
     * endTime : 2018-11-30
     * id : 382
     * isDelete : 1
     * name : 免包厢费，进店开机送一打酒
     * startTime : 2018-11-22
     * storeId : 1105
     */

    private String addTime;
    private String endTime;
    private String id;
    private String isDelete;
    private String name;
    private String startTime;
    private String storeId;

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}

package com.andy.meetquickly.bean;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/9 20:11
 * 描述：
 */
public class GiftBean {


    /**
     * addTime : null
     * id : 116
     * money : 5.00
     * name : 原谅帽
     * sort : 16
     * type : 1
     * url : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/gift/yuanliangmao.png
     */

    private String id;
    private String money;
    private String name;
    private String sort;
    private String type;
    private String url;
    private boolean isSelect = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}

package com.andy.meetquickly.bean;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/9 16:06
 * 描述：
 */
public class MeesageGiftBean {
    /**
     * giftNum : 1
     * giftData : {"name":"原谅帽","sort":"16","id":"116","type":"1","money":"5.00","url":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/gift/yuanliangmao.png"}
     */

    private int giftNum;
    private GiftDataBean giftData;

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public GiftDataBean getGiftData() {
        return giftData;
    }

    public void setGiftData(GiftDataBean giftData) {
        this.giftData = giftData;
    }

    public static class GiftDataBean {
        /**
         * name : 原谅帽
         * sort : 16
         * id : 116
         * type : 1
         * money : 5.00
         * url : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/gift/yuanliangmao.png
         */

        private String name;
        private String sort;
        private String id;
        private String type;
        private String money;
        private String url;

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

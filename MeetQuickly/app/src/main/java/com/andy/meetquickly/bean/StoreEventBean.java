package com.andy.meetquickly.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/23 13:54
 * 描述：
 */
public class StoreEventBean implements Serializable {


    /**
     * content : 妹纸678，真空裸嗨
     * coverImg : http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15427040751966.jpg
     * createTime : 2019-01-15 11:01:59
     * expireTime : 2018-07-20 08:22
     * id : 2
     * isDelete : null
     * isEnd : 1
     * isExcept : 0
     * orderNum : 8
     * rejectSeason : null
     * status : 1
     * storeActivityImgs : [{"createTime":"2019-01-15 11:01:59","id":"2","imgUrl":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/meal/1.jpg","sort":"1","status":"1","storeActivityId":"2","thumbUrl":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/meal/1.jpg"},{"createTime":"2019-01-15 11:01:59","id":"1","imgUrl":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/beer/17.jpg","sort":"0","status":"1","storeActivityId":"2","thumbUrl":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/beer/17.jpg"}]
     * storeId : 1105
     * type : 1
     * updateBy : null
     * updateTime : null
     */

    private String content;
    private String coverImg;
    private String createTime;
    private String expireTime;
    private String id;
    private String isEnd;
    private String isExcept;
    private String orderNum;
    private String status;
    private String storeId;
    private String type;
    private String kind;
    private List<StoreActivityImgsBean> storeActivityImgs;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(String isEnd) {
        this.isEnd = isEnd;
    }

    public String getIsExcept() {
        return isExcept;
    }

    public void setIsExcept(String isExcept) {
        this.isExcept = isExcept;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<StoreActivityImgsBean> getStoreActivityImgs() {
        return storeActivityImgs;
    }

    public void setStoreActivityImgs(List<StoreActivityImgsBean> storeActivityImgs) {
        this.storeActivityImgs = storeActivityImgs;
    }

    public static class StoreActivityImgsBean implements Serializable{
        /**
         * createTime : 2019-01-15 11:01:59
         * id : 2
         * imgUrl : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/meal/1.jpg
         * sort : 1
         * status : 1
         * storeActivityId : 2
         * thumbUrl : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/meal/1.jpg
         */

        private String createTime;
        private String id;
        private String imgUrl;
        private String sort;
        private String status;
        private String storeActivityId;
        private String thumbUrl;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStoreActivityId() {
            return storeActivityId;
        }

        public void setStoreActivityId(String storeActivityId) {
            this.storeActivityId = storeActivityId;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }
    }
}

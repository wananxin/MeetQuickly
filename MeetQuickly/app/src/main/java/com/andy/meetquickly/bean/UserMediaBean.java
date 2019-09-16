package com.andy.meetquickly.bean;

import java.io.Serializable;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/29 19:44
 * 描述：
 */
public class UserMediaBean implements Serializable {


    /**
     * addTime : 2018-08-26 02:08:31
     * id : 3850
     * sort : 1
     * status : 1
     * thumb : http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15352223117354a.jpg
     * type : 1
     * uId : 20000272
     * url : http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15352223117354.jpg
     */

    private String addTime;
    private String id;
    private String sort;
    private String status;
    private String thumb;
    private String type;
    private String uId;
    private String url;
    /**
     * commentNo : 4
     * isThumbsUp : 0
     * playNo : 33
     * thumbsUpNo : 1
     */

    private String commentNo;
    private String isThumbsUp;
    private String playNo;
    private String thumbsUpNo;

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCommentNo() {
        return commentNo;
    }

    public void setCommentNo(String commentNo) {
        this.commentNo = commentNo;
    }

    public String getIsThumbsUp() {
        return isThumbsUp;
    }

    public void setIsThumbsUp(String isThumbsUp) {
        this.isThumbsUp = isThumbsUp;
    }

    public String getPlayNo() {
        return playNo;
    }

    public void setPlayNo(String playNo) {
        this.playNo = playNo;
    }

    public String getThumbsUpNo() {
        return thumbsUpNo;
    }

    public void setThumbsUpNo(String thumbsUpNo) {
        this.thumbsUpNo = thumbsUpNo;
    }
}

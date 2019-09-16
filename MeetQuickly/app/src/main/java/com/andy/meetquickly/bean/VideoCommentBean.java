package com.andy.meetquickly.bean;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/6/15 15:27
 * 描述：
 */
public class VideoCommentBean {


    /**
     * commentTotal : 5
     * content : how我
     * createTime : 2019-06-14 17:20:27
     * face : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/5/7/402881e76a9203be016a9203bea00000.jpg
     * id : 7
     * imgsId : 5077
     * isRead : 0
     * nickName : Cools
     * status : 1
     * toUid : null
     * uId : 20005614
     */

    private int commentTotal;
    private String content;
    private String createTime;
    private String face;
    private String id;
    private String imgsId;
    private String isRead;
    private String nickName;
    private String status;
    private Object toUid;
    private String uId;

    public int getCommentTotal() {
        return commentTotal;
    }

    public void setCommentTotal(int commentTotal) {
        this.commentTotal = commentTotal;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgsId() {
        return imgsId;
    }

    public void setImgsId(String imgsId) {
        this.imgsId = imgsId;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getToUid() {
        return toUid;
    }

    public void setToUid(Object toUid) {
        this.toUid = toUid;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }
}

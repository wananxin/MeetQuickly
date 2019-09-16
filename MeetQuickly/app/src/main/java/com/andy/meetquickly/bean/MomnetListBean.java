package com.andy.meetquickly.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/28 17:00
 * 描述：
 */
public class MomnetListBean implements Serializable {


    /**
     * age : 20
     * auditBy : null
     * auditStatus : null
     * auditTime : null
     * birthday : 1998-12-27
     * commentNum : 0
     * content : 沃趣F4-彭鑫
     * createTime : 1555467512
     * distance : null
     * face : http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15329014109534.jpg
     * id : 402881f36a2916aa016a2916aa3d0000
     * isThumbsUp : 0
     * latitude : null
     * location : null
     * longitude : null
     * nickName : 韩冰
     * praiseNum : 2
     * rejectReason : null
     * sex : 2
     * status : null
     * type : 1
     * uId : 20000333
     * updateTime : null
     * userDynamicFiles : [{"createTime":"1555467516","dynamicId":"402881f36a2916aa016a2916aa3d0000","id":"3","sort":"0","status":"1","thumb":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/4/17/402881f36a2916aa016a2916ab830002.jpg","uId":"20000333","url":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/4/17/402881f36a2916aa016a2916ab830001.jpg"},{"createTime":"1555467516","dynamicId":"402881f36a2916aa016a2916aa3d0000","id":"4","sort":"1","status":"1","thumb":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/4/17/402881f36a2916aa016a2916b24b0004.jpg","uId":"20000333","url":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/4/17/402881f36a2916aa016a2916b24b0003.jpg"},{"createTime":"1555467516","dynamicId":"402881f36a2916aa016a2916aa3d0000","id":"5","sort":"2","status":"1","thumb":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/4/17/402881f36a2916aa016a2916b50b0006.jpg","uId":"20000333","url":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/4/17/402881f36a2916aa016a2916b50b0005.jpg"}]
     */

    private int age;
    private Object auditBy;
    private Object auditStatus;
    private Object auditTime;
    private String birthday;
    private String commentNum;
    private String content;
    private String createTime;
    private String distance;
    private String face;
    private String id;
    private String isThumbsUp;
    private Object latitude;
    private Object location;
    private Object longitude;
    private String nickName;
    private String praiseNum;
    private Object rejectReason;
    private String replyContent;
    private String sex;
    private Object status;
    private String type;
    private String uId;
    private Object updateTime;
    private List<UserDynamicFilesBean> userDynamicFiles;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Object getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(Object auditBy) {
        this.auditBy = auditBy;
    }

    public Object getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Object auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Object getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Object auditTime) {
        this.auditTime = auditTime;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
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

    public String getIsThumbsUp() {
        return isThumbsUp;
    }

    public void setIsThumbsUp(String isThumbsUp) {
        this.isThumbsUp = isThumbsUp;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }

    public Object getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(Object rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
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

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public List<UserDynamicFilesBean> getUserDynamicFiles() {
        return userDynamicFiles;
    }

    public void setUserDynamicFiles(List<UserDynamicFilesBean> userDynamicFiles) {
        this.userDynamicFiles = userDynamicFiles;
    }

    public static class UserDynamicFilesBean implements Serializable {
        /**
         * createTime : 1555467516
         * dynamicId : 402881f36a2916aa016a2916aa3d0000
         * id : 3
         * sort : 0
         * status : 1
         * thumb : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/4/17/402881f36a2916aa016a2916ab830002.jpg
         * uId : 20000333
         * url : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/4/17/402881f36a2916aa016a2916ab830001.jpg
         */

        private String createTime;
        private String dynamicId;
        private String id;
        private String sort;
        private String status;
        private String thumb;
        private String uId;
        private String url;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDynamicId() {
            return dynamicId;
        }

        public void setDynamicId(String dynamicId) {
            this.dynamicId = dynamicId;
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
    }
}

package com.andy.meetquickly.bean;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/9 16:06
 * 描述：
 */
public class MessageCardBean {

    /**
     * nickName : 一个新人
     * visitCount : 0
     * id : 4117
     * userVisit : []
     * uid : 20005614
     * age : 0
     * face : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/5/7/402881e76a9203be016a9203bea00000.jpg
     */

    private String nickName;
    private String uid;
    private String face;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }
}

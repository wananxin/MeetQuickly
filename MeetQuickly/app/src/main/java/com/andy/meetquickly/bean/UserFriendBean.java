package com.andy.meetquickly.bean;

import com.andy.meetquickly.utils.StringUtil;
import com.andy.meetquickly.view.indexbar.Abbreviated;
import com.andy.meetquickly.view.indexbar.ContactsUtils;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/10 16:59
 * 描述：
 */
public class UserFriendBean  implements Abbreviated, Comparable<UserFriendBean>{


    /**
     * age : 0
     * birthday : null
     * company : null
     * distance : null
     * face : http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15433178944940.jpg
     * height : null
     * homeCity : null
     * homeDistrict : null
     * homeProvince : null
     * id : 3438
     * isAttention : null
     * level : null
     * mobile : null
     * nickName :    皇家一号
     * place : null
     * sex : 1
     * uid : 20000253
     * userVisit : []
     * visitCount : 0
     * weight : null
     */

    private  int age;
    private  String face;
    private  String nickName;
    private  String sex;
    private  String uid;
    private  String uId;
    private String id;
    private  String mAbbreviation;
    private  String mInitial;
    private  String isAttention;

    public UserFriendBean(int age, String face, String name, String sex, String uid){
        this.age = age;
        this.face = face;
        this.nickName = name;
        this.sex = sex;
        this.uid = uid;
        this.uId = uid;
        this.mAbbreviation = ContactsUtils.getAbbreviation(name);
        this.mInitial = mAbbreviation.substring(0, 1);
    }

    public UserFriendBean(){

    }

    public String getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(String isAttention) {
        this.isAttention = isAttention;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        this.mAbbreviation = ContactsUtils.getAbbreviation(nickName);
        this.mInitial = mAbbreviation.substring(0, 1);
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getAge() {
        return age;
    }

    public String getFace() {
        return face;
    }


    public String getNickName() {
        return nickName;
    }


    public String getSex() {
        return sex;
    }


    public String getUid() {
        return uid;
    }

    @Override
    public String getInitial() {
        return mInitial;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    @Override
    public int compareTo(UserFriendBean r) {
        if (StringUtil.isNotNull(mAbbreviation)) {
            if (mAbbreviation.equals(r.mAbbreviation)) {
                return 0;
            }
            boolean flag;
            if ((flag = mAbbreviation.startsWith("#")) ^ r.mAbbreviation.startsWith("#")) {
                return flag ? 1 : -1;
            }
            return getInitial().compareTo(r.getInitial());
        } else {
            return -1;
        }
    }

}

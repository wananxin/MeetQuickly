package com.andy.meetquickly.bean;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/29 19:12
 * 描述：
 */
public class UserBaseBean {


    /**
     * age : 0
     * birthday : 2018-06-26
     * company : null
     * distance : null
     * face : http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15352223117354.jpg
     * height : 165
     * homeCity : null
     * homeDistrict : null
     * homeProvince : null
     * id : null
     * isAttention : null
     * level : 11
     * nickName : 饭小猫
     * place : null
     * sex : 2
     * uid : 20000272
     * userVisit : [{"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/","friendUid":"20000272","id":"852","uId":"20005599","visitTime":"2019-03-22 23:03:06"},{"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/","friendUid":"20000272","id":"743","uId":"20005583","visitTime":"2019-03-16 14:03:53"},{"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15439785303906.jpg","friendUid":"20000272","id":"693","uId":"20005346","visitTime":"2019-03-12 06:03:05"},{"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/http://pic-huanyipi.oss-cn-beijing.aliyuncs.com/image/a2ba4ac99adee5c2e27cb0fce5610ba7d3b9d9b6.jpg","friendUid":"20000272","id":"680","uId":"20005576","visitTime":"2019-03-10 22:03:33"},{"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/http://pic-huanyipi.oss-cn-beijing.aliyuncs.com/image/c5ac673bcb8043a9d462659228f2207efe5f6472.jpg","friendUid":"20000272","id":"677","uId":"20005519","visitTime":"2019-03-10 01:03:05"},{"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15348091851449.jpg","friendUid":"20000272","id":"635","uId":"20004793","visitTime":"2019-03-07 08:03:15"},{"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15513569818014.jpg","friendUid":"20000272","id":"474","uId":"20005546","visitTime":"2019-02-28 20:02:25"},{"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15313979104025.jpg","friendUid":"20000272","id":"466","uId":"20000851","visitTime":"2019-02-28 15:02:51"},{"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15435751697113.jpg","friendUid":"20000272","id":"418","uId":"20003124","visitTime":"2019-02-23 23:02:32"},{"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/","friendUid":"20000272","id":"147","uId":"20000868","visitTime":"2019-01-23 18:01:43"}]
     * visitCount : 10
     * weight : 50
     */

    private int age;
    private String birthday;
    private Object company;
    private Object distance;
    private String face;
    private String height;
    private Object homeCity;
    private Object homeDistrict;
    private Object homeProvince;
    private Object id;
    private String isAttention;
    private String level;
    private String nickName;
    private Object place;
    private String sex;
    private String uid;
    private int visitCount;
    private String weight;
    private String registTime;
    private List<UserVisitBean> userVisit;


    public String getRegistTime() {
        return registTime;
    }

    public void setRegistTime(String registTime) {
        this.registTime = registTime;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Object getCompany() {
        return company;
    }

    public void setCompany(Object company) {
        this.company = company;
    }

    public Object getDistance() {
        return distance;
    }

    public void setDistance(Object distance) {
        this.distance = distance;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Object getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(Object homeCity) {
        this.homeCity = homeCity;
    }

    public Object getHomeDistrict() {
        return homeDistrict;
    }

    public void setHomeDistrict(Object homeDistrict) {
        this.homeDistrict = homeDistrict;
    }

    public Object getHomeProvince() {
        return homeProvince;
    }

    public void setHomeProvince(Object homeProvince) {
        this.homeProvince = homeProvince;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(String isAttention) {
        this.isAttention = isAttention;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Object getPlace() {
        return place;
    }

    public void setPlace(Object place) {
        this.place = place;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public List<UserVisitBean> getUserVisit() {
        return userVisit;
    }

    public void setUserVisit(List<UserVisitBean> userVisit) {
        this.userVisit = userVisit;
    }

    public static class UserVisitBean {
        /**
         * face : http://replace-i.oss-cn-hangzhou.aliyuncs.com/
         * friendUid : 20000272
         * id : 852
         * uId : 20005599
         * visitTime : 2019-03-22 23:03:06
         */

        private String face;
        private String friendUid;
        private String id;
        private String uId;
        private String visitTime;

        public String getFace() {
            return face;
        }

        public void setFace(String face) {
            this.face = face;
        }

        public String getFriendUid() {
            return friendUid;
        }

        public void setFriendUid(String friendUid) {
            this.friendUid = friendUid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }

        public String getVisitTime() {
            return visitTime;
        }

        public void setVisitTime(String visitTime) {
            this.visitTime = visitTime;
        }
    }
}

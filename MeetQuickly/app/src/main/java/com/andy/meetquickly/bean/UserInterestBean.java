package com.andy.meetquickly.bean;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/29 19:15
 * 描述：
 */
public class UserInterestBean {


    private List<TagsBean> tags;
    private List<MusicsBean> musics;
    private List<BooksBean> books;
    private List<UserBaseInfoBean> userBaseInfo;
    private List<VacationBean> vacation;
    private List<FoodsBean> foods;
    private List<SportsBean> sports;
    private List<?> managerStore;
    private List<FilmsBean> films;
    private List<?> industry;
    private List<?> works;

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public List<MusicsBean> getMusics() {
        return musics;
    }

    public void setMusics(List<MusicsBean> musics) {
        this.musics = musics;
    }

    public List<BooksBean> getBooks() {
        return books;
    }

    public void setBooks(List<BooksBean> books) {
        this.books = books;
    }

    public List<UserBaseInfoBean> getUserBaseInfo() {
        return userBaseInfo;
    }

    public void setUserBaseInfo(List<UserBaseInfoBean> userBaseInfo) {
        this.userBaseInfo = userBaseInfo;
    }

    public List<VacationBean> getVacation() {
        return vacation;
    }

    public void setVacation(List<VacationBean> vacation) {
        this.vacation = vacation;
    }

    public List<FoodsBean> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodsBean> foods) {
        this.foods = foods;
    }

    public List<SportsBean> getSports() {
        return sports;
    }

    public void setSports(List<SportsBean> sports) {
        this.sports = sports;
    }

    public List<?> getManagerStore() {
        return managerStore;
    }

    public void setManagerStore(List<?> managerStore) {
        this.managerStore = managerStore;
    }

    public List<FilmsBean> getFilms() {
        return films;
    }

    public void setFilms(List<FilmsBean> films) {
        this.films = films;
    }

    public List<?> getIndustry() {
        return industry;
    }

    public void setIndustry(List<?> industry) {
        this.industry = industry;
    }

    public List<?> getWorks() {
        return works;
    }

    public void setWorks(List<?> works) {
        this.works = works;
    }

    public static class TagsBean {
        /**
         * addTime : 2018-08-29 15:08:16
         * content : 勇敢
         * id : 13263
         * type : 9
         * uId : 20000272
         */

        private String addTime;
        private String content;
        private String id;
        private String type;
        private String uId;

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }
    }

    public static class MusicsBean {
        /**
         * addTime : 2018-08-29 15:08:16
         * content : 民歌
         * id : 13269
         * type : 4
         * uId : 20000272
         */

        private String addTime;
        private String content;
        private String id;
        private String type;
        private String uId;

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }
    }

    public static class BooksBean {
        /**
         * addTime : 2018-08-29 15:08:16
         * content : 神雕侠侣
         * id : 13274
         * type : 7
         * uId : 20000272
         */

        private String addTime;
        private String content;
        private String id;
        private String type;
        private String uId;

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }
    }

    public static class UserBaseInfoBean {
        /**
         * age : 0
         * birthday : 2018-06-26
         * company : 皇家永利ktv
         * distance : null
         * face : http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15352223117354.jpg
         * height : 165
         * homeCity : 益阳市
         * homeDistrict : 沅江市
         * homeProvince : 湖南省
         * id : null
         * isAttention : null
         * level : 11
         * nickName : 饭小猫
         * place :
         * sex : 2
         * uid : 20000272
         * userVisit : []
         * visitCount : 0
         * weight : 50
         */

        private int age;
        private String birthday;
        private String company;
        private Object distance;
        private String face;
        private String height;
        private String homeCity;
        private String homeDistrict;
        private String homeProvince;
        private Object id;
        private Object isAttention;
        private String level;
        private String nickName;
        private String place;
        private String sex;
        private String uid;
        private int visitCount;
        private String weight;
        private List<?> userVisit;

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

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
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

        public String getHomeCity() {
            return homeCity;
        }

        public void setHomeCity(String homeCity) {
            this.homeCity = homeCity;
        }

        public String getHomeDistrict() {
            return homeDistrict;
        }

        public void setHomeDistrict(String homeDistrict) {
            this.homeDistrict = homeDistrict;
        }

        public String getHomeProvince() {
            return homeProvince;
        }

        public void setHomeProvince(String homeProvince) {
            this.homeProvince = homeProvince;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public Object getIsAttention() {
            return isAttention;
        }

        public void setIsAttention(Object isAttention) {
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

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
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

        public List<?> getUserVisit() {
            return userVisit;
        }

        public void setUserVisit(List<?> userVisit) {
            this.userVisit = userVisit;
        }
    }

    public static class VacationBean {
        /**
         * addTime : 2018-08-29 15:08:16
         * content : 长沙
         * id : 13276
         * type : 8
         * uId : 20000272
         */

        private String addTime;
        private String content;
        private String id;
        private String type;
        private String uId;

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }
    }

    public static class FoodsBean {
        /**
         * addTime : 2018-08-29 15:08:16
         * content : 香辣小龙虾
         * id : 13270
         * type : 5
         * uId : 20000272
         */

        private String addTime;
        private String content;
        private String id;
        private String type;
        private String uId;

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }
    }

    public static class SportsBean {
        /**
         * addTime : 2018-08-29 15:08:16
         * content : 登山
         * id : 13267
         * type : 3
         * uId : 20000272
         */

        private String addTime;
        private String content;
        private String id;
        private String type;
        private String uId;

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }
    }

    public static class FilmsBean {
        /**
         * addTime : 2018-08-29 15:08:16
         * content : 侏罗纪世界2
         * id : 13271
         * type : 6
         * uId : 20000272
         */

        private String addTime;
        private String content;
        private String id;
        private String type;
        private String uId;

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }
    }
}

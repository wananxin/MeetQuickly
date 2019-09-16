package com.andy.meetquickly.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/28 14:42
 * 描述：
 */
public class UserDataInfoBean implements Serializable {


    private List<CommenBean> tags;
    private List<CommenBean> musics;
    private List<UserBaseInfoBean> userBaseInfo;
    private List<CommenBean> books;
    private List<GiftsEntityBean> giftsEntity;
    private List<CommenBean> sports;
    private List<ManagerStoreBean> managerStore;
    private List<CommenBean> films;
    private List<CommenBean> works;
    private List<CommenBean> foods;
    private List<CommenBean> vacation;
    private List<CommenBean> industry;
    private List<CommenBean> commonInterest;

    public List<CommenBean> getTags() {
        return tags;
    }

    public void setTags(List<CommenBean> tags) {
        this.tags = tags;
    }

    public List<CommenBean> getMusics() {
        return musics;
    }

    public void setMusics(List<CommenBean> musics) {
        this.musics = musics;
    }

    public List<UserBaseInfoBean> getUserBaseInfo() {
        return userBaseInfo;
    }

    public void setUserBaseInfo(List<UserBaseInfoBean> userBaseInfo) {
        this.userBaseInfo = userBaseInfo;
    }

    public List<CommenBean> getBooks() {
        return books;
    }

    public void setBooks(List<CommenBean> books) {
        this.books = books;
    }

    public List<GiftsEntityBean> getGiftsEntity() {
        return giftsEntity;
    }

    public void setGiftsEntity(List<GiftsEntityBean> giftsEntity) {
        this.giftsEntity = giftsEntity;
    }

    public List<CommenBean> getSports() {
        return sports;
    }

    public void setSports(List<CommenBean> sports) {
        this.sports = sports;
    }

    public List<ManagerStoreBean> getManagerStore() {
        return managerStore;
    }

    public void setManagerStore(List<ManagerStoreBean> managerStore) {
        this.managerStore = managerStore;
    }

    public List<CommenBean> getFilms() {
        return films;
    }

    public void setFilms(List<CommenBean> films) {
        this.films = films;
    }

    public List<CommenBean> getWorks() {
        return works;
    }

    public void setWorks(List<CommenBean> works) {
        this.works = works;
    }

    public List<CommenBean> getFoods() {
        return foods;
    }

    public void setFoods(List<CommenBean> foods) {
        this.foods = foods;
    }

    public List<CommenBean> getVacation() {
        return vacation;
    }

    public void setVacation(List<CommenBean> vacation) {
        this.vacation = vacation;
    }

    public List<CommenBean> getIndustry() {
        return industry;
    }

    public void setIndustry(List<CommenBean> industry) {
        this.industry = industry;
    }

    public List<CommenBean> getCommonInterest() {
        return commonInterest;
    }

    public void setCommonInterest(List<CommenBean> commonInterest) {
        this.commonInterest = commonInterest;
    }

    public static class UserBaseInfoBean {
        /**
         * age : 10
         * birthday : 2009-05-24
         * commissionCount : null
         * company : Q1
         * distance : null
         * face : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/5/7/402881e76a9203be016a9203bea00000.jpg
         * groupLevel : null
         * height : 150
         * homeCity : 市辖区
         * homeDistrict : 东城区
         * homeProvince : 北京
         * id : null
         * income : null
         * isAttention : null
         * level : 1
         * mobile : null
         * nickName : Cools
         * place :
         * recommenderNickName : null
         * referrerId : 0
         * sex : 1
         * storeId : null
         * time : null
         * type : null
         * uid : 20005614
         * userRole : 1
         * userVisit : []
         * visitCount : 0
         * weight : 55
         */

        private int age;
        private String birthday;
        private Object commissionCount;
        private String company;
        private Object distance;
        private String face;
        private Object groupLevel;
        private String height;
        private String homeCity;
        private String homeDistrict;
        private String homeProvince;
        private Object id;
        private Object income;
        private Object isAttention;
        private String level;
        private Object mobile;
        private String nickName;
        private String place;
        private Object recommenderNickName;
        private int referrerId;
        private String sex;
        private Object storeId;
        private Object time;
        private Object type;
        private String uid;
        private String userRole;
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

        public Object getCommissionCount() {
            return commissionCount;
        }

        public void setCommissionCount(Object commissionCount) {
            this.commissionCount = commissionCount;
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

        public Object getGroupLevel() {
            return groupLevel;
        }

        public void setGroupLevel(Object groupLevel) {
            this.groupLevel = groupLevel;
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

        public Object getIncome() {
            return income;
        }

        public void setIncome(Object income) {
            this.income = income;
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

        public Object getMobile() {
            return mobile;
        }

        public void setMobile(Object mobile) {
            this.mobile = mobile;
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

        public Object getRecommenderNickName() {
            return recommenderNickName;
        }

        public void setRecommenderNickName(Object recommenderNickName) {
            this.recommenderNickName = recommenderNickName;
        }

        public int getReferrerId() {
            return referrerId;
        }

        public void setReferrerId(int referrerId) {
            this.referrerId = referrerId;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Object getStoreId() {
            return storeId;
        }

        public void setStoreId(Object storeId) {
            this.storeId = storeId;
        }

        public Object getTime() {
            return time;
        }

        public void setTime(Object time) {
            this.time = time;
        }

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUserRole() {
            return userRole;
        }

        public void setUserRole(String userRole) {
            this.userRole = userRole;
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

    public static class GiftsEntityBean implements Serializable{
        /**
         * addTime : 2018-06-20 16:52:32.0
         * id : 1055
         * money : 2.00
         * name : 么么哒
         * num : 1
         * sort : 2
         * type : 1
         * url : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/gift/memeda.png
         */

        private String addTime;
        private String id;
        private String money;
        private String name;
        private String num;
        private String sort;
        private String type;
        private String url;

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

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
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
    }

    public static class ManagerStoreBean {
        /**
         * activityName : null
         * address : null
         * appVersion : null
         * area : null
         * auditTime : null
         * auditUser : null
         * businessTime : null
         * cashCouponReturn : null
         * cashCouponReturnManager : null
         * cashCouponReturnUser : null
         * city : null
         * companyId : null
         * companyName : 万代
         * coverImg : http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15427040751966.jpg
         * createTime : null
         * discount : null
         * distance : null
         * feature : null
         * id : 22
         * isDelete : null
         * isFreeDriving : null
         * isHot : null
         * isNew : null
         * isOnline : null
         * kind : null
         * mobile : null
         * placeScore : null
         * province : null
         * reason : null
         * remark : null
         * satisfiedScore : null
         * serviceScore : null
         * sort : null
         * status : null
         * storeId : 1105
         * storeName : 小丹的小店
         * storeVisit : null
         * uId : null
         * userInfo : null
         */

        private Object activityName;
        private Object address;
        private Object appVersion;
        private Object area;
        private Object auditTime;
        private Object auditUser;
        private Object businessTime;
        private Object cashCouponReturn;
        private Object cashCouponReturnManager;
        private Object cashCouponReturnUser;
        private Object city;
        private Object companyId;
        private String companyName;
        private String coverImg;
        private Object createTime;
        private Object discount;
        private Object distance;
        private Object feature;
        private String id;
        private Object isDelete;
        private Object isFreeDriving;
        private Object isHot;
        private Object isNew;
        private Object isOnline;
        private String kind;
        private Object mobile;
        private Object placeScore;
        private Object province;
        private Object reason;
        private Object remark;
        private Object satisfiedScore;
        private Object serviceScore;
        private Object sort;
        private Object status;
        private String storeId;
        private String storeName;
        private Object storeVisit;
        private Object uId;
        private Object userInfo;

        public Object getActivityName() {
            return activityName;
        }

        public void setActivityName(Object activityName) {
            this.activityName = activityName;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public Object getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(Object appVersion) {
            this.appVersion = appVersion;
        }

        public Object getArea() {
            return area;
        }

        public void setArea(Object area) {
            this.area = area;
        }

        public Object getAuditTime() {
            return auditTime;
        }

        public void setAuditTime(Object auditTime) {
            this.auditTime = auditTime;
        }

        public Object getAuditUser() {
            return auditUser;
        }

        public void setAuditUser(Object auditUser) {
            this.auditUser = auditUser;
        }

        public Object getBusinessTime() {
            return businessTime;
        }

        public void setBusinessTime(Object businessTime) {
            this.businessTime = businessTime;
        }

        public Object getCashCouponReturn() {
            return cashCouponReturn;
        }

        public void setCashCouponReturn(Object cashCouponReturn) {
            this.cashCouponReturn = cashCouponReturn;
        }

        public Object getCashCouponReturnManager() {
            return cashCouponReturnManager;
        }

        public void setCashCouponReturnManager(Object cashCouponReturnManager) {
            this.cashCouponReturnManager = cashCouponReturnManager;
        }

        public Object getCashCouponReturnUser() {
            return cashCouponReturnUser;
        }

        public void setCashCouponReturnUser(Object cashCouponReturnUser) {
            this.cashCouponReturnUser = cashCouponReturnUser;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Object companyId) {
            this.companyId = companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public Object getDiscount() {
            return discount;
        }

        public void setDiscount(Object discount) {
            this.discount = discount;
        }

        public Object getDistance() {
            return distance;
        }

        public void setDistance(Object distance) {
            this.distance = distance;
        }

        public Object getFeature() {
            return feature;
        }

        public void setFeature(Object feature) {
            this.feature = feature;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(Object isDelete) {
            this.isDelete = isDelete;
        }

        public Object getIsFreeDriving() {
            return isFreeDriving;
        }

        public void setIsFreeDriving(Object isFreeDriving) {
            this.isFreeDriving = isFreeDriving;
        }

        public Object getIsHot() {
            return isHot;
        }

        public void setIsHot(Object isHot) {
            this.isHot = isHot;
        }

        public Object getIsNew() {
            return isNew;
        }

        public void setIsNew(Object isNew) {
            this.isNew = isNew;
        }

        public Object getIsOnline() {
            return isOnline;
        }

        public void setIsOnline(Object isOnline) {
            this.isOnline = isOnline;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public Object getMobile() {
            return mobile;
        }

        public void setMobile(Object mobile) {
            this.mobile = mobile;
        }

        public Object getPlaceScore() {
            return placeScore;
        }

        public void setPlaceScore(Object placeScore) {
            this.placeScore = placeScore;
        }

        public Object getProvince() {
            return province;
        }

        public void setProvince(Object province) {
            this.province = province;
        }

        public Object getReason() {
            return reason;
        }

        public void setReason(Object reason) {
            this.reason = reason;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public Object getSatisfiedScore() {
            return satisfiedScore;
        }

        public void setSatisfiedScore(Object satisfiedScore) {
            this.satisfiedScore = satisfiedScore;
        }

        public Object getServiceScore() {
            return serviceScore;
        }

        public void setServiceScore(Object serviceScore) {
            this.serviceScore = serviceScore;
        }

        public Object getSort() {
            return sort;
        }

        public void setSort(Object sort) {
            this.sort = sort;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public Object getStoreVisit() {
            return storeVisit;
        }

        public void setStoreVisit(Object storeVisit) {
            this.storeVisit = storeVisit;
        }

        public Object getUId() {
            return uId;
        }

        public void setUId(Object uId) {
            this.uId = uId;
        }

        public Object getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(Object userInfo) {
            this.userInfo = userInfo;
        }
    }

    public static class CommenBean {
        /**
         * addTime : 2019-05-27 10:05:37
         * content : 互联网、计算机、电讯业
         * id : 18253
         * type : 1
         * uId : 20005614
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

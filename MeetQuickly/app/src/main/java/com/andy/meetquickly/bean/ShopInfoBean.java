package com.andy.meetquickly.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/28 11:02
 * 描述：
 */
public class ShopInfoBean implements Serializable{


    /**
     * activityName : 免包厢费，进店开机送一打酒
     * address : 芙蓉路
     * appVersion : null
     * area : 开福区
     * auditTime : null
     * auditUser : null
     * businessTime : 12:00-05:00
     * cashCouponReturn : null
     * cashCouponReturnManager : null
     * cashCouponReturnUser : null
     * city : 长沙
     * companyId : null
     * companyName : 万代
     * coverImg : http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15427040751966.jpg
     * createTime : 2018-10-18 09:10:00
     * discount : 2
     * distance : null
     * feature : 各种特色游戏
     * id : 1027
     * isDelete : null
     * isFreeDriving : null
     * isHot : null
     * isNew : null
     * isOnline : 1
     * kind : 1
     * mobile : 18890057006
     * placeScore : null
     * province : 湖南
     * reason : null
     * remark : 服务与游戏创新一体一家好玩的ktv，能让你心灵释放的地方。
     * satisfiedScore : null
     * serviceScore : null
     * sort : null
     * status : null
     * storeId : 1105
     * storeName : 小丹的小店
     * storeVisit : null
     * uId : 20000241
     */

    private String activityName;
    private String address;
    private Object appVersion;
    private String area;
    private Object auditTime;
    private Object auditUser;
    private String businessTime;
    private String cashCouponReturn;
    private String cashCouponProportion;
    private String cashCouponReturnManager;
    private String cashCouponReturnUser;
    private String city;
    private Object companyId;
    private String companyName;
    private String couponAmount;
    private String coverImg;
    private String createTime;
    private String discount;
    private String distance;
    private String feature;
    private String id;
    private Object isDelete;
    private String isFreeDriving;
    private Object isHot;
    private Object isNew;
    private String isOnline;
    private String kind;
    private String mobile;
    private Object placeScore;
    private String province;
    private Object reason;
    private String remark;
    private Object satisfiedScore;
    private Object serviceScore;
    private Object sort;
    private Object status;

    private String storeId;
    private String storeName;
    private Object storeVisit;
    private String uId;
    private List<UserBean> userInfo;
    /**
     * appVersion : null
     * auditTime : null
     * auditUser : null
     * cashCouponReturn : null
     * cashCouponReturnManager : null
     * cashCouponReturnUser : null
     * companyId : null
     * couponAmount : null
     * distance : null
     * isCoupon : 0
     * isDelete : null
     * isFreeDriving : null
     * isHot : null
     * isNew : null
     * placeScore : null
     * reason : null
     * satisfiedScore : null
     * serviceScore : null
     * sort : null
     * status : null
     * storeCoupon : {"availableNo":"20","beginTime":"2019-06-05","couponAmount":"200","createTime":"2019-06-05 17:06:32","endTime":"2019-06-05","havaRecevied":"7","id":"1","isOverdue":"0","storeId":"1105","type":"1"}
     * storeImgs : []
     * storeVisit : null
     * userInfo : [{"accessToken":"6ebbf7512facbd0ce139f49e9a235248","addfriendSecond":null,"address":null,"age":0,"agentUid":null,"allProfit":null,"allWithDrawals":null,"amount":null,"auth":null,"balance":null,"beaddfriendSecond":null,"becomplainsSecond":null,"bedifferenceSecond":null,"bepraiseSecond":null,"bereportSecond":null,"birthday":null,"birthdays":null,"blackList":null,"cashCoupon":null,"cityActivity":null,"commission":null,"company":null,"complainsSecond":null,"consumption":null,"contact":null,"contactId":null,"dateCompany":null,"differenceSecond":null,"districtActivity":null,"exp":null,"fabulousSecond":null,"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15300087598851.JPG","fansNum":null,"gesturesPassword":null,"gesturesStatus":null,"groupId":null,"groupLevel":"0","height":null,"homeCity":null,"homeDistrict":null,"homeProvince":null,"id":null,"industry":null,"inviteCode":"","isAgent":null,"isDel":null,"isFace":null,"isHot":null,"isOnline":null,"isPerfect":"1","isSynHuanxin":null,"latitude":null,"level":"8","longitude":null,"managerUid":null,"mobile":"13647438641","nickName":"夜空的静静","numberFriends":null,"onlineTime":null,"parentUid":null,"password":"xinyue520","place":null,"praiseSecond":null,"provinceActivity":null,"qqOpenId":null,"qrCode":"/uploads/15301097562844.jpg","rechargeVip":null,"referrerId":null,"registIp":null,"registTime":"2018-06-26 16:06:10","reportSecond":null,"role":null,"rongyunToken":null,"secondReferrerId":null,"securityAnswer":null,"securityQuestion":null,"sex":"2","shieldlist":null,"signSecond":null,"storeId":null,"temporaryBalance":null,"thirdReferrerId":null,"treadSecond":null,"uId":"20000268","userName":null,"userRole":null,"voiceSignature":null,"weight":null,"withdrawPassword":null,"workField":null,"wxOpenId":null},{"accessToken":"954428e66bc1a9b133ef07d7741d6e31","addfriendSecond":null,"address":null,"age":0,"agentUid":null,"allProfit":null,"allWithDrawals":null,"amount":null,"auth":null,"balance":null,"beaddfriendSecond":null,"becomplainsSecond":null,"bedifferenceSecond":null,"bepraiseSecond":null,"bereportSecond":null,"birthday":null,"birthdays":null,"blackList":null,"cashCoupon":null,"cityActivity":null,"commission":null,"company":null,"complainsSecond":null,"consumption":null,"contact":null,"contactId":null,"dateCompany":null,"differenceSecond":null,"districtActivity":null,"exp":null,"fabulousSecond":null,"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15300103034197.jpg","fansNum":null,"gesturesPassword":null,"gesturesStatus":null,"groupId":null,"groupLevel":"0","height":null,"homeCity":null,"homeDistrict":null,"homeProvince":null,"id":null,"industry":null,"inviteCode":"287114","isAgent":null,"isDel":null,"isFace":null,"isHot":null,"isOnline":null,"isPerfect":"1","isSynHuanxin":null,"latitude":null,"level":"8","longitude":null,"managerUid":null,"mobile":"15673582222","nickName":"国贸ktv张洪","numberFriends":null,"onlineTime":null,"parentUid":null,"password":"zhanghong5858","place":null,"praiseSecond":null,"provinceActivity":null,"qqOpenId":null,"qrCode":"/uploads/15306255143907.jpg","rechargeVip":null,"referrerId":null,"registIp":null,"registTime":"2018-06-26 16:06:19","reportSecond":null,"role":null,"rongyunToken":null,"secondReferrerId":null,"securityAnswer":null,"securityQuestion":null,"sex":"1","shieldlist":null,"signSecond":null,"storeId":null,"temporaryBalance":null,"thirdReferrerId":null,"treadSecond":null,"uId":"20000270","userName":null,"userRole":null,"voiceSignature":null,"weight":null,"withdrawPassword":null,"workField":null,"wxOpenId":null},{"accessToken":"707df0a0e6628e7a92b50c962479fec4","addfriendSecond":null,"address":null,"age":0,"agentUid":null,"allProfit":null,"allWithDrawals":null,"amount":null,"auth":null,"balance":null,"beaddfriendSecond":null,"becomplainsSecond":null,"bedifferenceSecond":null,"bepraiseSecond":null,"bereportSecond":null,"birthday":null,"birthdays":null,"blackList":null,"cashCoupon":null,"cityActivity":null,"commission":null,"company":null,"complainsSecond":null,"consumption":null,"contact":null,"contactId":null,"dateCompany":null,"differenceSecond":null,"districtActivity":null,"exp":null,"fabulousSecond":null,"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/5/7/402881e76a9203be016a9203bea00000.jpg","fansNum":null,"gesturesPassword":null,"gesturesStatus":null,"groupId":null,"groupLevel":"0","height":null,"homeCity":null,"homeDistrict":null,"homeProvince":null,"id":null,"industry":null,"inviteCode":"109429","isAgent":null,"isDel":null,"isFace":null,"isHot":null,"isOnline":null,"isPerfect":"0","isSynHuanxin":null,"latitude":null,"level":"2","longitude":null,"managerUid":null,"mobile":"18229906596","nickName":"快逅1号","numberFriends":null,"onlineTime":null,"parentUid":null,"password":"zm08070810","place":null,"praiseSecond":null,"provinceActivity":null,"qqOpenId":null,"qrCode":"","rechargeVip":null,"referrerId":null,"registIp":null,"registTime":"2018-06-26 21:06:06","reportSecond":null,"role":null,"rongyunToken":null,"secondReferrerId":null,"securityAnswer":null,"securityQuestion":null,"sex":"1","shieldlist":null,"signSecond":null,"storeId":null,"temporaryBalance":null,"thirdReferrerId":null,"treadSecond":null,"uId":"20000340","userName":null,"userRole":null,"voiceSignature":null,"weight":null,"withdrawPassword":null,"workField":null,"wxOpenId":null},{"accessToken":"440736a89067b17dbe43c1a1fb18c0b2","addfriendSecond":null,"address":null,"age":0,"agentUid":null,"allProfit":null,"allWithDrawals":null,"amount":null,"auth":null,"balance":null,"beaddfriendSecond":null,"becomplainsSecond":null,"bedifferenceSecond":null,"bepraiseSecond":null,"bereportSecond":null,"birthday":null,"birthdays":null,"blackList":null,"cashCoupon":null,"cityActivity":null,"commission":null,"company":null,"complainsSecond":null,"consumption":null,"contact":null,"contactId":null,"dateCompany":null,"differenceSecond":null,"districtActivity":null,"exp":null,"fabulousSecond":null,"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com//uploads/15300602328407.jpg","fansNum":null,"gesturesPassword":null,"gesturesStatus":null,"groupId":null,"groupLevel":"0","height":null,"homeCity":null,"homeDistrict":null,"homeProvince":null,"id":null,"industry":null,"inviteCode":"","isAgent":null,"isDel":null,"isFace":null,"isHot":null,"isOnline":null,"isPerfect":"0","isSynHuanxin":null,"latitude":null,"level":"6","longitude":null,"managerUid":null,"mobile":"15573807150","nickName":"鹏","numberFriends":null,"onlineTime":null,"parentUid":null,"password":"999999a","place":null,"praiseSecond":null,"provinceActivity":null,"qqOpenId":null,"qrCode":"/uploads/15300909439852.jpg","rechargeVip":null,"referrerId":null,"registIp":null,"registTime":"2018-06-26 22:06:28","reportSecond":null,"role":null,"rongyunToken":null,"secondReferrerId":null,"securityAnswer":null,"securityQuestion":null,"sex":"1","shieldlist":null,"signSecond":null,"storeId":null,"temporaryBalance":null,"thirdReferrerId":null,"treadSecond":null,"uId":"20000341","userName":null,"userRole":null,"voiceSignature":null,"weight":null,"withdrawPassword":null,"workField":null,"wxOpenId":null},{"accessToken":"8gDykrhBmGi6DpBanlt0ow==","addfriendSecond":null,"address":null,"age":0,"agentUid":null,"allProfit":null,"allWithDrawals":null,"amount":null,"auth":null,"balance":null,"beaddfriendSecond":null,"becomplainsSecond":null,"bedifferenceSecond":null,"bepraiseSecond":null,"bereportSecond":null,"birthday":null,"birthdays":null,"blackList":null,"cashCoupon":null,"cityActivity":null,"commission":null,"company":null,"complainsSecond":null,"consumption":null,"contact":null,"contactId":null,"dateCompany":null,"differenceSecond":null,"districtActivity":null,"exp":null,"fabulousSecond":null,"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/5/7/402881e76a9203be016a9203bea00000.jpg","fansNum":null,"gesturesPassword":null,"gesturesStatus":null,"groupId":null,"groupLevel":"1","height":null,"homeCity":null,"homeDistrict":null,"homeProvince":null,"id":null,"industry":null,"inviteCode":"V1T0GN","isAgent":null,"isDel":null,"isFace":null,"isHot":null,"isOnline":null,"isPerfect":"1","isSynHuanxin":null,"latitude":"37.785835","level":"4","longitude":"-122.406418","managerUid":null,"mobile":"15084902240","nickName":"Cools","numberFriends":null,"onlineTime":null,"parentUid":null,"password":"byHQ+jOqWSc=","place":null,"praiseSecond":null,"provinceActivity":null,"qqOpenId":null,"qrCode":"image/qRimage/2019/04/22/402881f36a43bb2d016a43bb2d450000_V1T0GN.jpg","rechargeVip":null,"referrerId":null,"registIp":null,"registTime":"2019-04-22 14:04:21","reportSecond":null,"role":null,"rongyunToken":null,"secondReferrerId":null,"securityAnswer":null,"securityQuestion":null,"sex":"1","shieldlist":null,"signSecond":null,"storeId":null,"temporaryBalance":null,"thirdReferrerId":null,"treadSecond":null,"uId":"20005614","userName":null,"userRole":null,"voiceSignature":null,"weight":null,"withdrawPassword":null,"workField":null,"wxOpenId":null},{"accessToken":"OqfaUqQ31BlvuS1xUV6wpg==","addfriendSecond":null,"address":null,"age":0,"agentUid":null,"allProfit":null,"allWithDrawals":null,"amount":null,"auth":null,"balance":null,"beaddfriendSecond":null,"becomplainsSecond":null,"bedifferenceSecond":null,"bepraiseSecond":null,"bereportSecond":null,"birthday":null,"birthdays":null,"blackList":null,"cashCoupon":null,"cityActivity":null,"commission":null,"company":null,"complainsSecond":null,"consumption":null,"contact":null,"contactId":null,"dateCompany":null,"differenceSecond":null,"districtActivity":null,"exp":null,"fabulousSecond":null,"face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/4/29/402881f36a670f5e016a6710316a0001.jpg","fansNum":null,"gesturesPassword":null,"gesturesStatus":null,"groupId":null,"groupLevel":"1","height":null,"homeCity":null,"homeDistrict":null,"homeProvince":null,"id":null,"industry":null,"inviteCode":"JES42G","isAgent":null,"isDel":null,"isFace":null,"isHot":null,"isOnline":null,"isPerfect":"1","isSynHuanxin":null,"latitude":"37.785835","level":"1","longitude":"-122.406418","managerUid":null,"mobile":"15717328325","nickName":"给你","numberFriends":null,"onlineTime":null,"parentUid":null,"password":"86XBihQMeuk=","place":null,"praiseSecond":null,"provinceActivity":null,"qqOpenId":null,"qrCode":"image/qRimage/2019/04/29/402881f36a670f5e016a6710389b0002_JES42G.jpg","rechargeVip":null,"referrerId":null,"registIp":null,"registTime":"2019-04-29 11:04:57","reportSecond":null,"role":null,"rongyunToken":null,"secondReferrerId":null,"securityAnswer":null,"securityQuestion":null,"sex":"1","shieldlist":null,"signSecond":null,"storeId":null,"temporaryBalance":null,"thirdReferrerId":null,"treadSecond":null,"uId":"20005637","userName":null,"userRole":null,"voiceSignature":null,"weight":null,"withdrawPassword":null,"workField":null,"wxOpenId":null}]
     */

    private String isCoupon;
    private StoreCouponBean storeCoupon;

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(String isCoupon) {
        this.isCoupon = isCoupon;
    }

    public StoreCouponBean getStoreCoupon() {
        return storeCoupon;
    }

    public void setStoreCoupon(StoreCouponBean storeCoupon) {
        this.storeCoupon = storeCoupon;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(Object appVersion) {
        this.appVersion = appVersion;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
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

    public String getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(String businessTime) {
        this.businessTime = businessTime;
    }

    public String getCashCouponReturn() {
        return cashCouponReturn;
    }

    public void setCashCouponReturn(String cashCouponReturn) {
        this.cashCouponReturn = cashCouponReturn;
    }

    public String getCashCouponReturnManager() {
        return cashCouponReturnManager;
    }

    public void setCashCouponReturnManager(String cashCouponReturnManager) {
        this.cashCouponReturnManager = cashCouponReturnManager;
    }

    public String getCashCouponReturnUser() {
        return cashCouponReturnUser;
    }

    public void setCashCouponReturnUser(String cashCouponReturnUser) {
        this.cashCouponReturnUser = cashCouponReturnUser;
    }

    public String getCashCouponProportion() {
        return cashCouponProportion;
    }

    public void setCashCouponProportion(String cashCouponProportion) {
        this.cashCouponProportion = cashCouponProportion;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
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

    public String getIsFreeDriving() {
        return isFreeDriving;
    }

    public void setIsFreeDriving(String isFreeDriving) {
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

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Object getPlaceScore() {
        return placeScore;
    }

    public void setPlaceScore(Object placeScore) {
        this.placeScore = placeScore;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Object getReason() {
        return reason;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
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

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public List<UserBean> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(List<UserBean> userInfo) {
        this.userInfo = userInfo;
    }

    public static class StoreCouponBean implements Serializable {
        /**
         * availableNo : 20
         * beginTime : 2019-06-05
         * couponAmount : 200
         * createTime : 2019-06-05 17:06:32
         * endTime : 2019-06-05
         * havaRecevied : 7
         * id : 1
         * isOverdue : 0
         * storeId : 1105
         * type : 1
         */

        private String availableNo;
        private String beginTime;
        @com.google.gson.annotations.SerializedName("couponAmount")
        private String couponAmountX;
        @com.google.gson.annotations.SerializedName("createTime")
        private String createTimeX;
        private String endTime;
        private String havaRecevied;
        @com.google.gson.annotations.SerializedName("id")
        private String idX;
        private String isOverdue;
        @com.google.gson.annotations.SerializedName("storeId")
        private String storeIdX;
        private String type;

        public String getAvailableNo() {
            return availableNo;
        }

        public void setAvailableNo(String availableNo) {
            this.availableNo = availableNo;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getCouponAmountX() {
            return couponAmountX;
        }

        public void setCouponAmountX(String couponAmountX) {
            this.couponAmountX = couponAmountX;
        }

        public String getCreateTimeX() {
            return createTimeX;
        }

        public void setCreateTimeX(String createTimeX) {
            this.createTimeX = createTimeX;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getHavaRecevied() {
            return havaRecevied;
        }

        public void setHavaRecevied(String havaRecevied) {
            this.havaRecevied = havaRecevied;
        }

        public String getIdX() {
            return idX;
        }

        public void setIdX(String idX) {
            this.idX = idX;
        }

        public String getIsOverdue() {
            return isOverdue;
        }

        public void setIsOverdue(String isOverdue) {
            this.isOverdue = isOverdue;
        }

        public String getStoreIdX() {
            return storeIdX;
        }

        public void setStoreIdX(String storeIdX) {
            this.storeIdX = storeIdX;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

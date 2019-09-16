package com.andy.meetquickly.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/26 13:45
 * 描述：
 */
public class UserBean {


    /**
     * accessToken : kirdaDxnaeKZDmR90mHawQ==         登陆凭证
     * addfriendSecond : null          添加好友次数
     * address : null
     * age : 27                        年龄
     * agentUid : null                 代理人UID
     * allProfit : null                 总收益
     * allWithDrawals : null            总提现金额
     * amount : null                   充值总金额
     * auth : null
     * balance : null                  用户余额
     * beaddfriendSecond : null          被添加好友次数
     * becomplainsSecond : null          被投诉记录
     * bedifferenceSecond : null          被差评次数
     * bepraiseSecond : null              被好评次数
     * bereportSecond : null              被举报次数
     * birthday : 1992-04-26           生日
     * birthdays : null
     * blackList : null                黑名单--------------------------------
     * cashCoupon : null                用户代金券总额
     * cityActivity : null              活动范围市
     * commission : null               佣金
     * company : null                 工作详细地址
     * complainsSecond : null         投诉记录
     * consumption : null             总消费金额
     * contact : null
     * dateCompany : null
     * differenceSecond : null          给差评次数
     * districtActivity : null           活动范围区
     * exp : null                     经验值
     * fabulousSecond : null          点赞次数
     * face : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/4/26/402881f36a57b564016a57b564080000.jpg
     * 头像保存路径
     * fansNum : null                 粉丝数量
     * gesturesPassword : null        手势密码
     * gesturesStatus : null          是否开启手势密码
     * groupId : null
     * groupLevel : null               0:不是推广人 1:一级团队，2:二级团队 以此类推
     * height : null                  身高
     * homeCity : null                 家乡(市)
     * homeDistrict : null              家乡(区)
     * homeProvince : null             家乡(省)
     * industry : null                 行业
     * inviteCode : G1FKO7              邀请码
     * isAgent : null                   是否缴纳升级金
     * isDel : null                     是否删除
     * isFace : null                     头像标记 0合法 1不合法
     * isHot : null                      是否推荐热门1.最优秀 2.优秀 3.良好 4.好
     * isOnline : null
     * isPerfect : 1                     是否完善资料0未完善 1已完善
     * isSynHuanxin : null              是否同步环信数据
     * latitude : null                 维度
     * level : 1                      魅力等级
     * longitude : null               经度
     * managerUid : null              用户经理人UID
     * mobile : 18573818003           电话号码
     * nickName : Wanan              昵称
     * numberFriends : null           好友人数
     * onlineTime : null
     * parentUid : null              经理人UID
     * password : x16YjoF1LNE=      用户密码
     * place : null                 常去地址
     * praiseSecond : null           好评次数
     * provinceActivity : null        活动范围省
     * qqOpenId : null
     * qrCode : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/qRimage/2019/04/26/402881f36a57b564016a57b57f940001_G1FKO7.jpg
     * 二维码图片路径
     * rechargeVip : null
     * referrerId : null            推荐人ID
     * registIp : null
     * registTime : 2019-04-26 11:04:33          注册时间
     * reportSecond : null           举报次数
     * role : null
     * secondReferrerId : null        二级推荐人ID
     * securityAnswer : null         密保答案--------------------------
     * securityQuestion : null      密保问题--------------------------
     * sex :                       性别
     * shieldlist : null           屏蔽列表---------------------------
     * signSecond : null            签到次数
     * storeId : null
     * temporaryBalance : null         临时账户余额
     * thirdReferrerId : null           三级推荐人ID
     * treadSecond : null
     * uId : 20005630       用户ID
     * userName : null       用户名
     * voiceSignature : null
     * weight : null           体重
     * withdrawPassword : null     提现密码
     * workField : null             工作领域
     * wxOpenId :                   微信ID
     */

    private String accessToken;
    private Object addfriendSecond;
    private Object address;
    private int age;
    private Object agentUid;
    private Object allProfit;
    private Object allWithDrawals;
    private Object amount;
    private Object auth;
    private Object balance;
    private Object beaddfriendSecond;
    private Object becomplainsSecond;
    private Object bedifferenceSecond;
    private Object bepraiseSecond;
    private Object bereportSecond;
    private String birthday;
    private Object birthdays;
    private Object blackList;
    private Object cashCoupon;
    private Object cityActivity;
    private Object commission;
    private Object company;
    private Object complainsSecond;
    private Object consumption;
    private Object contact;
    private Object dateCompany;
    private Object differenceSecond;
    private Object districtActivity;
    private Object exp;
    private Object fabulousSecond;
    private String face;
    private Object fansNum;
    private String gesturesPassword;
    private String gesturesStatus;
    private Object groupId;
    private String groupLevel;
    private Object height;
    private Object homeCity;
    private Object homeDistrict;
    private Object homeProvince;
    private Object industry;
    private String inviteCode;
    private Object isAgent;
    private String isCertification;
    private Object isDel;
    private Object isFace;
    private Object isHot;
    private Object isOnline;
    private String isPerfect;
    private Object isSynHuanxin;
    private String latitude;
    private String level;
    private String longitude;
    private Object managerUid;
    private String mobile;
    private String nickName;
    private Object numberFriends;
    private Object onlineTime;
    private Object parentUid;
    private String password;
    private Object place;
    private Object praiseSecond;
    private Object provinceActivity;
    private Object qqOpenId;
    private String qrCode;
    private Object rechargeVip;
    private Object referrerId;
    private Object registIp;
    private String registTime;
    private Object reportSecond;
    private Object role;
    private String rongyunToken;
    private Object secondReferrerId;
    private Object securityAnswer;
    private Object securityQuestion;
    private String sex;
    private Object shieldlist;
    private Object signSecond;
    private String storeId;
    private Object temporaryBalance;
    private Object thirdReferrerId;
    private Object treadSecond;
    private String uId;
    private Object userName;
    private String userRole;///////
    private Object voiceSignature;
    private Object weight;
    private Object withdrawPassword;
    private Object workField;
    private String wxOpenId;
    private String isOpenPartner;
    private String versionNumber;
    private String isFirstLanding;
    private String isFirstLandingManager;


    public String getIsFirstLandingManager() {
        return isFirstLandingManager;
    }

    public void setIsFirstLandingManager(String isFirstLandingManager) {
        this.isFirstLandingManager = isFirstLandingManager;
    }

    public String getIsFirstLanding() {
        return isFirstLanding;
    }

    public void setIsFirstLanding(String isFirstLanding) {
        this.isFirstLanding = isFirstLanding;
    }

    public String getIsOpenPartner() {
        return isOpenPartner;
    }

    public void setIsOpenPartner(String isOpenPartner) {
        this.isOpenPartner = isOpenPartner;
    }

    public String getIsCertification() {
        return isCertification;
    }

    public void setIsCertification(String isCertification) {
        this.isCertification = isCertification;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Object getAddfriendSecond() {
        return addfriendSecond;
    }

    public void setAddfriendSecond(Object addfriendSecond) {
        this.addfriendSecond = addfriendSecond;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Object getAgentUid() {
        return agentUid;
    }

    public void setAgentUid(Object agentUid) {
        this.agentUid = agentUid;
    }

    public Object getAllProfit() {
        return allProfit;
    }

    public void setAllProfit(Object allProfit) {
        this.allProfit = allProfit;
    }

    public Object getAllWithDrawals() {
        return allWithDrawals;
    }

    public void setAllWithDrawals(Object allWithDrawals) {
        this.allWithDrawals = allWithDrawals;
    }

    public Object getAmount() {
        return amount;
    }

    public void setAmount(Object amount) {
        this.amount = amount;
    }

    public Object getAuth() {
        return auth;
    }

    public void setAuth(Object auth) {
        this.auth = auth;
    }

    public Object getBalance() {
        return balance;
    }

    public void setBalance(Object balance) {
        this.balance = balance;
    }

    public Object getBeaddfriendSecond() {
        return beaddfriendSecond;
    }

    public void setBeaddfriendSecond(Object beaddfriendSecond) {
        this.beaddfriendSecond = beaddfriendSecond;
    }

    public Object getBecomplainsSecond() {
        return becomplainsSecond;
    }

    public void setBecomplainsSecond(Object becomplainsSecond) {
        this.becomplainsSecond = becomplainsSecond;
    }

    public Object getBedifferenceSecond() {
        return bedifferenceSecond;
    }

    public void setBedifferenceSecond(Object bedifferenceSecond) {
        this.bedifferenceSecond = bedifferenceSecond;
    }

    public Object getBepraiseSecond() {
        return bepraiseSecond;
    }

    public void setBepraiseSecond(Object bepraiseSecond) {
        this.bepraiseSecond = bepraiseSecond;
    }

    public Object getBereportSecond() {
        return bereportSecond;
    }

    public void setBereportSecond(Object bereportSecond) {
        this.bereportSecond = bereportSecond;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Object getBirthdays() {
        return birthdays;
    }

    public void setBirthdays(Object birthdays) {
        this.birthdays = birthdays;
    }

    public Object getBlackList() {
        return blackList;
    }

    public void setBlackList(Object blackList) {
        this.blackList = blackList;
    }

    public Object getCashCoupon() {
        return cashCoupon;
    }

    public void setCashCoupon(Object cashCoupon) {
        this.cashCoupon = cashCoupon;
    }

    public Object getCityActivity() {
        return cityActivity;
    }

    public void setCityActivity(Object cityActivity) {
        this.cityActivity = cityActivity;
    }

    public Object getCommission() {
        return commission;
    }

    public void setCommission(Object commission) {
        this.commission = commission;
    }

    public Object getCompany() {
        return company;
    }

    public void setCompany(Object company) {
        this.company = company;
    }

    public Object getComplainsSecond() {
        return complainsSecond;
    }

    public void setComplainsSecond(Object complainsSecond) {
        this.complainsSecond = complainsSecond;
    }

    public Object getConsumption() {
        return consumption;
    }

    public void setConsumption(Object consumption) {
        this.consumption = consumption;
    }

    public Object getContact() {
        return contact;
    }

    public void setContact(Object contact) {
        this.contact = contact;
    }

    public Object getDateCompany() {
        return dateCompany;
    }

    public void setDateCompany(Object dateCompany) {
        this.dateCompany = dateCompany;
    }

    public Object getDifferenceSecond() {
        return differenceSecond;
    }

    public void setDifferenceSecond(Object differenceSecond) {
        this.differenceSecond = differenceSecond;
    }

    public Object getDistrictActivity() {
        return districtActivity;
    }

    public void setDistrictActivity(Object districtActivity) {
        this.districtActivity = districtActivity;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Object getExp() {
        return exp;
    }

    public void setExp(Object exp) {
        this.exp = exp;
    }

    public Object getFabulousSecond() {
        return fabulousSecond;
    }

    public void setFabulousSecond(Object fabulousSecond) {
        this.fabulousSecond = fabulousSecond;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public Object getFansNum() {
        return fansNum;
    }

    public void setFansNum(Object fansNum) {
        this.fansNum = fansNum;
    }

    public String getGesturesPassword() {
        return gesturesPassword;
    }

    public void setGesturesPassword(String gesturesPassword) {
        this.gesturesPassword = gesturesPassword;
    }

    public String getGesturesStatus() {
        return gesturesStatus;
    }

    public void setGesturesStatus(String gesturesStatus) {
        this.gesturesStatus = gesturesStatus;
    }

    public Object getGroupId() {
        return groupId;
    }

    public void setGroupId(Object groupId) {
        this.groupId = groupId;
    }

    public String getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(String groupLevel) {
        this.groupLevel = groupLevel;
    }

    public Object getHeight() {
        return height;
    }

    public void setHeight(Object height) {
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

    public Object getIndustry() {
        return industry;
    }

    public void setIndustry(Object industry) {
        this.industry = industry;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Object getIsAgent() {
        return isAgent;
    }

    public void setIsAgent(Object isAgent) {
        this.isAgent = isAgent;
    }

    public Object getIsDel() {
        return isDel;
    }

    public void setIsDel(Object isDel) {
        this.isDel = isDel;
    }

    public Object getIsFace() {
        return isFace;
    }

    public void setIsFace(Object isFace) {
        this.isFace = isFace;
    }

    public Object getIsHot() {
        return isHot;
    }

    public void setIsHot(Object isHot) {
        this.isHot = isHot;
    }

    public Object getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Object isOnline) {
        this.isOnline = isOnline;
    }

    public String getIsPerfect() {
        return isPerfect;
    }

    public void setIsPerfect(String isPerfect) {
        this.isPerfect = isPerfect;
    }

    public Object getIsSynHuanxin() {
        return isSynHuanxin;
    }

    public void setIsSynHuanxin(Object isSynHuanxin) {
        this.isSynHuanxin = isSynHuanxin;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Object getManagerUid() {
        return managerUid;
    }

    public void setManagerUid(Object managerUid) {
        this.managerUid = managerUid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Object getNumberFriends() {
        return numberFriends;
    }

    public void setNumberFriends(Object numberFriends) {
        this.numberFriends = numberFriends;
    }

    public Object getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Object onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Object getParentUid() {
        return parentUid;
    }

    public void setParentUid(Object parentUid) {
        this.parentUid = parentUid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getPlace() {
        return place;
    }

    public void setPlace(Object place) {
        this.place = place;
    }

    public Object getPraiseSecond() {
        return praiseSecond;
    }

    public void setPraiseSecond(Object praiseSecond) {
        this.praiseSecond = praiseSecond;
    }

    public Object getProvinceActivity() {
        return provinceActivity;
    }

    public void setProvinceActivity(Object provinceActivity) {
        this.provinceActivity = provinceActivity;
    }

    public Object getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(Object qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Object getRechargeVip() {
        return rechargeVip;
    }

    public void setRechargeVip(Object rechargeVip) {
        this.rechargeVip = rechargeVip;
    }

    public Object getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(Object referrerId) {
        this.referrerId = referrerId;
    }

    public Object getRegistIp() {
        return registIp;
    }

    public void setRegistIp(Object registIp) {
        this.registIp = registIp;
    }

    public String getRegistTime() {
        return registTime;
    }

    public void setRegistTime(String registTime) {
        this.registTime = registTime;
    }

    public Object getReportSecond() {
        return reportSecond;
    }

    public void setReportSecond(Object reportSecond) {
        this.reportSecond = reportSecond;
    }

    public Object getRole() {
        return role;
    }

    public void setRole(Object role) {
        this.role = role;
    }

    public String getRongyunToken() {
        return rongyunToken;
    }

    public void setRongyunToken(String rongyunToken) {
        this.rongyunToken = rongyunToken;
    }

    public Object getSecondReferrerId() {
        return secondReferrerId;
    }

    public void setSecondReferrerId(Object secondReferrerId) {
        this.secondReferrerId = secondReferrerId;
    }

    public Object getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(Object securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public Object getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(Object securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Object getShieldlist() {
        return shieldlist;
    }

    public void setShieldlist(Object shieldlist) {
        this.shieldlist = shieldlist;
    }

    public Object getSignSecond() {
        return signSecond;
    }

    public void setSignSecond(Object signSecond) {
        this.signSecond = signSecond;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Object getTemporaryBalance() {
        return temporaryBalance;
    }

    public void setTemporaryBalance(Object temporaryBalance) {
        this.temporaryBalance = temporaryBalance;
    }

    public Object getThirdReferrerId() {
        return thirdReferrerId;
    }

    public void setThirdReferrerId(Object thirdReferrerId) {
        this.thirdReferrerId = thirdReferrerId;
    }

    public Object getTreadSecond() {
        return treadSecond;
    }

    public void setTreadSecond(Object treadSecond) {
        this.treadSecond = treadSecond;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public Object getUserName() {
        return userName;
    }

    public void setUserName(Object userName) {
        this.userName = userName;
    }

    public Object getVoiceSignature() {
        return voiceSignature;
    }

    public void setVoiceSignature(Object voiceSignature) {
        this.voiceSignature = voiceSignature;
    }

    public Object getWeight() {
        return weight;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }

    public Object getWithdrawPassword() {
        return withdrawPassword;
    }

    public void setWithdrawPassword(Object withdrawPassword) {
        this.withdrawPassword = withdrawPassword;
    }

    public Object getWorkField() {
        return workField;
    }

    public void setWorkField(Object workField) {
        this.workField = workField;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}

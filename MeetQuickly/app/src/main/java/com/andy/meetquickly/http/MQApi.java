package com.andy.meetquickly.http;

import android.content.Context;

import com.andy.meetquickly.common.Constant;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxSPTool;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/25 10:22
 * 描述：
 */
public class MQApi {

    public static void initMQApi(Context context){
    }

    public static class RequestParams extends HashMap<String, String> {
        public void put(String key, int val) {
            put(key, String.valueOf(val));
        }
    }

    //发送验证码
    public static void sendCaptchas(String phone, String type, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("phone",phone);
        params.put("type",type);
        new HttpRequest("login/sendCaptchas.do",params,simpleCallBack);
    }

    //验证验证码
    public static void valiadCaptchas(String phone,String captcha,String wxOpenId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("phone",phone);
        params.put("captcha",captcha);
        params.put("wxOpenId",wxOpenId);
        new HttpRequest("login/valiadCaptchas.do",params,simpleCallBack);
    }

    //修改用户密码接口
    public static void updatePassword(String uId,String password,String newPassword,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("password",password);
        params.put("newPassword",newPassword);
        new HttpRequest("login/updatePassword.do",params,simpleCallBack);
    }

    //用户微信登陆接口
    public static void appUserLoginByWx(String wxOpenId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("wxOpenId",wxOpenId);
        new HttpRequest("login/appUserLoginByWx.do",params,simpleCallBack);
    }

    //注册
    public static void register(String phone, String password, String nickName,
                                           String sex, String birthday, File file, String fileKey,
                                           ProgressResponseCallBack progressResponseCallBack, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("phone",phone);
        params.put("password",password);
        params.put("nickName",nickName);
        params.put("sex",sex);
        params.put("birthday",birthday);
        new HttpRequest("login/register.do",params,file,fileKey,progressResponseCallBack,simpleCallBack);
    }

    //用户登录
    public static void appUserLogin(Context context,String phone, String password, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("phone",phone);
        params.put("password",password);
        params.put("platformType",2);
        params.put("osVersion",android.os.Build.VERSION.SDK_INT+"");//系统版本
        params.put("longitude",RxSPTool.getString(context,Constant.KEY_LONGITUDE));//系统版本
        params.put("latitude",RxSPTool.getString(context,Constant.KEY_LATITUDE));//系统版本
        params.put("appVersion",RxDeviceTool.getAppVersionName(context) + "");//APP版本
        params.put("deviceBrand",RxDeviceTool.getBuildBrandModel() + "");//设备型号
        params.put("deviceNo",RxDeviceTool.getDeviceIdIMEI(context) + "");//设备号设备唯一标示
        params.put("pushNo",JPushInterface.getRegistrationID(context));//极光推送号
        new HttpRequest("login/appUserLogin.do",params,simpleCallBack);
    }

    //忘记密码
    public static void forgetPassword(String phone, String password, String captcha,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("phone",phone);
        params.put("password",password);
        params.put("captcha",captcha);
        new HttpRequest("login/forgetPassword.do",params,simpleCallBack);
    }



    //去哪玩列表
    public static void getShopsList(String uId,String storeId,int currentPage,int kind,int discountLeft,int discountRight,int activityName,String longitude,String latitude, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("storeId",storeId);
        params.put("currentPage",currentPage);
        params.put("kind",kind);
        params.put("discountLeft",discountLeft);
        params.put("discountRight",discountRight);
        params.put("activityName",activityName);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        new HttpRequest("shop/getShopsList.do",params,simpleCallBack);
    }

    //找他玩列表
    public static void getUsersList(int currentPage,String uId,String sex,String longitude,String latitude, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("sex",sex);
        params.put("currentPage",currentPage);
//        params.put("screenSex",screenSex);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        new HttpRequest("user/getUsersList.do",params,simpleCallBack);
    }


    //附近动态列表
    public static void getUserNearDynamicList(int currentPage,String uId,String sex,String longitude,String latitude, int screenSex,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("sex",sex);
        params.put("currentPage",currentPage);
        params.put("screenSex",screenSex);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        if (screenSex != 0) {
            params.put("screenSex", screenSex);
        }
        new HttpRequest("dynamic/getUserNearDynamicList.do",params,simpleCallBack);
    }

    //动态点赞
    public static void addUserThumbsUp(String uId,String dynamicId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("dynamicId",dynamicId);
        new HttpRequest("dynamic/addUserThumbsUp.do",params,simpleCallBack);
    }

    //获取网店资料详情接口
    public static void getShopDataDetailsByStoreId(String uId,String storeId,String longitude,String latitude,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("storeId",storeId);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        new HttpRequest("shop/getShopDataDetailsByStoreId.do",params,simpleCallBack);
    }

    //获取网店图片列表接口
    public static void getShopImgListByStoreId(String storeId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("storeId",storeId);
        new HttpRequest("shop/getShopImgListByStoreId.do",params,simpleCallBack);
    }

    //获取网店活动详情接口
    public static void getShopDiscountDetailsByStoreId(String storeId,int currentPage,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("storeId",storeId);
        params.put("currentPage",currentPage);
        new HttpRequest("shop/getShopDiscountDetailsByStoreId.do",params,simpleCallBack);
    }

    //获取网店可能邂逅人接口
    public static void getShopEncounterListByStoreId(String storeId,int currentPage,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("storeId",storeId);
        params.put("currentPage",currentPage);
        new HttpRequest("shop/getShopEncounterListByStoreId.do",params,simpleCallBack);
    }

    //获取网店可能邂逅人接口
    public static void getShopConsumptionReferenceByStoreId(String storeId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("storeId",storeId);
        new HttpRequest("shop/getShopConsumptionReferenceByStoreId.do",params,simpleCallBack);
    }

    //获取用户基本资料接口
    public static void getUserBaseInfo(String uId,String currentUid,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentUid",currentUid);
        new HttpRequest("user/getUserBaseInfo.do",params,simpleCallBack);
    }

    //获取用户相关资料接口
    public static void getUserDataInfo(String uId,String visitUid,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("visitUid",visitUid);
        new HttpRequest("user/getUserDataInfo.do",params,simpleCallBack);
    }

    //获取用户图片或视频集接口
    public static void getUserImageOrVideoList(String uId,int type,int currentPage,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("type",type);
        params.put("currentPage",currentPage);
        new HttpRequest("user/getUserImageOrVideoList.do",params,simpleCallBack);
    }

    //获取用户动态列表接口
    public static void getUserDynamicList(String uId,int currentPage,String currentUid,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentPage",currentPage);
        params.put("currentUid",currentUid);
        new HttpRequest("user/getUserDynamicList.do",params,simpleCallBack);
    }

    //修改个人头像接口
    public static void updateUserFace(String uId, File file, String fileKey,
                                ProgressResponseCallBack progressResponseCallBack, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/updateUserFace.do",params,file,fileKey,progressResponseCallBack,simpleCallBack);
    }

    //上传用户图片或视频集接口
    public static void updateUserImageOrVideoList(String uId, int type,List<File> file, String fileKey,
                                      ProgressResponseCallBack progressResponseCallBack, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("type",type);
        new HttpRequest("user/updateUserImageOrVideoList.do",params,file,fileKey,progressResponseCallBack,simpleCallBack);
    }

    //上传用户图片或视频集接口
    public static void updateUserImageOrVideoList(String uId, int type,File file, String fileKey,
                                                  ProgressResponseCallBack progressResponseCallBack, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("type",type);
        new HttpRequest("user/updateUserImageOrVideoList.do",params,file,fileKey,progressResponseCallBack,simpleCallBack);
    }

    //获取标签兴趣等信息接口 类型 1行业  2工作领域 3运动 4音乐 5食物 6电影 7书和动漫 8旅游足迹 9我的标签
    public static void getTagsList(String uId,int type, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("type",type);
        new HttpRequest("user/getTagsList.do",params,simpleCallBack);
    }

    //修改用户相关资料接口
//    uId	是	用户ID
//    nickName	否	用户昵称
//    birthday	否	用户生日
//    height	否	身高
//    weight	否	体重
//    company	否	公司信息
//    place	否	常去地点
//    homeProvince	否	家乡（省）
//    homeCity	否	家乡(市)
//    homeDistrict	否	家乡(区)
//    stores	否	String[] 常去场所
//    tags	否	String[] 个人标签
//    sports	否	String[] 运动
//    musics	否	String[] 音乐
//    foods	否	String[] 食物
//    films	否	String[] 电影
//    books	否	String[] 书籍
//    vacation	否	String[] 旅行足迹
//    industry	否	String[] 个人行业
//    works	否	String[] 工作领域
    public static void updateUserInfo(String uId,String nickName,String birthday,String height,
                                      String weight,String company,
                                      String homeProvince,String homeCity,String homeDistrict,
                                      String tags,String sports,
                                      String musics,String foods,String films,
                                      String books,String vacation,String industry,
                                      String works,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("nickName",nickName);
        params.put("birthday",birthday);
        params.put("height",height);
        params.put("weight",weight);
        params.put("company",company);
        params.put("homeProvince",homeProvince);
        params.put("homeCity",homeCity);
        params.put("homeDistrict",homeDistrict);
        params.put("tags",tags);
        params.put("sports",sports);
        params.put("musics",musics);
        params.put("foods",foods);
        params.put("films",films);
        params.put("books",books);
        params.put("vacation",vacation);
        params.put("industry",industry);
        params.put("works",works);
        new HttpRequest("user/updateUserInfo.do",params,simpleCallBack);
    }

    //新增绑定关系接口
//    uId	是	用户ID
//    type	是	type 1.网店绑妹纸，2.妹纸绑网店
//    phone	是	电话或者YP号
    public static void addBinding(String uId,int type,String phone, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("type",type);
        params.put("phone",phone);
        new HttpRequest("user/addBinding.do",params,simpleCallBack);
    }

    //用户发布动态接口
    public static void addUserDynamicPic(String uId, int type,String content,String longitude,String latitude,
                                      String location,List<File> file, String fileKey,
                                      ProgressResponseCallBack progressResponseCallBack, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("type",type);
        params.put("content",content);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        params.put("location",location);
        new HttpRequest("dynamic/addUserDynamic.do",params,file,fileKey,progressResponseCallBack,simpleCallBack);
    }

    //用户发布动态接口
    public static void addUserDynamicVideo(String uId, int type,String content,String longitude,String latitude,
                                         String location,File file, String fileKey,
                                         ProgressResponseCallBack progressResponseCallBack, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("type",type);
        params.put("content",content);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        params.put("location",location);
        new HttpRequest("dynamic/addUserDynamic.do",params,file,fileKey,progressResponseCallBack,simpleCallBack);
    }

    //用户评论动态接口
    public static void addUserComment(String uId,String dynamicId,String content,String toUid,
                                      SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("dynamicId",dynamicId);
        params.put("content",content);
        params.put("toUid",toUid);
        new HttpRequest("dynamic/addUserComment.do",params,simpleCallBack);
    }

    //获取用户动态评论列表接口
    public static void getUserCommentsList(String dynamicId,int currentPage,String uId,
                                      SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("dynamicId",dynamicId);
        params.put("currentPage",currentPage);
        params.put("uId",uId);
        new HttpRequest("dynamic/getUserCommentsList.do",params,simpleCallBack);
    }

    //获取平台所有公司信息接口
    public static void getAllCompanyInfo(String name,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("name",name);
        new HttpRequest("shop/getAllCompanyInfo.do",params,simpleCallBack);
    }

    //同步用户通讯录接口
    public static void insertPhoneBook(String uId,String phoneBooks,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("phoneBooks",phoneBooks);
        new HttpRequest("user/insertPhoneBook.do",params,simpleCallBack);
    }

    //上传用户屏蔽列表接口
    public static void addUsersShieldList(String uId,String phoneBooks,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("phoneBooks",phoneBooks);
        new HttpRequest("user/addUsersShieldList.do",params,simpleCallBack);
    }

    //获取用户屏蔽列表接口
    public static void getUsersShieldList(String uId,int currentPage,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentPage",currentPage);
        new HttpRequest("user/getUsersShieldList.do",params,simpleCallBack);
    }

    //根据屏蔽ID删除用户屏蔽人接口
    public static void delUsersShieldListById(String id,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("user/delUsersShieldListById.do",params,simpleCallBack);
    }

    //申请添加好友接口
    public static void applyBecomeFriend(String uId,String friendUid,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("friendUid",friendUid);
        new HttpRequest("user/applyBecomeFriend.do",params,simpleCallBack);
    }

    //获取用户好友列表接口
    public static void getUserFriendList(String uId,String name,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("name",name);
        new HttpRequest("user/getUserFriendList.do",params,simpleCallBack);
    }

    //删除好友接口
    public static void delUserFriend(String id,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("user/delUserFriend.do",params,simpleCallBack);
    }

    //获取新好友申请列表接口
    public static void getNewUserFriendApplyList(String uId,int currentPage,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentPage",currentPage);
        new HttpRequest("user/getNewUserFriendApplyList.do",params,simpleCallBack);
    }

    //同意好友申请接口
    public static void agreeUserFriendApply(String id,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("user/agreeUserFriendApply.do",params,simpleCallBack);
    }

    //关注用户接口
    public static void followUser(String uId,String friendUid,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("friendUid",friendUid);
        new HttpRequest("user/followUser.do",params,simpleCallBack);
    }

    //获取用户关注列表接口
    public static void getUserFollowList(String uId,String name,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("name",name);
        new HttpRequest("user/getUserFollowList.do",params,simpleCallBack);
    }

    //取消关注接口
    public static void cancelFollow(String id,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("user/cancelFollow.do",params,simpleCallBack);
    }

    //获取我的粉丝列表接口
    public static void getUserFansList(String uId,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getUserFansList.do",params,simpleCallBack);
    }

    //获取个人主页访问列表接口
    public static void getUserVisitLists(String uId,int currentPage,String currentUid,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentPage",currentPage);
        params.put("currentUid",currentUid);
        new HttpRequest("user/getUserVisitLists.do",params,simpleCallBack);
    }

    //获取网店访问列表信息接口
    public static void getShopVisitLists(String uId,int currentPage,String storeId,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentPage",currentPage);
        params.put("storeId",storeId);
        new HttpRequest("shop/getShopVisitLists.do",params,simpleCallBack);
    }

    //获取举报内容列表接口
    public static void getReportContentList(String type,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("type",type);
        new HttpRequest("common/getReportContentList.do",params,simpleCallBack);
    }

    //用户举报网店接口
    public static void addReportShop(String uId,String storeId,String reportContent,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("storeId",storeId);
        params.put("reportContent",reportContent);
        new HttpRequest("shop/addReportShop.do",params,simpleCallBack);
    }

    //用户举报动态接口
    public static void addReportDynamic(String uId,String dynamicId,String reportContent,boolean isVideo,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("dynamicId",dynamicId);
        params.put("reportContent",reportContent);
        if (isVideo){
            params.put("type",3);

        }
        new HttpRequest("user/addReportDynamic.do",params,simpleCallBack);
    }
    //用户举报个人主页接口

    public static void addReportUserHomePage(String uId,String reportId,String reportContent,
                                           SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("reportId",reportId);
        params.put("reportContent",reportContent);
        new HttpRequest("user/addReportUserHomePage.do",params,simpleCallBack);
    }

    //用户屏蔽动态接口
    public static void addDynamicShield(String uId,String dynamicId,
                                        SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("shielUid",dynamicId);
        new HttpRequest("user/shieldTaComments.do",params,simpleCallBack);
    }

    //播放个人视频记录接口
    public static void addUserVideoPlayNo(String id,
                                        SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("user/addUserVideoPlayNo.do",params,simpleCallBack);
    }

    //点赞个人视频接口
    public static void thumbsUpUserVideo(String id,String uId,
                                        SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        params.put("uId",uId);
        new HttpRequest("user/thumbsUpUserVideo.do",params,simpleCallBack);
    }

    //删除个人图片或视频接口
    public static void delUserImageOrVideoList(String id, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("user/delUserImageOrVideoList.do",params,simpleCallBack);
    }

    //获取礼物列表接口
    public static void getGiftList(SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        new HttpRequest("user/getGiftList.do",params,simpleCallBack);
    }

    //获取支付宝加签后台的订单信息字符串
    public static void getAliPayOrderStr(String uId, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("pay/getAliPayOrderStr.do",params,simpleCallBack);
    }

    //获取支付宝加签后台的订单信息字符串
    public static void getWXPayOrderStr(String uId, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("pay/getWXPayOrderStr.do",params,simpleCallBack);
    }

    //赠送礼物
    public static void giveGiftToUser(String uId,String toUid,String giftId,String giftNum, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("toUid",toUid);
        params.put("giftId",giftId);
        params.put("giftNum",giftNum);
        params.put("mobileSystem",2);
        new HttpRequest("user/giveGiftToUser.do",params,simpleCallBack);
    }

    //获取钱包数据
    public static void getWalletDate(String uId, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getWalletDate.do",params,simpleCallBack);
    }

    //获取快币明细
    public static void getKuaiBiDetail(String uId, int currentPage,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentPage",currentPage);
        new HttpRequest("user/getKuaiBiDetail.do",params,simpleCallBack);
    }

    //获取总收益列表
    public static void getTotalIncomeDetailList(String uId, int currentPage,int type,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentPage",currentPage);
        params.put("type",type); //1.总收益 2.昨日收益
        new HttpRequest("user/getTotalIncomeDetailList.do",params,simpleCallBack);
    }

    //获取总收益列表
    public static void addUserTags(String uId, String content,int type,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("content",content);
        params.put("type",type); //1行业  2工作领域 3运动 4音乐 5食物 6电影 7书和动漫 8旅游足迹 9我的标签
        new HttpRequest("user/addUserTags.do",params,simpleCallBack);
    }

    //获取礼物收益列表
    public static void getGiftIncomeDetailList(String uId, int currentPage,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentPage",currentPage);
        new HttpRequest("user/getGiftIncomeDetailList.do",params,simpleCallBack);
    }

    //获取订单返代金券列表
    public static void getOrderCouponDetailList(String uId, int currentPage,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentPage",currentPage);
        new HttpRequest("user/getOrderCouponDetailList.do",params,simpleCallBack);
    }

    //获取提现信息数据
    public static void getUserWithdrawalsInfo(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getUserWithdrawalsInfo.do",params,simpleCallBack);
    }

    //用户申请提现
    public static void addUserWithdrawalsInfo(String uId,String withdrawalsAmount,String accountNumber,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("withdrawalsAmount",withdrawalsAmount);
        params.put("type",1);
        params.put("accountNumber",accountNumber);
        new HttpRequest("user/addUserWithdrawalsInfo.do",params,simpleCallBack);
    }

    //判断是否为好友
    public static void isUserFriend(String uId,String friendUid,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("friendUid",friendUid);
        new HttpRequest("user/isUserFriend.do",params,simpleCallBack);
    }

    //获取用户每月账单汇总
    public static void getUserBillCount(String uId,String currentTime,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentTime",currentTime);
        new HttpRequest("user/getUserBillCount.do",params,simpleCallBack);
    }

    //获取用户结算列表接口
    public static void getUserCashWithdrawalDetail(String uId,int currentPage,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentPage",currentPage);
        new HttpRequest("user/getUserCashWithdrawalDetail.do",params,simpleCallBack);
    }

    //获取快币余额接口
    public static void getKuaiBiBalance(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getKuaiBiBalance.do",params,simpleCallBack);
    }

    //充值快币接口
    public static void rechargeKuaiBi(String uId,String type,String amount,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("type",type);
        params.put("amount",amount);
        new HttpRequest("pay/rechargeKuaiBi.do",params,simpleCallBack);
    }

    //获取合伙人个人以及直属团队推广完成情况接口
    public static void getUserTeamGoalsInfo(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getUserTeamGoalsInfo.do",params,simpleCallBack);
    }

    //获取合伙人信息列表接口
    public static void getUserPartnerList(String uId,int currentPage,int type,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentPage",currentPage);
        params.put("type",type);
        new HttpRequest("user/getUserPartnerList.do",params,simpleCallBack);
    }

    //经理人获取我的用户列表接口
    public static void getMyUsersList(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("shop/getMyUsersList.do",params,simpleCallBack);
    }

    //经理人解除绑定接口
    public static void updateMyUsers(String id,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("shop/updateMyUsers.do",params,simpleCallBack);
    }

    //获取网店详细信息接口
    public static void getShopDetailInfo(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("shop/getShopDetailInfo.do",params,simpleCallBack);
    }

    //更新网店信息接口
    public static void updateShopInfo(String uId,String storeId,String discount,
                                      String isOnline,String businessTime,String feature,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("storeId",storeId);
        params.put("discount",discount);
        params.put("isOnline",isOnline);
        params.put("businessTime",businessTime);
        params.put("feature",feature);
        new HttpRequest("shop/updateShopInfo.do",params,simpleCallBack);
    }

    //新增网店现有活动接口
    public static void addShopActivityInfo(String storeId,String name,
                                      String startTime,String endTime,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("storeId",storeId);
        params.put("name",name);
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        new HttpRequest("shop/addShopActivityInfo.do",params,simpleCallBack);
    }

    //更新网店现有活动接口
    public static void updateShopActivityInfo(String id,String name,
                                      String startTime,String endTime,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        params.put("name",name);
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        new HttpRequest("shop/updateShopActivityInfo.do",params,simpleCallBack);
    }

    //删除网店现有活动接口
    public static void delShopActivityInfo(String id,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("shop/delShopActivityInfo.do",params,simpleCallBack);
    }

    //新增网店台位类型接口
    public static void addShopTableInfo(String storeId,String name,String minPeople,
                                        String maxPeople,String boxFee,String minCharge,
                                        String remark,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("storeId",storeId);
        params.put("name",name);
        params.put("minPeople",minPeople);
        params.put("maxPeople",maxPeople);
        params.put("boxFee",boxFee);
        params.put("minCharge",minCharge);
        params.put("remark",remark);
        new HttpRequest("shop/addShopTableInfo.do",params,simpleCallBack);
    }

    //更新网店台位类型接口
    public static void updateShopTableInfo(String id,String name,String minPeople,
                                        String maxPeople,String boxFee,String minCharge,String remark,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        params.put("name",name);
        params.put("minPeople",minPeople);
        params.put("maxPeople",maxPeople);
        params.put("boxFee",boxFee);
        params.put("minCharge",minCharge);
        params.put("remark",remark);
        new HttpRequest("shop/updateShopTableInfo.do",params,simpleCallBack);
    }

    //删除网店台位类型接口
    public static void delShopTableInfo(String id,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("shop/delShopTableInfo.do",params,simpleCallBack);
    }

    //获取图片模板接口
    public static void getStoreTemplateByType(int type,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("type",type);
        new HttpRequest("shop/getStoreTemplateByType.do",params,simpleCallBack);
    }

    //新增消费参考接口
    public static void addStoreConsumption(String storeId,String name,int type,
                                           String unit,String price,String cost,String imgUrl,String remark,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("storeId",storeId);
        params.put("name",name);
        params.put("type",type);
        params.put("unit",unit);
        params.put("price",price);
        params.put("cost",cost);
        params.put("imgUrl",imgUrl);
        params.put("remark",remark);
        new HttpRequest("shop/addStoreConsumption.do",params,simpleCallBack);
    }

    //更新消费参考接口
    public static void updateStoreConsumption(String id,String name,int type,
                                           String unit,String price,String cost,String imgUrl,String remark,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        params.put("name",name);
        params.put("type",type);
        params.put("unit",unit);
        params.put("price",price);
        params.put("cost",cost);
        params.put("imgUrl",imgUrl);
        params.put("remark",remark);
        new HttpRequest("shop/updateStoreConsumption.do",params,simpleCallBack);
    }

    //删除消费参考接口
    public static void delStoreConsumption(String id,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("shop/delStoreConsumption.do",params,simpleCallBack);
    }

    //新增网店活动(动态)接口

    public static void addStoreActivity(String storeId,String type,String content,String expireTime,String orderNum,List<File> files, String fileKey,
                                        ProgressResponseCallBack progressResponseCallBack, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("storeId",storeId);
        params.put("type",type);
        params.put("content",content);
        params.put("expireTime",expireTime);
        params.put("orderNum",orderNum);
        new HttpRequest("shop/addStoreActivity.do",params,files,fileKey,progressResponseCallBack,simpleCallBack);
    }

    //动态列表(活动)接口
    public static void getStoreActivtyList(int currentPage,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("currentPage",currentPage);
        new HttpRequest("dynamic/getStoreActivtyList.do",params,simpleCallBack);
    }

    //获取关注动态列表接口
    public static void getUserFollowDynamicList(String uId,int currentPage,String longitude,String latitude,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentPage",currentPage);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        new HttpRequest("dynamic/getUserFollowDynamicList.do",params,simpleCallBack);
    }

    //网店活动历史记录接口
    public static void getStoreActivityList(String storeId,int currentPage,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("storeId",storeId);
        params.put("currentPage",currentPage);
        new HttpRequest("shop/getStoreActivityList.do",params,simpleCallBack);
    }

    //删除网店活动接口
    public static void delStoreActivityList(String id,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("shop/delStoreActivityList.do",params,simpleCallBack);
    }

    //根据uid获取用户登陆信息接口
    public static void getUserInfoByUid(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getUserInfoByUid.do",params,simpleCallBack);
    }

    //获取用户订单详情接口
    public static void getUserOrderInfo(String uId,int userRole,int type,int currentPage,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("userRole",userRole);  //用户角色 1.普通用户 2.经理人
        params.put("type",type);      //订单状态 2.进行中 3.已完成
        params.put("currentPage",currentPage);
        new HttpRequest("order/getUserOrderInfo.do",params,simpleCallBack);
    }

    //用户取消订单接口
    public static void userCancelOrder(String id,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("order/userCancelOrder.do",params,simpleCallBack);
    }

    //用户确认到店接口
    public static void userReachStore(String id,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("order/userReachStore.do",params,simpleCallBack);
    }

    //用户评价网店接口
    public static void userEvaluateShop(String uId,String storeId,String orderId,String serviceScore,
                                        String placeScore,String satisfiedScore,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("storeId",storeId);
        params.put("orderId",orderId);
        params.put("serviceScore",serviceScore);
        params.put("placeScore",placeScore);
        params.put("satisfiedScore",satisfiedScore);
        new HttpRequest("order/userEvaluateShop.do",params,simpleCallBack);
    }

    //用户删除已完成的订单接口
    public static void delOrder(String id,String userRole,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        params.put("userRole",userRole);
        new HttpRequest("order/delOrder.do",params,simpleCallBack);
    }

    //经理人上传消费凭证接口
    public static void uploadConsumptionBills(String id,List<File> filelist,String key,ProgressResponseCallBack progressResponseCallBack,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("order/uploadConsumptionBills.do",params,filelist,key,progressResponseCallBack,simpleCallBack);
    }

    //用户确认订单消费凭证接口
    public static void userConfirmOrder(String id,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("order/userConfirmOrder.do",params,simpleCallBack);
    }

    //用户订单复议接口
    public static void userReconsiderationOrder(String id,String content,File files,String key,ProgressResponseCallBack progressResponseCallBack,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        params.put("content",content);
        new HttpRequest("order/userReconsiderationOrder.do",params,files,key,progressResponseCallBack,simpleCallBack);
    }

    //用户订单投诉接口
    public static void userComplainOrder(String uId,String storeId,String orderId,String content,
                                         String otherContent,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("storeId",storeId);
        params.put("orderId",orderId);
        params.put("content",content);
        params.put("otherContent",otherContent);
        new HttpRequest("order/userComplainOrder.do",params,simpleCallBack);
    }

    //获取主页banner接口
    public static void getHomeBanner(SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        new HttpRequest("common/getHomeBanner.do",params,simpleCallBack);
    }

    //获取今日推荐网店接口
    public static void getRecommendList(SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        new HttpRequest("user/getRecommendList.do",params,simpleCallBack);
    }

    //获取首页合伙人数据接口
    public static void getHomePartnerInfo(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getHomePartnerInfo.do",params,simpleCallBack);
    }

    //获取经理人上传消费凭证接口
    public static void getConsumptionBills(String orderId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("orderId",orderId);
        new HttpRequest("order/getConsumptionBills.do",params,simpleCallBack);
    }

    //获取经理人未支付订单列表接口
    public static void getManagerUnpaid(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("order/getManagerUnpaid.do",params,simpleCallBack);
    }

    //经理人支付订单接口
    public static void managerPayOrders(String uId,int type,String amount,String orderId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("type",type);
        params.put("amount",amount);
        params.put("orderId",orderId);
        new HttpRequest("pay/managerPayOrders.do",params,simpleCallBack);
    }

    //用户删除动态接口
    public static void delUserDynamic(String id,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("dynamic/delUserDynamic.do",params,simpleCallBack);
    }

    //设置手势密码接口
    public static void setGesturePassword(String uId,String gesturePassword,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("gesturePassword",gesturePassword);
        new HttpRequest("user/setGesturePassword.do",params,simpleCallBack);
    }

    //打开关闭手势密码接口
    public static void openOrCloseGesturePassword(String uId,int type,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("type",type);
        new HttpRequest("user/openOrCloseGesturePassword.do",params,simpleCallBack);
    }

    //校验手势密码接口
    public static void checkGesturePassword(String uId,String gesturePassword,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("gesturePassword",gesturePassword);
        new HttpRequest("user/checkGesturePassword.do",params,simpleCallBack);
    }

    //获取首页相关信息数据接口
    public static void getHomePageListInfo(String uId,String longitude,String latitude,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        new HttpRequest("user/getHomePageListInfo.do",params,simpleCallBack);
    }


    //获取互动列表总数接口
    public static void getUnreadMessageTotal(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("dynamic/getUnreadMessageTotal.do",params,simpleCallBack);
    }

    //获取互动列表接口
    public static void getUnreadMessageList(String uId,int currentPage,String longitude,String latitude,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentPage",currentPage);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        new HttpRequest("dynamic/getUnreadMessageList.do",params,simpleCallBack);
    }

    //根据动态ID获取动态详情接口
    public static void getDynamicDetailInfoById(String uId,String id,String longitude,String latitude,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("id",id);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        new HttpRequest("dynamic/getDynamicDetailInfoById.do",params,simpleCallBack);
    }

    //获取每日项目信息接口
    public static void getDailyTasksInfo(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("common/getDailyTasksInfo.do",params,simpleCallBack);
    }

    //上传图片接口
    public static void uploadShopImgs(List<File> files,ProgressResponseCallBack progressResponseCallBack,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        new HttpRequest("shop/uploadShopImgs.do",params,files,"files",progressResponseCallBack,simpleCallBack);
    }

    //获取网店申请数据接口
    public static void getUserShopInfo(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("shop/getUserShopInfo.do",params,simpleCallBack);
    }

    //申请开店接口
    public static void userApplyForShop(String id,String uId,String phone,String name,
                                        String companyId,String kind,String remark,
                                        String coverImg,String imgs,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        params.put("uId",uId);
        params.put("phone",phone);
        params.put("name",name);
        params.put("companyId",companyId);
        params.put("kind",kind);
        params.put("remark",remark);
        params.put("coverImg",coverImg);
        params.put("imgs",imgs);
        new HttpRequest("shop/userApplyForShop.do",params,simpleCallBack);
    }

    //用户领取网店抵用券
    public static void userReceiveShopCoupon(String uId,String storeId,String storeCouponId,String couponAmount,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("storeId",storeId);
        params.put("storeCouponId",storeCouponId);
        params.put("couponAmount",couponAmount);
        new HttpRequest("shop/userReceiveShopCoupon.do",params,simpleCallBack);
    }

    //获取用户代金券数据接口
    public static void getUserCouponInfoList(String uId,int type,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("type",type);
        new HttpRequest("user/getUserCouponInfoList.do",params,simpleCallBack);
    }

    //判断用户是否有代金券接口
    public static void getUserCashCoupon(String uId,String kind,String storeId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("kind",kind);
        params.put("storeId",storeId);
        new HttpRequest("user/getUserCashCoupon.do",params,simpleCallBack);
    }

    //新增用户下单记录接口
    public static void addUserOrder(String uId,int type,String storeId,int useCoupon,String coupon,
                                    String userCouponId,String name,String mobile,String kind,
                                    String targetDate,
                                    String reachTime,String peoples,String remark,String missLovers,
                                    String activityId,String sex,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("type",type);
        params.put("storeId",storeId);
        params.put("useCoupon",useCoupon);
        params.put("coupon",coupon);
        params.put("userCouponId",userCouponId);
        params.put("name",name);
        params.put("mobile",mobile);
        params.put("sex",sex);
        params.put("kind",kind);
        params.put("targetDate",targetDate);
        params.put("reachTime",reachTime);
        params.put("peoples",peoples);
        params.put("remark",remark);
        params.put("missLovers",missLovers);
        params.put("activityId",activityId);
        new HttpRequest("user/addUserOrder.do",params,simpleCallBack);
    }


    //获取系统配置(客服电话)接口
    public static void getSysConfigInfo(SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("pKey","CONSUMER_HOTLINE");
        new HttpRequest("common/getSysConfigInfo.do",params,simpleCallBack);
    }

    //获取用户好友粉丝关注信息接口
    public static void getUserRelationInfo(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getUserRelationInfo.do",params,simpleCallBack);
    }

    //主页搜索接口
    public static void searchHomePage(String uId,int type,String name,
                                      String longitude,String latitude,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("type",type);
        params.put("name",name);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        new HttpRequest("user/searchHomePage.do",params,simpleCallBack);
    }

    //获取用户站内消息接口
    public static void getUserMessage(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getUserMessage.do",params,simpleCallBack);
    }

    //获取视频评论列表接口
    public static void getVideoCommentsList(String uId,String imgsId,int currentPage,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("imgsId",imgsId);
        params.put("currentPage",currentPage);
        new HttpRequest("user/getVideoCommentsList.do",params,simpleCallBack);
    }

    //评论用户视频接口
    public static void commentUserVideo(String uId,String imgsId,String content,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("imgsId",imgsId);
        params.put("content",content);
        new HttpRequest("user/commentUserVideo.do",params,simpleCallBack);
    }

    //屏蔽TA的动态接口
    public static void shieldTaComments(String uId,String shielUid,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("shielUid",shielUid);
        new HttpRequest("user/shieldTaComments.do",params,simpleCallBack);
    }

    //获取用户屏蔽列表接口
    public static void getUserShielList(String uId,String name,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("name",name);
        new HttpRequest("user/getUserShielList.do",params,simpleCallBack);
    }

    //取消用户屏蔽接口
    public static void cancelUserShielRecord(String id,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("user/cancelUserShielRecord.do",params,simpleCallBack);
    }

    //取消用户屏蔽接口
    public static void addUserCertification(String uId,String name,String idCardNo,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("name",name);
        params.put("idCardNo",idCardNo);
        new HttpRequest("user/addUserCertification.do",params,simpleCallBack);
    }

    //获取实名认证信息接口
    public static void getUserCertificationInfo(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getUserCertificationInfo.do",params,simpleCallBack);
    }

    //获取用户邀请总人数接口
    public static void getUserInviteTotal(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getUserInviteTotal.do",params,simpleCallBack);
    }

    //获取网店绑定邂逅人申请列表接口
    public static void getShopBindUserList(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getShopBindUserList.do",params,simpleCallBack);
    }

    //用户同意经理人申请接口
    public static void agreeManagerRecord(String id,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("id",id);
        new HttpRequest("user/agreeManagerRecord.do",params,simpleCallBack);
    }

    //福利中心
    public static void getWelfareCenterList(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getWelfareCenterList.do",params,simpleCallBack);
    }

    //领取福利
    public static void receiveCoupon(String uId,String id,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("id",id);
        new HttpRequest("user/receiveCoupon.do",params,simpleCallBack);
    }

    //领取福利弹窗
    public static void getReceiveCoupon(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getReceiveCoupon.do",params,simpleCallBack);
    }

    //分享任务
    public static void userShare(String uId,int type,String storeId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("type",type);
        params.put("storeId",storeId);
        new HttpRequest("user/userShare.do?",params,simpleCallBack);
    }

    //获取邀请用户列表
    public static void getUserInviteList(String uId,int currentPage,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("currentPage",currentPage);
        new HttpRequest("user/getUserInviteList.do?",params,simpleCallBack);
    }

    //获取邀请用户列表
    public static void getUserInviteTenPerson(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getUserInviteTenPerson.do?",params,simpleCallBack);
    }

    //用户开通合伙人接口
    public static void userOpenPartner(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/userOpenPartner.do?",params,simpleCallBack);
    }

    //获取活动
    public static void getActityList(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/getActityList.do?",params,simpleCallBack);
    }

    //是否第一次打开应用
    public static void updatelsFirstLanding(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/updateIsFirstLanding.do?",params,simpleCallBack);
    }

    //经理人是否第一次打开应用
    public static void updateManagerIsFirstLanding(String uId,SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        new HttpRequest("user/updateManagerIsFirstLanding.do?",params,simpleCallBack);
    }

    //添加可能邂逅的人
    public static void regNewUserByVirtual(String uId,String nickName, String height,String sex,String weight,
                                         String age,List<File> file, String fileKey,
                                         ProgressResponseCallBack progressResponseCallBack, SimpleCallBack<String> simpleCallBack){
        RequestParams params = new RequestParams();
        params.put("uId",uId);
        params.put("nickName",nickName);
        params.put("height",height);
        params.put("sex",sex);
        params.put("weight",weight);
        params.put("age",age);
        new HttpRequest("login/regNewUserByVirtual.do",params,file,fileKey,progressResponseCallBack,simpleCallBack);
    }
















}

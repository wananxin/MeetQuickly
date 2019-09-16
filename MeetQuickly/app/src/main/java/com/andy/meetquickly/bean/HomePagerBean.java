package com.andy.meetquickly.bean;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/6/17 13:28
 * 描述：
 */
public class HomePagerBean {


    /**
     * managerStore : [{"activityName":"免包厢费，进店开机送一打酒","address":"芙蓉路","appVersion":null,"area":"开福区","auditTime":null,"auditUser":null,"businessTime":"00:00-00:00","cashCouponProportion":null,"cashCouponReturn":null,"cashCouponReturnManager":null,"cashCouponReturnUser":null,"city":"长沙","companyId":null,"companyName":"万代","couponAmount":null,"coverImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/6/11/402881e76b467c07016b467c07010000.jpg","createTime":"2018-10-18 09:15:00","discount":"8","distance":null,"feature":"没有什么能够阻挡","id":"1027","imgs":null,"isCoupon":"0","isDelete":null,"isFreeDriving":null,"isHot":null,"isNew":null,"isOnline":"1","kind":"1","mobile":"18890057006","placeScore":null,"province":"湖南","reason":null,"remark":"服务与游戏创新一体一家好玩的ktv，能让你心灵释放的地方。","satisfiedScore":null,"serviceScore":null,"sort":null,"status":null,"storeCoupon":null,"storeId":"1105","storeImgs":[],"storeName":"小丹的小店","storeVisit":null,"uId":"20000241","userInfo":null},{"activityName":null,"address":"芙蓉路","appVersion":null,"area":"开福区","auditTime":null,"auditUser":null,"businessTime":"12:00-05:00","cashCouponProportion":null,"cashCouponReturn":null,"cashCouponReturnManager":null,"cashCouponReturnUser":null,"city":"长沙","companyId":null,"companyName":"万代","couponAmount":null,"coverImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/6/11/402881e76b467c07016b467c07010000.jpg","createTime":"2018-10-18 20:13:21","discount":"2","distance":null,"feature":"真空。不穿内衣。各种游戏节目舞蹈","id":"1028","imgs":null,"isCoupon":"0","isDelete":null,"isFreeDriving":null,"isHot":null,"isNew":null,"isOnline":"1","kind":"1","mobile":"18607313775","placeScore":null,"province":"湖南","reason":null,"remark":"真空包装！游戏舞蹈节目","satisfiedScore":null,"serviceScore":null,"sort":null,"status":null,"storeCoupon":null,"storeId":"1191","storeImgs":[],"storeName":"国贸KTV","storeVisit":null,"uId":"20000298","userInfo":null}]
     * partnerInfo : {"amount":"0.00","walletBalance":"378.00","groupCount":"1","sumYestoday":"0"}
     * usersOrdersList : [{"activityId":"402881e76ae3b12e016ae3bc524d003c","address":"芙蓉路","adminVerify":null,"affirmOrder":null,"area":"开福区","cashCouponProportion":"1.5","cashCouponReturnUser":"0.3","city":"长沙","companyName":"万代","consumptionReturnOne":"0.5","consumptionReturnThree":"0.3","consumptionReturnTwo":"0.2","count":"1","coupon":"500.00","couponMoney":"800.00","coverImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/6/11/402881e76b467c07016b467c07010000.jpg","createTime":"2019-04-15","face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/6/14/2c91d0326b556450016b55caf519001f.jpg","id":"472","isFreeDriving":null,"latitude":"28.242271","longitude":"113.018967","managerMoney":null,"managerProof1":null,"managerProof2":null,"managerVerify":null,"missLovers":"20000340,20000341,20000333,20005614,20000270,20000268","name":"郭","orderId":"20190415210043","orderStatus":"2","paymentAmount":null,"peoples":"6","province":"湖南","reachStore":"2","reachStoreTime":"1559011374","reachTime":"20:00","remark":"要小姐姐哦","returnMoney":null,"sex":"1","storeActivity":null,"storeId":"1105","storeMobile":"18890057006","storeName":"小丹的小店","tables":"中包","targetDate":"5-24","uId":"20005614","useCoupon":"1","userFeedback":null,"userFeedbackTime":null,"userInfo":null,"userProof":null,"userVerify":null,"userVerityTime":null}]
     * unpaid : 0
     * managerOrdersList : [{"activityId":"402881e76ae3b12e016ae3bc524d003c","address":"芙蓉路","adminVerify":null,"affirmOrder":null,"area":"开福区","cashCouponProportion":"1.5","cashCouponReturnUser":"0.3","city":"长沙","companyName":"万代","consumptionReturnOne":"0.5","consumptionReturnThree":"0.3","consumptionReturnTwo":"0.2","count":"1","coupon":"500.00","couponMoney":"800.00","coverImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/6/11/402881e76b467c07016b467c07010000.jpg","createTime":"2019-04-15","face":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/6/14/2c91d0326b556450016b55caf519001f.jpg","id":"472","isFreeDriving":null,"latitude":"28.242271","longitude":"113.018967","managerMoney":null,"managerProof1":null,"managerProof2":null,"managerVerify":null,"missLovers":"20000340,20000341,20000333,20005614,20000270,20000268","name":"郭","orderId":"20190415210043","orderStatus":"2","paymentAmount":null,"peoples":"6","province":"湖南","reachStore":"2","reachStoreTime":"1559011374","reachTime":"20:00","remark":"要小姐姐哦","returnMoney":null,"sex":"1","storeActivity":null,"storeId":"1105","storeMobile":"18890057006","storeName":"小丹的小店","tables":"中包","targetDate":"5-24","uId":"20005614","useCoupon":"1","userFeedback":null,"userFeedbackTime":null,"userInfo":null,"userProof":null,"userVerify":null,"userVerityTime":null}]
     */

    private PartnerInfoBean partnerInfo;
    private int unpaid;
    private List<ShopInfoBean> managerStore;
    private List<OrderDetailBean> usersOrdersList;
    private List<OrderDetailBean> managerOrdersList;

    public PartnerInfoBean getPartnerInfo() {
        return partnerInfo;
    }

    public void setPartnerInfo(PartnerInfoBean partnerInfo) {
        this.partnerInfo = partnerInfo;
    }

    public int getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(int unpaid) {
        this.unpaid = unpaid;
    }

    public List<ShopInfoBean> getManagerStore() {
        return managerStore;
    }

    public void setManagerStore(List<ShopInfoBean> managerStore) {
        this.managerStore = managerStore;
    }

    public List<OrderDetailBean> getUsersOrdersList() {
        return usersOrdersList;
    }

    public void setUsersOrdersList(List<OrderDetailBean> usersOrdersList) {
        this.usersOrdersList = usersOrdersList;
    }

    public List<OrderDetailBean> getManagerOrdersList() {
        return managerOrdersList;
    }

    public void setManagerOrdersList(List<OrderDetailBean> managerOrdersList) {
        this.managerOrdersList = managerOrdersList;
    }

    public static class PartnerInfoBean {
        /**
         * amount : 0.00
         * walletBalance : 378.00
         * groupCount : 1
         * sumYestoday : 0
         */

        private String amount;
        private String walletBalance;
        private String groupCount;
        private String sumYestoday;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getWalletBalance() {
            return walletBalance;
        }

        public void setWalletBalance(String walletBalance) {
            this.walletBalance = walletBalance;
        }

        public String getGroupCount() {
            return groupCount;
        }

        public void setGroupCount(String groupCount) {
            this.groupCount = groupCount;
        }

        public String getSumYestoday() {
            return sumYestoday;
        }

        public void setSumYestoday(String sumYestoday) {
            this.sumYestoday = sumYestoday;
        }
    }
}

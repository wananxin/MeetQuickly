package com.andy.meetquickly.bean;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/20 13:54
 * 描述：
 */
public class BillsBean {


    /**
     * total : 211.00
     * commissionCount : 200.00
     * giftCount : 11.00
     * couponCount : 0.00
     */

    private String total;
    private String commissionCount;
    private String giftCount;
    private String couponCount;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCommissionCount() {
        return commissionCount;
    }

    public void setCommissionCount(String commissionCount) {
        this.commissionCount = commissionCount;
    }

    public String getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(String giftCount) {
        this.giftCount = giftCount;
    }

    public String getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(String couponCount) {
        this.couponCount = couponCount;
    }
}

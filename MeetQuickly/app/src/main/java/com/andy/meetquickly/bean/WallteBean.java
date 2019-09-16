package com.andy.meetquickly.bean;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/16 14:46
 * 描述：
 */
public class WallteBean {


    /**
     * commissionCount : 100.00
     * commissionCountTem : 0.00
     * kbCount : 0.00
     * otherIncomes : 0.00
     * walletBalance : 100.0
     */

    private String commissionCount;
    private String commissionCountTem;
    private String kbCount;
    private String otherIncomes;
    private String walletBalance;

    public String getCommissionCount() {
        return commissionCount;
    }

    public void setCommissionCount(String commissionCount) {
        this.commissionCount = commissionCount;
    }

    public String getCommissionCountTem() {
        return commissionCountTem;
    }

    public void setCommissionCountTem(String commissionCountTem) {
        this.commissionCountTem = commissionCountTem;
    }

    public String getKbCount() {
        return kbCount;
    }

    public void setKbCount(String kbCount) {
        this.kbCount = kbCount;
    }

    public String getOtherIncomes() {
        return otherIncomes;
    }

    public void setOtherIncomes(String otherIncomes) {
        this.otherIncomes = otherIncomes;
    }

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }
}

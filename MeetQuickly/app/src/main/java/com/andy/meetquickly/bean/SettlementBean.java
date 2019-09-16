package com.andy.meetquickly.bean;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/20 14:21
 * 描述：
 */
public class SettlementBean {


    /**
     * accountNumber : null
     * accountType : 1
     * auditorUid : null
     * canAccountTime : 2019-05-21 13:05:51
     * commissionId : null
     * id : 61
     * idCard : null
     * money : 1.00
     * name :
     * orderTradeNo : TNO20190520133151074660760
     * remark : null
     * status : 0
     * time : 2019-05-20 13:05:51
     * uId : 20005630
     * updateTime : null
     */

    private String accountNumber;
    private String accountType;
    private String canAccountTime;
    private String id;
    private String money;
    private String orderTradeNo;
    private String status;
    private String time;
    private String uId;
    private String updateTime;
    private String balance;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCanAccountTime() {
        return canAccountTime;
    }

    public void setCanAccountTime(String canAccountTime) {
        this.canAccountTime = canAccountTime;
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

    public String getOrderTradeNo() {
        return orderTradeNo;
    }

    public void setOrderTradeNo(String orderTradeNo) {
        this.orderTradeNo = orderTradeNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}

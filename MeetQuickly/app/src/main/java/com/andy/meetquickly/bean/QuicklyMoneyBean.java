package com.andy.meetquickly.bean;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/16 15:03
 * 描述：
 */
public class QuicklyMoneyBean {


    /**
     * balance : 0
     * des : 快币充值
     * expense : 0
     * giftMoneyId : null
     * id : 6299
     * income : 100
     * mobileSystem : null
     * time : 2019-05-16 14:05:07
     * type : 1
     * uId : 20005630
     */

    private int balance;
    private String des;
    private int expense;
    private String id;
    private int income;
    private String time;
    private String type;
    private String uId;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

package com.andy.meetquickly.bean;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/20 17:39
 * 描述：
 */
public class RechargeBean {

    String billNumb;
    String billMoney;
    String money;

    public String getBillNumb() {
        return billNumb;
    }

    public void setBillNumb(String billNumb) {
        this.billNumb = billNumb;
    }

    public String getBillMoney() {
        return billMoney;
    }

    public void setBillMoney(String billMoney) {
        this.billMoney = billMoney;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public RechargeBean(String billNumb, String billMoney, String money) {
        this.billNumb = billNumb;
        this.billMoney = billMoney;
        this.money = money;
    }

    public RechargeBean(){

    }
}

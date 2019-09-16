package com.andy.meetquickly.bean;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/7/25 14:28
 * 描述：
 */
public class ConsumptionBillBean {


    /**
     * id : 563
     * managerProof1 : http://replace-i.oss-cn-hangzhou.aliyuncs.com/
     * managerProof2 : http://replace-i.oss-cn-hangzhou.aliyuncs.com/
     * orderId : 20190725001001
     */

    private String id;
    private String managerProof1;
    private String managerProof2;
    private String orderId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManagerProof1() {
        return managerProof1;
    }

    public void setManagerProof1(String managerProof1) {
        this.managerProof1 = managerProof1;
    }

    public String getManagerProof2() {
        return managerProof2;
    }

    public void setManagerProof2(String managerProof2) {
        this.managerProof2 = managerProof2;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

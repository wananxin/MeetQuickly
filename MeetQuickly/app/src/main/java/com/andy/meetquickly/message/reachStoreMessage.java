package com.andy.meetquickly.message;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/24 16:35
 * 描述：
 */
public class reachStoreMessage {

    private String orderId;
    public reachStoreMessage(String orderId){
        this.orderId=orderId;
    }
    public String getMessage() {
        return orderId;
    }

    public void setMessage(String orderId) {
        this.orderId = orderId;
    }

}

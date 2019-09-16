package com.andy.meetquickly.message;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/6/6 13:48
 * 描述：
 */
public class receiveCouponMessage {

    private String message;
    public receiveCouponMessage(String message){
        this.message=message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

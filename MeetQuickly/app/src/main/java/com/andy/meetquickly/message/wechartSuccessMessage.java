package com.andy.meetquickly.message;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/24 16:35
 * 描述：
 */
public class wechartSuccessMessage {

    private String message;
    public wechartSuccessMessage(String message){
        this.message=message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

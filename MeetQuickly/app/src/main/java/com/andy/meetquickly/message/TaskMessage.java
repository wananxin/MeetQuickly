package com.andy.meetquickly.message;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/24 16:35
 * 描述：
 */
public class TaskMessage {

    private int message;
    public TaskMessage(int message){
        this.message=message;
    }
    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

}

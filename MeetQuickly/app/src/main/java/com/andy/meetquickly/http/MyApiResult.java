package com.andy.meetquickly.http;

import com.zhouyou.http.model.ApiResult;


/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/24 19:51
 * 描述：
 */
public class MyApiResult<T> extends ApiResult<T> {
//public class MyApiResult<T> implements Serializable {


    /**
     * data : null
     * isLastPage : false
     * message : 验证码发送成功
     * result : true
     * status : 1
     */

//    transient T data;
    boolean isLastPage;
    String message;
    boolean result;
    int status;

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public int getCode() {
        return status;
    }

    public void setCode(int code) {
        this.status = code;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }

//    public T getData() {
//        return data;
//    }
//
//    public void setData(T data) {
//        this.data = data;
//    }

    public boolean isOk() {
        return status == 1;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code='" + status + '\'' +
                ", msg='" + message + '\'' +
                ", data="  +
                '}';
    }
}

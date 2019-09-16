package com.andy.meetquickly.http;

import android.content.Context;

import com.zhouyou.http.callback.CallBackProxy;

import java.util.Map;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/24 19:33
 * 描述：
 */
public interface NetRequest {

    void init(Context context);

    void doGet(String url, final Map<String, String> paramsMap, final IResponseListener iResponseListener);

    void doPost(String url, final Map<String, String> paramsMap, final CallBackProxy iResponseListener);

    void cancel(Object tag);

}

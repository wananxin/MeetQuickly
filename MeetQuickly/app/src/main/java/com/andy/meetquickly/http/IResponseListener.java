package com.andy.meetquickly.http;

import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.callback.CallBackProxy;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/24 19:36
 * 描述：
 */
public interface IResponseListener {

    void onResponse(MyApiResult responseResult);

}

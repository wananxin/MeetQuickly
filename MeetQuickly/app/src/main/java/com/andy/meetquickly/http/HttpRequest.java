package com.andy.meetquickly.http;

import android.content.Context;

import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.utils.LogUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.model.ApiResult;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/24 19:39
 * 描述：
 */
public class HttpRequest {

    public HttpRequest(String url,Map<String,String> stringMap,SimpleCallBack<String> simpleCallBack){
        String logInfo = Constant.API_SERVER + url + "?";
        for (String key : stringMap.keySet()) {
            logInfo = logInfo + key + "/" + stringMap.get(key) + "/";
        }
        LogUtil.e("请求地址为:"+logInfo);
        doPost(url,stringMap,simpleCallBack);
    }

    public HttpRequest(String url, Map<String,String> stringMap, File file, String fileKey, ProgressResponseCallBack progressResponseCallBack,SimpleCallBack<String> simpleCallBack){
        String logInfo = url + "?";
        for (String key : stringMap.keySet()) {
            logInfo = logInfo + key + "/" + stringMap.get(key) + "/";
        }
        logInfo = logInfo + "file/"+fileKey+"/"+file.getName();
        LogUtil.e("请求地址为:"+logInfo);
        doFilePost(url,stringMap,file,fileKey,progressResponseCallBack,simpleCallBack);
    }

    public HttpRequest(String url, Map<String,String> stringMap, List<File> file, String fileKey, ProgressResponseCallBack progressResponseCallBack, SimpleCallBack<String> simpleCallBack){
        String logInfo = url + "?";
        for (String key : stringMap.keySet()) {
            logInfo = logInfo + key + "/" + stringMap.get(key) + "/";
        }
        logInfo = logInfo + "file/"+fileKey+"/"+file.getClass().toString()+"文件数量:"+file.size();
        LogUtil.e("请求地址为:"+logInfo);
        doFileListPost(url,stringMap,file,fileKey,progressResponseCallBack,simpleCallBack);
    }

    private void doFileListPost(String url, Map<String, String> stringMap, List<File> file, String fileKey, ProgressResponseCallBack progressResponseCallBack, SimpleCallBack<String> simpleCallBack) {
        EasyHttp.post(url)
                .addFileParams(fileKey,file,progressResponseCallBack)
                .params(stringMap)
                .execute(new CallBackProxy<MyApiResult<String>, String>(simpleCallBack) {
                });
    }

    private void doFilePost(String url, Map<String, String> stringMap, File file, String fileKey, ProgressResponseCallBack progressResponseCallBack, SimpleCallBack<String> simpleCallBack) {
        EasyHttp.post(url)
                .params(fileKey,file,progressResponseCallBack)
                .params(stringMap)
                .execute(new CallBackProxy<MyApiResult<String>, String>(simpleCallBack) {
                });
    }

    public void doGet(String url, Map<String, String> paramsMap, IResponseListener iResponseListener) {

    }

    public void doPost(String url, Map<String, String> paramsMap, SimpleCallBack<String> simpleCallBack) {
        EasyHttp.post(url)
                .params(paramsMap)
                .execute(new CallBackProxy<MyApiResult<String>, String>(simpleCallBack) {
                });
    }

    public void cancel(Object tag) {

    }
}

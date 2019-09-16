package com.andy.meetquickly.utils;

import java.io.IOException;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.alibaba.fastjson.JSON;


public class SharedPreUtil
{
 
     
    private static SharedPreUtil s_SharedPreUtil;
     
  
    private SharedPreferences msp;
     
    // 初始化，一般在应用启动之后就要初始化
    public static synchronized void initSharedPreference(Context context)
    {
        if (s_SharedPreUtil == null)
        {
            s_SharedPreUtil = new SharedPreUtil(context);
        }
    }
     
    /**
     * 获取唯一的instance
     *
     * @return
     */
    public static synchronized SharedPreUtil getInstance()
    {
        return s_SharedPreUtil;
    }
     
    @SuppressLint("WrongConstant")
    public SharedPreUtil(Context context)
    {
        msp = context.getSharedPreferences("SharedPreUtil",
                Context.MODE_PRIVATE | Context.MODE_APPEND);
    }
     
    public SharedPreferences getSharedPref()
    {
        return msp;
    }


    // 保存bean对象
    public void saveBeanByFastJson(  String key,Object obj){
        Editor editor = msp.edit();
        String toJson = JSON.toJSONString(obj);
        editor.putString(key,toJson).commit();
    }

    // 提取bean对象
    public String getBeanByFastJson(String key){
        String objString = null;
        objString =  msp.getString(key, "");
        return objString;
//        JSON.parseObject(objString, clazz)
    }

    public synchronized void delete(String key)
    {
        Editor editor = msp.edit();
        editor.putString(key,"");
     
        editor.commit();
 
    }

}

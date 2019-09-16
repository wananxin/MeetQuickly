package com.andy.meetquickly.cache;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.activity.MeetQuicklyApp;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.utils.LogUtil;
import com.andy.meetquickly.utils.SharedPreUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.vondear.rxtool.RxAppTool;
import com.vondear.rxtool.RxDeviceTool;

/**
 * Created by Administrator on 2018/9/28.
 */

public class UserCache {

    private static UserCache instance;
    private UserBean mUser;
    public static final String USER_CACHE = "user";

    public synchronized static UserCache getInstance() {
        if(null == instance){
            instance = new UserCache();
        }
        return instance;
    }

    public void clear(){
        mUser = null;
        SharedPreUtil.getInstance().delete(USER_CACHE);
    }

    public void update(UserBean user){
        user.setVersionNumber(RxAppTool.getAppVersionCode(MeetQuicklyApp.getInstance())+"");
        SharedPreUtil.getInstance().saveBeanByFastJson(USER_CACHE,user);
        load();
    }

    public void load() {

        String data= null;
        data = SharedPreUtil.getInstance().getBeanByFastJson(USER_CACHE);
        if (StringUtil.isNotNull(data)){
            mUser = JSON.parseObject(data, UserBean.class);
        }else {
            mUser = null;
        }
    }

    public UserBean getUser() {
        return mUser;
    }

}

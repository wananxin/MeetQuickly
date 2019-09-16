package com.andy.meetquickly.utils;


import com.orhanobut.logger.Logger;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/25 17:37
 * 描述：
 */
public class LogUtil {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    /**控制想要打印的日志级别
     * 等于VERBOSE，则就会打印所有级别的日志
     * 等于WARN，则只会打印警告级别以上的日志
     * 等于NOTHING，则会屏蔽掉所有日志*/
    public static final int LEVEL = VERBOSE;



    public static void v(String msg){
        if(LEVEL <= VERBOSE){
            Logger.v(msg);
        }
    }

    public static void d(String msg){
        if(LEVEL <= DEBUG){
            Logger.d(msg);
        }
    }

    public static void i(String msg){
        if(LEVEL <= INFO){
            Logger.i(msg);
        }
    }

    public static void w(String msg){
        if(LEVEL <= WARN){
            Logger.w(msg);
        }
    }

    public static void e(String msg){
        if(LEVEL <= ERROR){
            Logger.e(msg);
        }
    }

    public static void e(String Tag,String msg){
        if(LEVEL <= ERROR){
            Logger.e(msg);
        }
    }
}

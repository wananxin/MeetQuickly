package com.andy.meetquickly.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.andy.meetquickly.activity.user.CheckGustureActivity;
import com.andy.meetquickly.activity.user.GestureActivity;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.HttpInit;
import com.andy.meetquickly.message.MyTextMessageItemProvider;
import com.andy.meetquickly.utils.LogUtil;
import com.andy.meetquickly.utils.SharedPreUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.andy.meetquickly.view.MyConversationClickListener;
import com.andy.meetquickly.view.plugin.GiftItemProvider;
import com.andy.meetquickly.view.plugin.GiftMessage;
import com.andy.meetquickly.view.plugin.MyExtensionModule;
import com.andy.meetquickly.view.plugin.UserCardItemProvider;
import com.andy.meetquickly.view.plugin.UserCardMessage;
import com.baidu.mapapi.SDKInitializer;
import com.kongzue.dialog.v2.DialogSettings;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.vondear.rxtool.RxTool;

import java.util.List;

import cn.jpush.android.api.JPushInterface;
import iknow.android.utils.BaseUtils;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.push.RongPushClient;
import io.rong.push.pushconfig.PushConfig;
import nl.bravobit.ffmpeg.FFmpeg;

import static com.kongzue.dialog.v2.DialogSettings.STYLE_MATERIAL;
import static com.kongzue.dialog.v2.DialogSettings.THEME_LIGHT;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/18 17:49
 * 描述：初始化App
 */
public class MeetQuicklyApp extends Application {

    private static MeetQuicklyApp instance;
    private int mFinalCount = 0;
    private Long startforeGround;
    private Long startBackground;

    public static MeetQuicklyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = (MeetQuicklyApp) getApplicationContext();
        AppFrontBackHelper appFrontBackHelper = new AppFrontBackHelper();
        appFrontBackHelper.register(MeetQuicklyApp.this, new AppFrontBackHelper.OnAppStatusListener() {
            @Override
            public void onFront() {
                startforeGround = System.currentTimeMillis();
                if (mFinalCount == 0){
                    mFinalCount = mFinalCount + 1;
                } else {
                    doExit();
                }
            }

            @Override
            public void onBack() {
                //应用切到后台处理
                startBackground = System.currentTimeMillis();
            }
        });
        //Dialog初始化
        DialogSettings.use_blur = true;                 //设置是否启用模糊
        DialogSettings.style = STYLE_MATERIAL;          //设置弹窗样式
        DialogSettings.tip_theme = THEME_LIGHT;         //设置模式
        DialogSettings.dialog_theme = THEME_LIGHT;      //设置模式
        DialogSettings.dialog_cancelable_default = true;
        //初始化Http
        HttpInit.init(this);
        RxTool.init(this);
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // 是否显示线程信息 默认显示 上图Thread Infrom的位置
                .tag("meet_quickly")   // 自定义全局tag 默认：PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        MultiDex.install(this);
        //初始化APP
        //初始化百度地图
        SDKInitializer.initialize(this);
        //初始化SharedPreference
        SharedPreUtil.initSharedPreference(this);
        UserCache.getInstance().load();

        //极光推送初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);


        //对缓存的用户信息初始化
        //友盟初始化
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this,"5cd1260c570df3db6b0001b0"
                ,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
        PlatformConfig.setWeixin(Constant.WECHART_ID, "3c06e1b314a822d94587837d2bc2c43a");
        //腾讯BUGLY初始化
        Bugly.init(getApplicationContext(), "b99217ab5d", true);
        //融云初始化
        PushConfig config = new PushConfig.Builder()
                .enableMiPush("2882303761518029876", "5591802999876")
                .enableHWPush(true)
                .enableOppoPush("748ced8e61e84ade86ab3f2ebec65eb3","ea9f859f5a344b51bb71b438dee67952")
                .build();
        RongPushClient.setPushConfig(config);
        RongIM.init(this);
        RongIM.registerMessageType(GiftMessage.class);
        RongIM.getInstance().registerMessageTemplate(new GiftItemProvider());
        RongIM.registerMessageType(UserCardMessage.class);
        RongIM.getInstance().registerMessageTemplate(new UserCardItemProvider());
        setMyExtensionModule();
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                LogUtil.e(message.toString());
                return false;
            }
        });

        RongIM.registerMessageTemplate(new MyTextMessageItemProvider());
        RongIM.getInstance().setConversationClickListener(new MyConversationClickListener());


        //视频剪切
        BaseUtils.init(this);
        initFFmpegBinary(this);

    }

    private void initFFmpegBinary(Context context) {
        if (!FFmpeg.getInstance(context).isSupported()) {
            Log.e("InitApplication","Android cup arch not supported!");
        }
    }

    public void setMyExtensionModule() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
            }
        }
    }

    private void doExit() {
        //120s！我这里前后台切换大于120s才进行唤醒touchid
        if (startBackground != 0 && startforeGround != 0) {
            long l = startforeGround - startBackground;
            if (l > 12 * 1000) {
                //TODO 做你自己的处理
                if (null != UserCache.getInstance().getUser()){
                    if ("1".equals(UserCache.getInstance().getUser().getGesturesStatus()) &&
                            StringUtil.isNotNull(UserCache.getInstance().getUser().getGesturesPassword())){
                        CheckGustureActivity.start(MeetQuicklyApp.this,0);
                    }
                }
            }
        }
    }

}

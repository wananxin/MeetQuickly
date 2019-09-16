package com.andy.meetquickly.activity;

import android.Manifest;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.login.ForgetPsdActivity;
import com.andy.meetquickly.activity.login.LoginActivity;
import com.andy.meetquickly.activity.user.CheckGustureActivity;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.utils.CommentUtil;
import com.andy.meetquickly.utils.LogUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxAppTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

public class SplashActivity extends BaseActivity {

    LocationClient mLocationClient = null;
    private TimeCount time;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        getPermissions();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
    }

    private void connect(String token) {

        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    LogUtil.e("--onTokenIncorrect" + "Token 错误");
                    RxToast.showToast("融云Token 错误");
                    MainActivity.start(mContext);
                    finish();
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    LogUtil.e("--onSuccess" + userid);
                    //参数设置为 true，由 IMKit 来缓存用户信息。
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//                                @Override
//                                public UserInfo getUserInfo(String s) {
//                                    return findUserById(s);
//                                }
//                            }, true);
//                            MainActivity.start(mContext);
                            Map<String, Boolean> map = new HashMap<>();
                            map.put(Conversation.ConversationType.PRIVATE.getName(), false); // 会话列表需要显示私聊会话, 第二个参数 true 代表私聊会话需要聚合显示
                            map.put(Conversation.ConversationType.GROUP.getName(), false);  // 会话列表需要显示群组会话, 第二个参数 false 代表群组会话不需要聚合显示

                            RongIM.getInstance().startConversationList(mContext, map);
//                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtil.e("--onError" + errorCode);
                    RxToast.showToast("融云登录失败");
                    MainActivity.start(mContext);
                    finish();
                }
            });
        }
    }

    @Override
    public void initData() {

    }

    private void getPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEachCombined(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                //当CAMERA权限获取成功时，permission.granted=true
                userLocation();
            }
        });
    }

    private void userLocation() {
        showWaitDialog(SplashActivity.this, "正在定位");
        mLocationClient = new LocationClient(mContext.getApplicationContext());
        time = new TimeCount(6000, 1000);
        time.start();
        CommentUtil.getLocation(mContext, mLocationClient, (BDAbstractLocationListener) bdLocationListener);
    }

    BDAbstractLocationListener bdLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            dissmissWaitDialog();
            time.cancel();
            if (bdLocation.getLocType() == 161) {
                RxSPTool.putContent(SplashActivity.this, Constant.KEY_LONGITUDE, String.valueOf(bdLocation.getLongitude()));
                RxSPTool.putContent(SplashActivity.this, Constant.KEY_LATITUDE, String.valueOf(bdLocation.getLatitude()));
                RxSPTool.putContent(SplashActivity.this, Constant.KEY_ADDRESS, String.valueOf(bdLocation.getAddrStr()));
            } else {
                RxToast.showToast("定位错误，请打开定位权限");
                RxSPTool.putContent(SplashActivity.this, Constant.KEY_LONGITUDE, "");
                RxSPTool.putContent(SplashActivity.this, Constant.KEY_LATITUDE, "");
                RxSPTool.putContent(SplashActivity.this, Constant.KEY_ADDRESS, "");
            }
            doNext();
        }
    };

    public void doNext() {
        UserBean userBean = UserCache.getInstance().getUser();
        if (null != userBean) {

            if (StringUtil.isNotNull(userBean.getVersionNumber())) {
                if (String.valueOf(RxAppTool.getAppVersionCode(SplashActivity.this)).equals(userBean.getVersionNumber())) {
                    if (null != userBean.getRongyunToken()) {
                        if ("1".equals(UserCache.getInstance().getUser().getGesturesStatus()) &&
                                StringUtil.isNotNull(UserCache.getInstance().getUser().getGesturesPassword())) {
                            CheckGustureActivity.start(SplashActivity.this, 1);
                            finish();
                        } else {
                            connect(userBean.getRongyunToken());
                        }
                    } else {
                        RxToast.showToast("没有融云");
                        MainActivity.start(mContext);
                        finish();
                    }
                } else {
                    LoginActivity.start(mContext);
                    finish();
                }
            } else {
                LoginActivity.start(mContext);
                finish();
            }
        } else {
            LoginActivity.start(mContext);
            finish();
        }
    }

    class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (null != mLocationClient) {
                mLocationClient.unRegisterLocationListener(bdLocationListener);
            }
            RxToast.showToast("定位错误，请打开定位权限");
            RxSPTool.putContent(SplashActivity.this, Constant.KEY_LONGITUDE, "");
            RxSPTool.putContent(SplashActivity.this, Constant.KEY_LATITUDE, "");
            RxSPTool.putContent(SplashActivity.this, Constant.KEY_ADDRESS, "");
            doNext();
        }
    }

    @Override
    protected void onDestroy() {
        if (null != mLocationClient) {
            mLocationClient.unRegisterLocationListener(bdLocationListener);
        }
        if (null != time) {
            time.cancel();
        }
        super.onDestroy();

    }
}

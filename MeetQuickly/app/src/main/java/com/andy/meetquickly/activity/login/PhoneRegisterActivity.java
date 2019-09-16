package com.andy.meetquickly.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.MainActivity;
import com.andy.meetquickly.activity.home.CommentAgreementActivity;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

public class PhoneRegisterActivity extends BaseActivity {

    @BindView(R.id.et_username)
    AppCompatEditText etUsername;
    @BindView(R.id.checkBox)
    CheckBox checkBox;

    @Override
    public int getLayoutId() {
        return R.layout.activity_phone_register;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PhoneRegisterActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_sure, R.id.ll_wechart, R.id.ll_login, R.id.textViewXieyi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                if (checkBox.isChecked()) {
                    sendCaptchas();
                }else {
                    RxToast.showToast("请同意用户协议");
                }
                break;
            case R.id.ll_wechart:
                weChartLogin();
                break;
            case R.id.ll_login:
                LoginActivity.start(mContext);
                break;
            case R.id.textViewXieyi:
                CommentAgreementActivity.start(mContext,"快逅用户协议",Constant.USER_AGREEMENT);
                break;
        }
    }

    private void weChartLogin() {
        UMShareAPI.get(PhoneRegisterActivity.this).deleteOauth(PhoneRegisterActivity.this, SHARE_MEDIA.WEIXIN, authListener);
    }

    UMAuthListener authListener = new UMAuthListener() {

        private String wxOpenId;

        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (null != data) {
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    if (entry.getKey().equals("unionid")) {
                        wxOpenId = entry.getValue();
                        break;
                    }
                }
                WechartLogin(wxOpenId);
            } else {
                UMShareAPI.get(PhoneRegisterActivity.this).getPlatformInfo(PhoneRegisterActivity.this, SHARE_MEDIA.WEIXIN, authListener);
            }

        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            RxToast.showToast("失败了");
            LogUtil.e("失败了");
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            RxToast.showToast("取消了");
            LogUtil.e("取消了");
        }
    };

    private void sendCaptchas() {
        String phone = etUsername.getText().toString().trim();
        if (phone.isEmpty() || phone.length() < 11) {
            return;
        }
        MQApi.sendCaptchas(phone, "REG_MSG", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                Log.e("aaaa", e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                        }.getType());
//                        JSON.parseObject(s, new MyApiResult<String>().getClass());
                if (myApiResult.isOk()) {
                    EditVerifyActivity.start(mContext, phone);
                    finish();
                } else {
                    Toast.makeText(mContext, myApiResult.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void WechartLogin(String wxOpenId) {
        showWaitDialog(PhoneRegisterActivity.this);
        MQApi.appUserLoginByWx(wxOpenId, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<UserBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<UserBean>>() {
                        }.getType());
//                                JSON.parseObject(s, new TypeReference<MyApiResult<UserBean>>(){});
                if (myApiResult.isOk()) {
                    if (null == myApiResult.getData()) {
                        BindPhoneActivity.start(PhoneRegisterActivity.this, wxOpenId);
                    } else {
                        if ("0".equals(myApiResult.getData().getIsPerfect())) {
                            EditInfomationActivity.start(mContext, myApiResult.getData().getMobile(),1);
                        } else {
                            UserCache.getInstance().update(myApiResult.getData());
                            connect(myApiResult.getData().getRongyunToken());
                        }
                    }
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }

            }
        });
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
}

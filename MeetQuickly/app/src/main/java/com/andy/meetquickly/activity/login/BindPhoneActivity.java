package com.andy.meetquickly.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.MainActivity;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.LogUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

public class BindPhoneActivity extends BaseActivity {

    @BindView(R.id.et_phone_numb)
    AppCompatEditText etPhoneNumb;
    @BindView(R.id.et_confirm_password)
    AppCompatEditText etConfirmPassword;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    private String wxOpenId;
    private TimeCount time;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void initView() {
        wxOpenId = this.getIntent().getStringExtra("wxOpenId");
        time = new TimeCount(60000, 1000);
    }

    @Override
    public void initData() {

    }

    public static void start(Context context, String wxOpenId) {
        Intent starter = new Intent(context, BindPhoneActivity.class);
        starter.putExtra("wxOpenId", wxOpenId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_get_code, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_get_code:
                getCode();
                break;
            case R.id.tv_sure:
                valiadCaptcha();
                break;
        }
    }

    private void valiadCaptcha() {
        if (!StringUtil.isNotNull(etConfirmPassword.getText().toString())){
            RxToast.showToast("请输入验证码");
            return;
        }
        showWaitDialog(BindPhoneActivity.this);
        MQApi.valiadCaptchas(etPhoneNumb.getText().toString(), etConfirmPassword.getText().toString(), wxOpenId, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<UserBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<UserBean>>() {
                }.getType());
                if (myApiResult.isOk()){
                    if (null == myApiResult.getData()){
                        EditInfomationActivity.start(mContext,etPhoneNumb.getText().toString(),0);
                        finish();
                    }else {
                        if ("0".equals(myApiResult.getData().getIsPerfect())){
                            EditInfomationActivity.start(mContext,myApiResult.getData().getMobile(),1);
                            finish();
                        }else {
                            UserCache.getInstance().update(myApiResult.getData());
                            connect(myApiResult.getData().getRongyunToken());
                        }
                    }
                }else {
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


    private void getCode() {
        if (!StringUtil.isNotNull(etPhoneNumb.getText().toString())){
            RxToast.showToast("请输入手机号码");
            return;
        }
        showWaitDialog(BindPhoneActivity.this);
        MQApi.sendCaptchas(etPhoneNumb.getText().toString(), "WX_MSG", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                }.getType());
                if (myApiResult.isOk()){
                    RxToast.showToast("短信发送成功...");
                    time.start();
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvGetCode.setClickable(false);
            tvGetCode.setText("("+millisUntilFinished / 1000 +") 秒");
        }

        @Override
        public void onFinish() {
            tvGetCode.setText("获取验证码");
            tvGetCode.setClickable(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        time.cancel();
    }
}

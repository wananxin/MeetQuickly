package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.MainActivity;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.SysPhoneBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.fragment.HomeFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.DaoHangUtil;
import com.andy.meetquickly.utils.LogUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.SelectDialog;
import com.vondear.rxtool.RxActivityTool;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.view.RxToast;
import com.wangnan.library.GestureLockView;
import com.wangnan.library.listener.OnGestureLockListener;
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

public class CheckGustureActivity extends BaseActivity {

    @BindView(R.id.glv)
    GestureLockView glv;
    @BindView(R.id.textViewForget)
    TextView textViewForget;

    UserBean userBean;
    int type; //0为不需要操作  1为需要跳main
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @Override
    public int getLayoutId() {
        return R.layout.activity_check_gusture;
    }

    @Override
    public void initView() {
        setBarTitle("绘制解锁图案");
        userBean = UserCache.getInstance().getUser();
        type = this.getIntent().getIntExtra("type",0);
        ivBack.setVisibility(View.INVISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setSwipeBackEnable(false);
        glv.setUseAnim(true);
        glv.setUseVibrate(true);
        glv.showErrorStatus(600);
        glv.setGestureLockListener(new OnGestureLockListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(String progress) {

            }

            @Override
            public void onComplete(String result) {
                if (!StringUtil.isNotNull(result)) {
                    return;
                }
                checkGusture(result);
            }
        });

        textViewForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void checkGusture(String result) {
        showWaitDialog(CheckGustureActivity.this);
        MQApi.checkGesturePassword(userBean.getUId(), result, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
                glv.clearView();
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                }.getType());
                if (myApiResult.isOk()) {
                    if (type == 0) {
                        finish();
                    } else {
                        connect(userBean.getRongyunToken());
                    }
                } else {
                    glv.clearView();
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

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public static void start(Context context, int type) {
        if (!RxActivityTool.currentActivity().getClass().equals(CheckGustureActivity.class)) {
            Intent starter = new Intent(context, CheckGustureActivity.class);
            starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            starter.putExtra("type", type);
            context.startActivity(starter);
        }
    }

    private long exitTime = 0;

    //退出按钮监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            RxToast.showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            RxActivityTool.AppExit(MainActivity.mContext);
        }
    }

    @OnClick(R.id.textViewForget)
    public void onViewClicked() {
        getPhone();
    }

    private void getPhone() {
        showWaitDialog(CheckGustureActivity.this);
        MQApi.getSysConfigInfo(new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<SysPhoneBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<SysPhoneBean>>() {
                }.getType());
                if (myApiResult.isOk()) {
                    showPhoneDialog(myApiResult.getData().getCONSUMER_HOTLINE());
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void showPhoneDialog(String consumer_hotline) {
        SelectDialog.show(CheckGustureActivity.this, "提示", "是否拨打客服电话："+consumer_hotline, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RxDeviceTool.dial(CheckGustureActivity.this, consumer_hotline);
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

    }
}

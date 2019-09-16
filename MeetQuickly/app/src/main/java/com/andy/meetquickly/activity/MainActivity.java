package com.andy.meetquickly.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.appointment.AppointmentInfoActivity;
import com.andy.meetquickly.activity.appointment.UseCouponActivity;
import com.andy.meetquickly.activity.appointment.YuYueMeetPeopleActivity;
import com.andy.meetquickly.activity.login.LoginActivity;
import com.andy.meetquickly.activity.user.TaskActivity;
import com.andy.meetquickly.activity.user.WebViewActivity;
import com.andy.meetquickly.activity.user.WelfareCenterActivity;
import com.andy.meetquickly.adapter.DialogWelfareAdapter;
import com.andy.meetquickly.base.BaseInvasionActivity;
import com.andy.meetquickly.bean.CouponBean;
import com.andy.meetquickly.bean.HomeWelfareBean;
import com.andy.meetquickly.bean.SysPhoneBean;
import com.andy.meetquickly.bean.UserBaseBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.fragment.FindFragment;
import com.andy.meetquickly.fragment.HomeFragment;
import com.andy.meetquickly.fragment.MessageFragment;
import com.andy.meetquickly.fragment.UserFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.TaskMessage;
import com.andy.meetquickly.message.homeEndMessage;
import com.andy.meetquickly.message.showGiftMessage;
import com.andy.meetquickly.message.updateHeadMessage;
import com.andy.meetquickly.utils.CommentUtil;
import com.andy.meetquickly.utils.LogUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.app.hubert.guide.model.RelativeGuide;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.SelectDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.vondear.rxtool.RxActivityTool;
import com.vondear.rxtool.RxAppTool;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/18  18:45
 * 描述：     主页面
 */
public class MainActivity extends BaseInvasionActivity {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    private HomeFragment homeFragment;
    private FindFragment findFragment;
    private MessageFragment messageFragment;
    private UserFragment userFragment;

    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;

    private List<Fragment> fragmentList = new ArrayList<>();

    private int selectID = 0;

    private FragmentManager fragmentManager;
    /**
     * 上一次界面 onSaveInstanceState 之前的tab被选中的状态 key 和 value
     */
    private static final String PRV_SELINDEX = "PREV_SELINDEX";
    private int selindex = 0;
    private UserBean userBean;

    /**
     * Fragment的TAG 用于解决app内存被回收之后导致的fragment重叠问题
     */
    private static final String[] FRAGMENT_TAG = {"home", "find", "message", "user"};

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTabSelection(0);
                    return true;
                case R.id.navigation_find:
                    setTabSelection(1);
                    return true;
                case R.id.navigation_center:
                    return false;
                case R.id.navigation_message:
                    setTabSelection(2);
                    return true;
                case R.id.navigation_user:
                    setTabSelection(3);
                    return true;
            }
            return false;
        }
    };
    private BottomNavigationView navigation;
    private Badge badge;
    private HomeWelfareBean homeWelfareBean;

    private void setTabSelection(int index) {
        // TODO Auto-generated method stub
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.content, homeFragment, FRAGMENT_TAG[index]);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                if (findFragment == null) {
                    findFragment = new FindFragment();
                    transaction.add(R.id.content, findFragment, FRAGMENT_TAG[index]);
                } else {
                    transaction.show(findFragment);
                }
                break;
            case 2:
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.content, messageFragment, FRAGMENT_TAG[index]);
                } else {
                    transaction.show(messageFragment);
                }
                break;
            case 3:
                if (userFragment == null) {
                    userFragment = new UserFragment();
                    transaction.add(R.id.content, userFragment, FRAGMENT_TAG[index]);
                } else {
                    transaction.show(userFragment);
                }
                break;

            default:
                break;
        }

        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (findFragment != null) {
            transaction.hide(findFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (userFragment != null) {
            transaction.hide(userFragment);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    CustomDialog customDialog;

    IUnReadMessageObserver iUnReadMessageObserver = new IUnReadMessageObserver() {
        @Override
        public void onCountChanged(int i) {
            if (null == badge) {
                // 具体child的查找和view的嵌套结构请在源码中查看
                // 从bottomNavigationView中获得BottomNavigationMenuView
                BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
                // 从BottomNavigationMenuView中获得childview, BottomNavigationItemView
                // 获得viewIndex对应子tab
                View view = menuView.getChildAt(3);

                BottomNavigationItemView itemView = (BottomNavigationItemView) view;
                // 从子tab中获得其中显示图片的ImageView
                View icon = itemView.findViewById(android.support.design.R.id.icon);
                // 获得图标的宽度
                int iconWidth = icon.getWidth() / 2;
                // 获得tab的宽度/2
                int tabWidth = itemView.getWidth() / 2;
                // 计算badge要距离右边的距离
                int spaceWidth = tabWidth - iconWidth;

                // 显示badegeview
                badge = new QBadgeView(MainActivity.this).bindTarget(itemView)
//                        .setGravityOffset(spaceWidth, 3, false)
                        .setBadgeNumber(i);
            } else {
                badge.setBadgeNumber(i);
            }
        }
    };


    @Override
    public void initView() {
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(String userId) {
                return findUserById(userId);//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
            }

        }, true);
        userBean = UserCache.getInstance().getUser();
        setSwipeBackEnable(false);
        setTabSelection(selectID);
        setTabSelection(2);
        setTabSelection(selectID);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYuYueDialog();
            }
        });
        DialogSettings.dialog_cancelable_default = true;

        RongIM.getInstance().addUnReadMessageCountChangedObserver(iUnReadMessageObserver, Conversation.ConversationType.PRIVATE);

        RongIM.getInstance().setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {
                switch (connectionStatus) {

                    case CONNECTED://连接成功。

                        break;
                    case DISCONNECTED://断开连接。

                        break;
                    case CONNECTING://连接中。

                        break;
                    case NETWORK_UNAVAILABLE://网络不可用。

                        break;
                    case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                        LoginActivity.start(mContext);
                        UserCache.getInstance().clear();
                        RxToast.showToast("您的账号已在其他设备登录！");
                        finish();
                        break;
                }
            }
        });

        getWelFareData();

        if (!isNotificationEnabled(MainActivity.this)) {
            if (String.valueOf(RxAppTool.getAppVersionCode(MainActivity.this)).equals(RxSPTool.getString(MainActivity.this,Constant.KEY_VERSION))){

            }else {
                RxSPTool.putContent(MainActivity.this,Constant.KEY_VERSION,String.valueOf(RxAppTool.getAppVersionCode(MainActivity.this)));
                showNotificationDialog();
            }
        }
    }

    private void showNotificationDialog() {
        SelectDialog.show(MainActivity.this, "通知未开启", "未开启通知将会接收不到聊天与系统消息", "去开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gotoSet();
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    private boolean isNotificationEnabled(Context context) {
        boolean isOpened = false;
        try {
            isOpened = NotificationManagerCompat.from(context).areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
            isOpened = false;
        }
        return isOpened;

    }

    private void gotoSet() {

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {
            // android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) {
            // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
        } else {
            // 其他
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void getWelFareData() {
        MQApi.getReceiveCoupon(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<HomeWelfareBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<HomeWelfareBean>>() {
                        }.getType());
                if (myApiResult.isOk()) {
                    homeWelfareBean = myApiResult.getData();
                    if (StringUtil.isNotNull(homeWelfareBean.getCoupon())) {
                        showWelfareDialog();
                    }
                }
            }
        });
    }

    private void showWelfareDialog() {
        customDialog = CustomDialog.show(MainActivity.this, R.layout.dialog_home_welfare, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
                TextView textView = rootView.findViewById(R.id.textViewGo);
                ImageView imageViewKuaibi = rootView.findViewById(R.id.imageViewKuaibi);
                ImageView imageViewCoupon = rootView.findViewById(R.id.imageViewCoupon);
                ImageView imageViewMoney = rootView.findViewById(R.id.imageViewMoney);
                TextView textViewKuaibiMoney = rootView.findViewById(R.id.textViewKuaibiMoney);
                TextView textViewCouponMoney = rootView.findViewById(R.id.textViewCouponMoney);
                TextView textViewMoneyNumb = rootView.findViewById(R.id.textViewMoneyNumb);
                if ("0".equals(homeWelfareBean.getHuanbi())) {
                    imageViewKuaibi.setImageResource(R.mipmap.ic_welfare_kuaibi_unhave);
                    textViewKuaibiMoney.setText("0个");
                } else {
                    imageViewKuaibi.setImageResource(R.mipmap.ic_welfare_kuaibi_have);
                    textViewKuaibiMoney.setText(homeWelfareBean.getHuanbi() + "个");
                }

                if ("0".equals(homeWelfareBean.getCoupon())) {
                    imageViewCoupon.setImageResource(R.mipmap.ic_welfare_coupon_unhave);
                    textViewCouponMoney.setText("0元");
                } else {
                    imageViewCoupon.setImageResource(R.mipmap.ic_welfare_coupon_have);
                    textViewCouponMoney.setText(homeWelfareBean.getCoupon() + "元");
                }

                if ("0".equals(homeWelfareBean.getHongbao())) {
                    imageViewMoney.setImageResource(R.mipmap.ic_welfare_money_unhave);
                    textViewMoneyNumb.setText("0元");
                } else {
                    imageViewMoney.setImageResource(R.mipmap.ic_welfare_money_have);
                    textViewMoneyNumb.setText(homeWelfareBean.getHongbao() + "元");
                }

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WelfareCenterActivity.start(MainActivity.this);
                        customDialog.doDismiss();
                    }
                });
            }
        }).setCanCancel(true);
    }


    private void showYuYueDialog() {
        customDialog = CustomDialog.show(MainActivity.this, R.layout.dialog_add_reserve, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
//                        //绑定布局
//                        ImageView btnOk = rootView.findViewById(R.id.btn_ok);
                TextView tv_one = rootView.findViewById(R.id.tv_one);
                TextView tv_two = rootView.findViewById(R.id.tv_two);
                TextView tv_cancel = rootView.findViewById(R.id.tv_cancel);
                tv_one.setText("电话预定");
                tv_two.setText("填写需求预定");
                tv_one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                        getPhone();
                    }
                });
                tv_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                        getUserCoupon();
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                    }
                });
            }
        }).setCanCancel(true);
    }

    private void getPhone() {
        showWaitDialog(MainActivity.this);
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
                    addUserOrder();
                    RxDeviceTool.dial(MainActivity.this, myApiResult.getData().getCONSUMER_HOTLINE());
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void addUserOrder() {
        MQApi.addUserOrder(userBean.getUId(), 2, "", 2, "", "", "",
                "", "", "", "", "", "", "", "", "",
                new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {

                    }
                });
    }

    List<CouponBean> couponDatas = new ArrayList<>();

    private void getUserCoupon() {
        showWaitDialog(MainActivity.this);
        MQApi.getUserCashCoupon(userBean.getUId(), "", "", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<CouponBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<CouponBean>>>() {
                        }.getType());
                if (myApiResult.isOk()) {
                    if (myApiResult.getData().size() == 0) {
                        AppointmentInfoActivity.start(MainActivity.this, 3, "", "", ""
                                , "", "", "", "", "");
                    } else {
                        for (int i = 0; i < myApiResult.getData().size(); i++) {
                            couponDatas.add(myApiResult.getData().get(i));
                        }
                        showCouponDialog();
                    }
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void showCouponDialog() {
        customDialog = CustomDialog.show(MainActivity.this, R.layout.dialog_whether_coupon, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
//                        //绑定布局
//                        ImageView btnOk = rootView.findViewById(R.id.btn_ok);
                TextView tv_money = rootView.findViewById(R.id.tv_all_money);
                TextView tv_sure = rootView.findViewById(R.id.tv_sure);
                TextView tv_cancel = rootView.findViewById(R.id.tv_cancel);
                tv_money.setText("当前代金券累计金额为" + couponDatas.get(0).getAmountTotal());
                tv_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                        UseCouponActivity.start(mContext, 3, "", "", "", "");
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                        AppointmentInfoActivity.start(mContext, 3, "", "", "", "", "", "", "", "");
                    }
                });
            }
        }).setCanCancel(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        fragmentManager = getSupportFragmentManager();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState != null) {
            //读取上一次界面Save的时候tab选中的状态
            selindex = savedInstanceState.getInt(PRV_SELINDEX, selindex);
            homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[0]);
            findFragment = (FindFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[1]);
            messageFragment = (MessageFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[2]);
            userFragment = (UserFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[3]);
        }
        mImmersionBar.transparentStatusBar().init();
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(TaskMessage message) {
        if (message.getMessage() == 1) {
            navigation.setSelectedItemId(R.id.navigation_find);
        } else if (message.getMessage() == 2) {
            navigation.setSelectedItemId(R.id.navigation_message);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(homeEndMessage message) {
        showNewbieGuide();
    }

    private void showNewbieGuide() {
        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(600);
        enterAnimation.setFillAfter(true);

        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(600);
        exitAnimation.setFillAfter(true);

        NewbieGuide.with(MainActivity.this)
                .setLabel("listener")
                .alwaysShow(true)//总是显示，调试时可以打开
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        userBean.setIsFirstLanding("1");
                        updateGuide();
                    }
                })
                .addGuidePage(
                        GuidePage.newInstance()
                                .addHighLight(fab, HighLight.Shape.CIRCLE, 20, 5,
                                        null)
//                                        new RelativeGuide(R.layout.view_guide_tab,Gravity.TOP,50))
                                .setLayoutRes(R.layout.view_guide_tab)
                        .setExitAnimation(exitAnimation)
                )
                .show();

    }

    private void updateGuide() {
        MQApi.updatelsFirstLanding(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(String s) {
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                        }.getType());
                if (myApiResult.isOk()) {
                    userBean.setIsFirstLanding("1");
                    UserCache.getInstance().update(userBean);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(iUnReadMessageObserver);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //保存tab选中的状态
        outState.putInt(PRV_SELINDEX, selindex);
        super.onSaveInstanceState(outState);
    }

    public static void start(Context context) {
//        Intent starter = new Intent(context, MainActivity.class);
//        context.startActivity(starter);
        RxActivityTool.skipActivityAndFinishAll(context, MainActivity.class);
    }

    /**
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */
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
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    LogUtil.e("--onSuccess" + userid);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtil.e("--onError" + errorCode);
                }
            });
        }
    }

    private final int USER_TASK_NUMB = 1992;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
//                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
//                    upDataUserHead();
                    EventBus.getDefault().post(new updateHeadMessage(PictureSelector.obtainMultipleResult(data).get(0).getCutPath()));
                    break;
                case USER_TASK_NUMB:
                    int type = data.getIntExtra("numb", 0);
                    if (type == 1) {
                        navigation.setSelectedItemId(R.id.navigation_find);
                    } else if (type == 2) {
                        navigation.setSelectedItemId(R.id.navigation_message);
                    } else if (type == 0) {
                        navigation.setSelectedItemId(R.id.navigation_home);
                    }
                    break;
            }
        }
    }

    UserInfo userInfo;

    private UserInfo findUserById(String userId) {
        MQApi.getUserBaseInfo(userId, "", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<UserBaseBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<UserBaseBean>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<UserBaseBean>>(){});
                if (myApiResult.isOk()) {
                    userInfo = new UserInfo(userId, myApiResult.getData().getNickName(), Uri.parse(myApiResult.getData().getFace()));
                    RongIM.getInstance().refreshUserInfoCache(userInfo);
                }
            }
        });
        return userInfo;
    }

    private long exitTime = 0;

    //退出按钮监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
//            WebViewActivity.start(MainActivity.this,"活动","http://192.168.1.104:8084/rapidMeet/lottery/apply.html?aId=1&uId=20005614");
//            WebViewActivity.start(MainActivity.this,"活动","http://192.168.1.104:8084/rapidMeet/lottery/apply.html?aId=1&uId=" + userBean.getUId());
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            RxToast.showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
//            RxActivityTool.AppExit(MainActivity.mContext);
            moveTaskToBack(true);
        }
    }

}

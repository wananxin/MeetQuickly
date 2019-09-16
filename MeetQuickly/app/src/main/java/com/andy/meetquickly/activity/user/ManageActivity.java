package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.user.manager.AddMeetPeopleActivity;
import com.andy.meetquickly.activity.user.manager.ManageOrderActivity;
import com.andy.meetquickly.activity.user.manager.ManageUserActivity;
import com.andy.meetquickly.activity.user.manager.UnpaidOrderActivity;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.homeEndMessage;
import com.andy.meetquickly.utils.StringUtil;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.app.hubert.guide.model.RelativeGuide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageActivity extends BaseActivity {

    @BindView(R.id.rl_wdyy)
    RelativeLayout rlWdyy;
    @BindView(R.id.rl_yqzd)
    RelativeLayout rlYqzd;
    @BindView(R.id.rl_glwd)
    RelativeLayout rlGlwd;
    @BindView(R.id.rl_wdpl)
    RelativeLayout rlWdpl;
    @BindView(R.id.rl_fbhd)
    RelativeLayout rlFbhd;
    @BindView(R.id.rl_wdjd)
    RelativeLayout rlWdjd;
    @BindView(R.id.rl_wzfdd)
    RelativeLayout rlWzfdd;
    @BindView(R.id.rl_tjyh)
    RelativeLayout rlTjyh;
    private UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_manage;
    }

    @Override
    public void initView() {
        setBarTitle("经理人");
        userBean = UserCache.getInstance().getUser();
        if (!"1".equals(userBean.getIsFirstLandingManager())) {
            showNewbieGuide();
        }
    }

    @Override
    public void initData() {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ManageActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void showNewbieGuide() {
        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(600);
        enterAnimation.setFillAfter(true);

        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(600);
        exitAnimation.setFillAfter(true);

        NewbieGuide.with(ManageActivity.this)
                .setLabel("listener")
                .alwaysShow(true)//总是显示，调试时可以打开
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        userBean.setIsFirstLandingManager("1");
                        updateGuide();
                    }
                })
                .addGuidePage(
                        GuidePage.newInstance()
                                .addHighLight(rlWdyy, HighLight.Shape.ROUND_RECTANGLE, 20, 5,
                                        new RelativeGuide(R.layout.view_guide_wdyh, Gravity.BOTTOM, 50))
//                                .setLayoutRes(R.layout.view_guide_simple)
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        TextView tv = view.findViewById(R.id.textViewContent);
                                        tv.setText("这里全部是绑定在你网店里面的美女用户");
                                    }
                                })
                                .setEnterAnimation(enterAnimation)
                )
                .addGuidePage(
                        GuidePage
                                .newInstance()
                                .addHighLight(rlTjyh, HighLight.Shape.ROUND_RECTANGLE, 20, 5,
                                        new RelativeGuide(R.layout.view_guide_tjyh, Gravity.BOTTOM, 50))
//                                .setLayoutRes(R.layout.view_guide_simple)
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        TextView tv = view.findViewById(R.id.textViewContent);
                                        tv.setText("这里可以自行添加用户，显示在你的网店可能邂逅的人当中");
                                    }
                                })
                )
                .addGuidePage(
                        GuidePage
                                .newInstance()
                                .addHighLight(rlYqzd, HighLight.Shape.ROUND_RECTANGLE, 20, 5,
                                        new RelativeGuide(R.layout.view_guide_yqzd, Gravity.BOTTOM, 50))
//                                .setLayoutRes(R.layout.view_guide_simple)
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        TextView tv = view.findViewById(R.id.textViewContent);
                                        tv.setText("输入对方YP号，对方同意后也可显示在你的网店当中");
                                    }
                                })
                )
                .addGuidePage(
                        GuidePage
                                .newInstance()
                                .addHighLight(rlGlwd, HighLight.Shape.ROUND_RECTANGLE, 20, 5,
                                        new RelativeGuide(R.layout.view_guide_glwd, Gravity.BOTTOM, 50))
//                                .setLayoutRes(R.layout.view_guide_simple)
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        TextView tv = view.findViewById(R.id.textViewContent);
                                        tv.setText("丰富你的网店信息，可以提高你的订台率呢");
                                    }
                                })
                )
                .addGuidePage(
                        GuidePage
                                .newInstance()
                                .addHighLight(rlFbhd, HighLight.Shape.ROUND_RECTANGLE, 20, 5,
                                        new RelativeGuide(R.layout.view_guide_fbhd, Gravity.TOP, 50))
//                                .setLayoutRes(R.layout.view_guide_simple)
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        TextView tv = view.findViewById(R.id.textViewContent);
                                        tv.setText("场所如有优惠力度较大的活动，在此处进行编辑");
                                    }
                                })
                )
                .addGuidePage(
                        GuidePage
                                .newInstance()
                                .addHighLight(rlWdjd, HighLight.Shape.ROUND_RECTANGLE, 20, 5,
                                        new RelativeGuide(R.layout.view_guide_wdjd, Gravity.TOP, 50))
//                                .setLayoutRes(R.layout.view_guide_simple)
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        TextView tv = view.findViewById(R.id.textViewContent);
                                        tv.setText("您有新的订单时，在此处进行查看");
                                    }
                                })
                )
                .addGuidePage(
                        GuidePage
                                .newInstance()
                                .addHighLight(rlWzfdd, HighLight.Shape.ROUND_RECTANGLE, 20, 5,
                                        new RelativeGuide(R.layout.view_guide_wzfdd, Gravity.TOP, 50))
//                                .setLayoutRes(R.layout.view_guide_simple)
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        TextView tv = view.findViewById(R.id.textViewContent);
                                        tv.setText("这里是您未向平台支付过的订单，以免影响您的接单率。");
                                    }
                                })
                                .setExitAnimation(exitAnimation)
                )
                .show();

    }

    CustomDialog customDialog;

    @OnClick({R.id.rl_wdyy, R.id.rl_yqzd, R.id.rl_glwd, R.id.rl_wdpl, R.id.rl_tjyh, R.id.rl_fbhd, R.id.rl_wdjd, R.id.rl_wzfdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_wdyy:
                ManageUserActivity.start(mContext);
                //我的用户
                break;
            case R.id.rl_tjyh:
                AddMeetPeopleActivity.start(mContext);
                //添加用户
                break;
            case R.id.rl_yqzd:
                customDialog = CustomDialog.show(ManageActivity.this, R.layout.dialog_manage_yqzd, new CustomDialog.BindView() {
                    @Override
                    public void onBind(CustomDialog dialog, View rootView) {
//                        //绑定布局
//                        ImageView btnOk = rootView.findViewById(R.id.btn_ok);
                        EditText et_content = rootView.findViewById(R.id.et_content);
                        TextView tv_cancel = rootView.findViewById(R.id.tv_cancel);
                        TextView tv_sure = rootView.findViewById(R.id.tv_sure);
                        tv_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.doDismiss();
                            }
                        });
                        tv_sure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bindUser(et_content.getText().toString().trim());
                                customDialog.doDismiss();
                            }
                        });
                    }
                }).setCanCancel(true);
                break;
            case R.id.rl_glwd:
                //管理网店
                ManageShopActivity.start(mContext);
                break;
            case R.id.rl_wdpl:
                //网店评论
                break;
            case R.id.rl_fbhd:
                //发布活动
                PublishEventActivity.start(mContext);
                break;
            case R.id.rl_wdjd:
                //我的接单
                ManageOrderActivity.start(mContext);
                break;
            case R.id.rl_wzfdd:
                //未支付订单
                UnpaidOrderActivity.start(mContext);
                break;
        }
    }

    private void updateGuide() {
        MQApi.updateManagerIsFirstLanding(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(String s) {
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                        }.getType());
                if (myApiResult.isOk()) {
                    userBean.setIsFirstLandingManager("1");
                    UserCache.getInstance().update(userBean);
                }
            }
        });
    }

    private void bindUser(String trim) {
        if (!StringUtil.isNotNull(trim)) {
            RxToast.showToast("Yp号不能为空");
            return;
        }

        showWaitDialog(ManageActivity.this);
        MQApi.addBinding(userBean.getUId(), 1, trim, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                        }.getType());
//                        JSON.parseObject(s,new MyApiResult<String>().getClass());
                if (myApiResult.isOk()) {
                    RxToast.showToast("绑定成功!");
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }
}

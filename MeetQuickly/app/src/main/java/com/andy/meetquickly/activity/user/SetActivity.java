package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.login.LoginActivity;
import com.andy.meetquickly.activity.user.set.ShielMomentActivity;
import com.andy.meetquickly.activity.user.set.ShieldPhoneActivity;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.LogUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/25  11:43
 * 描述：     设置界面
 */
public class SetActivity extends BaseActivity {

    @BindView(R.id.ll_pbsz)
    LinearLayout llPbsz;
    @BindView(R.id.ll_pbdhm)
    LinearLayout llPbdhm;
    @BindView(R.id.ll_pbddt)
    LinearLayout llPbddt;
    @BindView(R.id.switch_kqsp)
    Switch switchKqsp;
    @BindView(R.id.switch_jy)
    Switch switchJy;
    @BindView(R.id.ll_qcltjl)
    LinearLayout llQcltjl;
    @BindView(R.id.ll_sybz)
    LinearLayout llSybz;
    @BindView(R.id.ll_jcxbb)
    LinearLayout llJcxbb;
    @BindView(R.id.ll_about)
    LinearLayout llAbout;
    @BindView(R.id.tv_login_out)
    TextView tvLoginOut;
    @BindView(R.id.ll_zhyaq)
    LinearLayout llZhyaq;
    UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    public void initView() {
        setBarTitle("设置");
        userBean = UserCache.getInstance().getUser();
        if ("0".equals(userBean.getGesturesStatus())) {
            switchKqsp.setChecked(false);
        } else {
            switchKqsp.setChecked(true);
        }

        switchKqsp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && (!StringUtil.isNotNull(userBean.getGesturesPassword()))) {
                    GestureActivity.start(mContext, 1, "设置手势密码", "绘制解锁图案", "");
                } else {
                    openOrCloseGusture(isChecked);
                }
            }
        });
    }

    private void openOrCloseGusture(boolean isChecked) {
        showWaitDialog(SetActivity.this);
        MQApi.openOrCloseGesturePassword(userBean.getUId(), isChecked ? 1 : 0, new SimpleCallBack<String>() {
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
                if (myApiResult.isOk()) {
                    RxToast.showToast("设置成功");
                    if (isChecked) {
                        userBean.setGesturesStatus("1");
                    } else {
                        userBean.setGesturesStatus("0");
                    }
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SetActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_pbsz, R.id.ll_pbdhm, R.id.ll_pbddt, R.id.ll_qcltjl, R.id.ll_sybz, R.id.ll_jcxbb, R.id.ll_about, R.id.tv_login_out, R.id.ll_zhyaq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_pbsz:
                //屏蔽设置
                showEditDialog();
                break;
            case R.id.ll_pbdhm:
                //屏蔽的号码
                ShieldPhoneActivity.start(mContext);
                break;
            case R.id.ll_pbddt:
                //屏蔽的动态
                ShielMomentActivity.start(mContext);
                break;
            case R.id.ll_qcltjl:
                //清除聊天记录
//                Toast.makeText(mContext,"清除聊天记录",Toast.LENGTH_SHORT);
                clearMessage();
                break;
            case R.id.ll_sybz:
                //使用帮助
                Toast.makeText(mContext, "使用帮助", Toast.LENGTH_SHORT);
                break;
            case R.id.ll_jcxbb:
                //检查新版本
                Beta.checkUpgrade(true, false);
                break;
            case R.id.ll_about:
                //关于我们
                AboutActivity.start(mContext);
                break;
            case R.id.tv_login_out:
                //退出登录
                UserCache.getInstance().clear();
                RongIM.getInstance().logout();
                LoginActivity.start(mContext);
                break;
            case R.id.ll_zhyaq:
                SafeActivity.start(mContext);
                break;
        }
    }

    private void clearMessage() {
        //清空单聊的聊天记录
//        RongIMClient.getInstance().clearConversations(new RongIMClient.ResultCallback() {
//            @Override
//            public void onSuccess(Object o) {
//
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode errorCode) {
//
//            }
//        },Conversation.ConversationType.PRIVATE);
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (null != conversations) {
                    for (int i = 0; i < conversations.size(); i++) {
                        RongIM.getInstance().clearMessages(Conversation.ConversationType.PRIVATE, conversations.get(i).getTargetId());
                        RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, conversations.get(i).getTargetId()
                                , new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {

                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                });
                    }
                }
                RxToast.showToast("清空成功");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    CustomDialog customDialog;

    private void showEditDialog() {
        customDialog = CustomDialog.show(SetActivity.this, R.layout.dialog_edit_info, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
//                        //绑定布局
//                        ImageView btnOk = rootView.findViewById(R.id.btn_ok);
                EditText et_one = rootView.findViewById(R.id.et_one);
                EditText et_two = rootView.findViewById(R.id.et_two);
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
                        if (!StringUtil.isNotNull(et_one.getText().toString().trim())) {
                            RxToast.showToast("请输入所屏蔽号码人姓名");
                            return;
                        }
                        if (!StringUtil.isNotNull(et_two.getText().toString().trim())) {
                            RxToast.showToast("请输入所屏蔽号码人手机号");
                            return;
                        }
                        if (!(et_two.getText().toString().trim().length() == 11)) {
                            RxToast.showToast("请输入正确所屏蔽号码人手机号");
                            return;
                        }
                        screenUser(et_one.getText().toString().trim(), et_two.getText().toString().trim());
                    }
                });
            }
        }).setCanCancel(true);
    }

    private void screenUser(String name, String phone) {
        String screen = "[{\"name\":\"" + name + "\",\"phone\":\"" + phone + "\"}]";
        showWaitDialog(SetActivity.this);
        MQApi.addUsersShieldList(userBean.getUId(), screen, new SimpleCallBack<String>() {
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
//                        JSON.parseObject(s,new MyApiResult<UserBean>().getClass());
                if (myApiResult.isOk()) {
                    customDialog.doDismiss();
                    RxToast.showToast("屏蔽成功");
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }
}

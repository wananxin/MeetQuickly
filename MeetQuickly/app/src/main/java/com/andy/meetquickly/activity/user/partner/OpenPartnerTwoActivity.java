package com.andy.meetquickly.activity.user.partner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.CommentAgreementActivity;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.ShareInfoBean;
import com.andy.meetquickly.bean.UserBaseBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.WallteBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.fragment.UserFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.view.FlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OpenPartnerTwoActivity extends BaseActivity {

    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;
    @BindView(R.id.tv_equity_introduce)
    TextView tvEquityIntroduce;
    @BindView(R.id.textViewYaoQing)
    TextView textViewYaoQing;

    private UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_open_partner_two;
    }

    @Override
    public void initView() {
        setBarTitle("开通合伙人");
        userBean = UserCache.getInstance().getUser();
        if ("0".equals(userBean.getIsOpenPartner())) {
            textViewYaoQing.setText("邀请好友");
        } else {
            textViewYaoQing.setText("开通合伙人");
        }
        initData();

    }

    @Override
    public void initData() {
        showWaitDialog(OpenPartnerTwoActivity.this);
        MQApi.getUserInviteTenPerson(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<UserBaseBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<UserBaseBean>>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<WithProfitBean>>>(){});
                if (myApiResult.isOk()) {
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        approveList.add(myApiResult.getData().get(i));
                    }
                    flowLayout.setUrls(approveList);
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, OpenPartnerTwoActivity.class);
        context.startActivity(starter);
    }

    private List<UserBaseBean> approveList = new ArrayList<>();

    @OnClick({R.id.tv_equity_introduce, R.id.textViewYaoQing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_equity_introduce:
                CommentAgreementActivity.start(mContext, "合伙人权益介绍", Constant.PARTNER_EQUITY_AGREEMENT);
                break;
            case R.id.textViewYaoQing:
                if ("0".equals(userBean.getIsOpenPartner())) {
                    showShareDialog();
                } else if ("1".equals(userBean.getIsOpenPartner())) {
                    openPatrer();
                }
                break;
        }
    }

    private void openPatrer() {
        showWaitDialog(OpenPartnerTwoActivity.this);
        MQApi.userOpenPartner(userBean.getUId(), new SimpleCallBack<String>() {
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
                    RxToast.showToast("开通成功");
                    PartnerActivity.start(mContext);
                    finish();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    CustomDialog customDialog;

    private void showShareDialog() {
        customDialog = CustomDialog.show(OpenPartnerTwoActivity.this, R.layout.dialog_shard, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
//                        //绑定布局
//                        ImageView btnOk = rootView.findViewById(R.id.btn_ok);
                TextView textViewFriend = rootView.findViewById(R.id.tv_share_friend);
                TextView textViewMoment = rootView.findViewById(R.id.tv_share_moment);
                ImageView imageViewFriend = rootView.findViewById(R.id.iv_share_friend);
                ImageView imageViewMoment = rootView.findViewById(R.id.iv_share_moment);
                TextView tv_cancel = rootView.findViewById(R.id.tv_cancel);
                textViewFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
//                        share(1);
                        getShareInfo(1);
                    }
                });
                textViewMoment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
//                        share(2);
                        getShareInfo(2);
                    }
                });
                imageViewFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
//                        share(1);
                        getShareInfo(1);
                    }
                });
                imageViewMoment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
//                        share(2);
                        getShareInfo(2);
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

    private void getShareInfo(int type) {
        showWaitDialog(OpenPartnerTwoActivity.this);
        MQApi.userShare(userBean.getUId(), 2, "", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<ShareInfoBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<ShareInfoBean>>() {
                }.getType());
                if (myApiResult.isOk()) {
                    share(type, myApiResult.getData());
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private UMImage thumb;
    private UMWeb web;

    private void share(int type, ShareInfoBean shareInfoBean) {
        if (thumb == null) {
            thumb = new UMImage(OpenPartnerTwoActivity.this, R.mipmap.ic_shard_logo);
        }
        if (web == null) {
            web = new UMWeb(shareInfoBean.getShareUrl());
            web.setTitle(shareInfoBean.getTitle());//标题
            web.setThumb(thumb);  //缩略图
            web.setDescription(shareInfoBean.getDetail());//描述
        }
        if (type == 1) {
            new ShareAction(OpenPartnerTwoActivity.this)
                    .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                    .setCallback(shareListener)//回调监听器
                    .withMedia(web)
                    .share();
        } else {
            new ShareAction(OpenPartnerTwoActivity.this)
                    .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                    .setCallback(shareListener)//回调监听器
                    .withMedia(web)
                    .share();
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            RxToast.showToast("成功了");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            RxToast.showToast("失败了");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            RxToast.showToast("取消了");
        }
    };
}

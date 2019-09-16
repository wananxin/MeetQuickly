package com.andy.meetquickly.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.ShopDetailActivity;
import com.andy.meetquickly.activity.user.CouponActivity;
import com.andy.meetquickly.activity.user.EventCenterActivity;
import com.andy.meetquickly.activity.user.ManageActivity;
import com.andy.meetquickly.activity.user.OpenPartnerActivity;
import com.andy.meetquickly.activity.user.OpenShopActivity;
import com.andy.meetquickly.activity.user.OrderActivity;
import com.andy.meetquickly.activity.user.SetActivity;
import com.andy.meetquickly.activity.user.TaskActivity;
import com.andy.meetquickly.activity.user.UpdataUserAllInfoActivity;
import com.andy.meetquickly.activity.user.UserInvitationPeopleActivity;
import com.andy.meetquickly.activity.user.WelfareCenterActivity;
import com.andy.meetquickly.activity.user.partner.OpenPartnerTwoActivity;
import com.andy.meetquickly.activity.user.partner.PartnerActivity;
import com.andy.meetquickly.activity.user.wallte.WallteActivity;
import com.andy.meetquickly.adapter.InvitationPeopleAdapter;
import com.andy.meetquickly.base.BaseFragment;
import com.andy.meetquickly.bean.ShareInfoBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserInviteTotalBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.updateHeadMessage;
import com.andy.meetquickly.utils.CommentUtil;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.kongzue.dialog.v2.SelectDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/19  15:29
 * 描述：     我的
 */
public class UserFragment extends BaseFragment {
    @BindView(R.id.iv_set)
    ImageView ivSet;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.ll_wallet)
    LinearLayout llWallet;
    @BindView(R.id.ll_order)
    LinearLayout llOrder;
    @BindView(R.id.ll_extend)
    LinearLayout llExtend;
    @BindView(R.id.ll_coupon)
    LinearLayout llCoupon;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    @BindView(R.id.tv_yp_numb)
    TextView tvYpNumb;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    @BindView(R.id.iv_task)
    ImageView ivTask;
    @BindView(R.id.rl_task)
    RelativeLayout rlTask;
    @BindView(R.id.iv_activity)
    ImageView ivActivity;
    @BindView(R.id.rl_activity)
    RelativeLayout rlActivity;
    @BindView(R.id.iv_authentication)
    ImageView ivAuthentication;
    @BindView(R.id.rl_authentication)
    RelativeLayout rlAuthentication;
    @BindView(R.id.iv_agent)
    ImageView ivAgent;
    @BindView(R.id.rl_open_shop)
    RelativeLayout rlAgent;
    @BindView(R.id.iv_manager)
    ImageView ivManager;
    @BindView(R.id.rl_manager)
    RelativeLayout rlManager;
    Unbinder unbinder;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.iv_user_head)
    ImageView ivUserHead;
    private UserBean userBean;
    private View rootView;
    private List<LocalMedia> selectList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    protected void initView() {
        userBean = UserCache.getInstance().getUser();
        if (null != userBean) {
            ImageLoaderUtil.getInstance().loadCircleImage(getContext(), userBean.getFace(), ivUserHead);
            tvName.setText(userBean.getNickName());
            tvLevel.setText("LV " + userBean.getLevel());
            tvYpNumb.setText("YP：" + userBean.getUId());
            userRole();
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.iv_set, R.id.rl_welfare, R.id.iv_scan, R.id.iv_user_head, R.id.ll_wallet, R.id.ll_order, R.id.ll_extend, R.id.ll_coupon, R.id.iv_qr_code, R.id.ll_edit, R.id.rl_task, R.id.rl_activity, R.id.rl_authentication, R.id.rl_open_shop, R.id.rl_manager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_user_head:
                selectPic();
                break;
            case R.id.rl_welfare:
                WelfareCenterActivity.start(UserFragment.this.getContext());
                break;
            case R.id.iv_set:
                //设置
                SetActivity.start(UserFragment.this.getActivity());
                break;
            case R.id.iv_scan:
                //扫描二维码
                Toast.makeText(UserFragment.this.getActivity(), "扫描二维码", Toast.LENGTH_SHORT);
                break;
            case R.id.ll_wallet:
                //钱包
                WallteActivity.start(UserFragment.this.getActivity());
                break;
            case R.id.ll_order:
                //订单
                OrderActivity.start(UserFragment.this.getActivity());
                break;
            case R.id.ll_extend:
                //合伙人
                if ("2".equals(userBean.getIsOpenPartner())) {
//                    OpenPartnerActivity.start(UserFragment.this.getActivity());
                    PartnerActivity.start(UserFragment.this.getActivity());
                }else {
                    OpenPartnerTwoActivity.start(UserFragment.this.getActivity());
                }
                break;
            case R.id.ll_coupon:
                //优惠券
                CouponActivity.start(UserFragment.this.getActivity());
                break;
            case R.id.iv_qr_code:
                //二维码
                getUserInviteTotal();
                break;
            case R.id.ll_edit:
                //编辑
                UpdataUserAllInfoActivity.start(UserFragment.this.getContext());
                break;
            case R.id.rl_task:
                //任务
                TaskActivity.start(UserFragment.this.getActivity());
                break;
            case R.id.rl_activity:
                //活动
                EventCenterActivity.start(UserFragment.this.getActivity());
                break;
            case R.id.rl_authentication:
                //认证
                break;
            case R.id.rl_open_shop:
                //代理人
                OpenShopActivity.start(UserFragment.this.getContext());
                break;
            case R.id.rl_manager:
                //经纪人
                ManageActivity.start(UserFragment.this.getActivity());
                break;
        }
    }

    private void userRole(){
        if ("2".equals(userBean.getUserRole())){
            rlManager.setVisibility(View.VISIBLE);
        }else {
            rlManager.setVisibility(View.GONE);
        }
    }

    private void getUserInviteTotal() {
        showWaitDialog(UserFragment.this.getActivity());
        MQApi.getUserInviteTotal(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<UserInviteTotalBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<UserInviteTotalBean>>(){}.getType());
                if (myApiResult.isOk()){
                    showQrCodeDialog(myApiResult.getData().getGroupCount());
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    CustomDialog customDialog;

    private void showQrCodeDialog(String numb) {
        customDialog = CustomDialog.show(UserFragment.this.getActivity(), R.layout.dialog_ercode_shard, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View view) {
                rootView = view;
                ImageView iv_head = rootView.findViewById(R.id.iv_user_head);
                ImageView iv_qrcode = rootView.findViewById(R.id.iv_qrcode);
                TextView tv_shard = rootView.findViewById(R.id.tv_shard);
                TextView tv_name = rootView.findViewById(R.id.tv_name);
                TextView tv_save = rootView.findViewById(R.id.tv_save);
                TextView tv_numb = rootView.findViewById(R.id.textViewNumb);
                if ("0".equals(numb)){
                    tv_numb.setText("");
                }else {
                    tv_numb.setText("已邀请(" + numb + ")");
                    tv_numb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserInvitationPeopleActivity.start(UserFragment.this.getContext(),numb);
                        }
                    });
                }
                ImageLoaderUtil.getInstance().loadCircleImage(UserFragment.this.getContext(), userBean.getFace(), iv_head);
                ImageLoaderUtil.getInstance().loadImage(UserFragment.this.getContext(), userBean.getQrCode(), iv_qrcode);
                tv_name.setText(userBean.getNickName());
                tv_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveViewToImg(rootView);
                        customDialog.doDismiss();
                    }
                });

                tv_shard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                        showShareDialog();
                    }
                });
            }
        }).setCanCancel(true);

    }

    private void showShareDialog() {
        customDialog = CustomDialog.show(UserFragment.this.getContext(), R.layout.dialog_shard, new CustomDialog.BindView() {
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

    private void getShareInfo(int type){
        showWaitDialog(UserFragment.this.getActivity());
        MQApi.userShare(userBean.getUId(), 2, "", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<ShareInfoBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<ShareInfoBean>>(){}.getType());
                if (myApiResult.isOk()){
                    share(type,myApiResult.getData());
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }


    private UMImage thumb;
    private UMWeb web;
    private void share(int type,ShareInfoBean shareInfoBean) {
        if (thumb == null) {
            thumb = new UMImage(UserFragment.this.getContext(), R.mipmap.ic_shard_logo);
        }
        if (web == null) {
            web = new UMWeb(shareInfoBean.getShareUrl());
            web.setTitle(shareInfoBean.getTitle());//标题
            web.setThumb(thumb);  //缩略图
            web.setDescription(shareInfoBean.getDetail());//描述
        }
        if (type == 1) {
            new ShareAction(UserFragment.this.getActivity())
                    .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                    .setCallback(shareListener)//回调监听器
                    .withMedia(web)
                    .share();
        } else {
            new ShareAction(UserFragment.this.getActivity())
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

    private void saveViewToImg(View rootView) {
        try {
            if (ActivityCompat.checkSelfPermission(UserFragment.this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(UserFragment.this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                ActivityCompat.requestPermissions(UserFragment.this.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
//                mLDialog.setDialogText("正在保存图片...");
//                mLDialog.show();
                showWaitDialog(UserFragment.this.getActivity(), "正在保存图片...");
                saveMyBitmap(createViewBitmap(rootView));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    //使用IO流将bitmap对象存到本地指定文件夹
    public void saveMyBitmap(final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String filePath = Environment.getExternalStorageDirectory().getPath();
                File file = new File(filePath + "/DCIM/Camera/" + Calendar.getInstance().getTimeInMillis() + ".png");
                try {
                    file.createNewFile();


                    FileOutputStream fOut = null;
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);


                    Message msg = Message.obtain();
                    msg.obj = file.getPath();
                    handler.sendMessage(msg);
                    //Toast.makeText(PayCodeActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //权限申请的回调
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mLDialog.setDialogText("正在保存图片...");
//                    mLDialog.show();
                    showWaitDialog(UserFragment.this.getActivity(), "正在保存图片...");
                    try {
                        saveMyBitmap(createViewBitmap(rootView));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    RxToast.showToast("请先开启读写权限");
                }
                return;
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String picFile = (String) msg.obj;
            String[] split = picFile.split("/");
            String fileName = split[split.length - 1];
            try {
                MediaStore.Images.Media.insertImage(UserFragment.this.getContext().getContentResolver()
                        , picFile, fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            UserFragment.this.getContext().sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                    + picFile)));
            RxToast.showToast("图片保存图库成功");
            dissmissWaitDialog();
        }
    };

    private void selectPic() {
        // 进入相册 以下是例子：不需要的api可以不写

        PictureSelector.create((Activity) UserFragment.this.getContext())
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_white_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(320, 320)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
//                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//                .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
//                .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                .openClickSound(cb_voice.isChecked())// 是否开启点击声音
//                .selectionMedia(selectList)// 是否传入已选图片.................
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(200)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(updateHeadMessage message) {
        upDataUserHead(message.getMessage());
    }

    private void upDataUserHead(String path) {
        showWaitDialog(UserFragment.this.getActivity(), "正在上传头像...");
        File file = new File(path);
        MQApi.updateUserFace(userBean.getUId(), file, "face", new ProgressResponseCallBack() {
            @Override
            public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {

            }
        }, new SimpleCallBack<String>() {
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
//                        JSON.parseObject(s, new TypeReference<MyApiResult<UserBean>>(){});
                if (myApiResult.isOk()) {
                    RxToast.showToast("修改头像成功！");
                    userBean.setFace(myApiResult.getData().getFace());
                    UserCache.getInstance().update(userBean);
                    ImageLoaderUtil.getInstance().loadCircleImage(UserFragment.this.getContext(), userBean.getFace(), ivUserHead);
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshUserData();
    }

    private void refreshUserData() {
        userBean = UserCache.getInstance().getUser();
        if (null != userBean) {
            MQApi.getUserInfoByUid(userBean.getUId(), new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {

                }

                @Override
                public void onSuccess(String s) {
                    MyApiResult<UserBean> myApiResult =
                            new Gson().fromJson(s, new TypeToken<MyApiResult<UserBean>>() {
                            }.getType());
//                                JSON.parseObject(s, new TypeReference<MyApiResult<UserBean>>(){});
                    if (myApiResult.isOk()) {
                        userBean = myApiResult.getData();
                        UserCache.getInstance().update(myApiResult.getData());
                        ImageLoaderUtil.getInstance().loadCircleImage(getContext(), userBean.getFace(), ivUserHead);
                        tvName.setText(userBean.getNickName());
                        tvLevel.setText("LV " + userBean.getLevel());
                        tvYpNumb.setText("YP：" + userBean.getUId());
                        ///////////////////////////////////////
//                        userBean.setUserRole("2");
//                        userBean.setUId("20000241");
//                        userBean.setStoreId("1105");
//                        UserCache.getInstance().update(userBean);
                        ///////////////////////////////////////
                        userRole();
                    }
                }
            });
        }
    }
}

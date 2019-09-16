package com.andy.meetquickly.activity.user;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.find.PublishMomentActivity;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.ShareInfoBean;
import com.andy.meetquickly.bean.TaskBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserInviteTotalBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.fragment.UserFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.TaskMessage;
import com.andy.meetquickly.utils.ImageLoaderUtil;
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

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskActivity extends BaseActivity {

    @BindView(R.id.iv_login)
    ImageView ivLogin;
    @BindView(R.id.textViewLogin)
    TextView textViewLogin;
    @BindView(R.id.textViewLoginInfo)
    TextView textViewLoginInfo;
    @BindView(R.id.textViewLoginEnable)
    TextView textViewLoginEnable;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.textViewShare)
    TextView textViewShare;
    @BindView(R.id.textViewShareInfo)
    TextView textViewShareInfo;
    @BindView(R.id.textViewShareEnable)
    TextView textViewShareEnable;
    @BindView(R.id.iv_moment)
    ImageView ivMoment;
    @BindView(R.id.textViewMoment)
    TextView textViewMoment;
    @BindView(R.id.textViewMomentInfo)
    TextView textViewMomentInfo;
    @BindView(R.id.textViewMomentEnable)
    TextView textViewMomentEnable;
    @BindView(R.id.iv_gift)
    ImageView ivGift;
    @BindView(R.id.textViewGift)
    TextView textViewGift;
    @BindView(R.id.textViewGiftInfo)
    TextView textViewGiftInfo;
    @BindView(R.id.textViewGiftEnable)
    TextView textViewGiftEnable;
    @BindView(R.id.iv_praise)
    ImageView ivPraise;
    @BindView(R.id.textViewPrase)
    TextView textViewPrase;
    @BindView(R.id.textViewPraseInfo)
    TextView textViewPraseInfo;
    @BindView(R.id.textViewPraseEnable)
    TextView textViewPraseEnable;
    @BindView(R.id.iv_phone)
    ImageView ivPhone;
    @BindView(R.id.textViewPhone)
    TextView textViewPhone;
    @BindView(R.id.textViewPhoneInfo)
    TextView textViewPhoneInfo;
    @BindView(R.id.textViewPhoneEnable)
    TextView textViewPhoneEnable;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.textViewHead)
    TextView textViewHead;
    @BindView(R.id.textViewHeadInfo)
    TextView textViewHeadInfo;
    @BindView(R.id.textViewHeadEnable)
    TextView textViewHeadEnable;
    @BindView(R.id.iv_nickname)
    ImageView ivNickname;
    @BindView(R.id.textViewNickName)
    TextView textViewNickName;
    @BindView(R.id.textViewNickNameInfo)
    TextView textViewNickNameInfo;
    @BindView(R.id.textViewNickNameEnable)
    TextView textViewNickNameEnable;
    private UserBean userBean;
    private Intent intent;
    private View rootView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_task;
    }

    @Override
    public void initView() {
        setBarTitle("任务");
        userBean = UserCache.getInstance().getUser();
        intent = new Intent();
        initData();
    }

    @Override
    public void initData() {
        showWaitDialog(TaskActivity.this);
        MQApi.getDailyTasksInfo(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<TaskBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<TaskBean>>() {
                }.getType());
                if (myApiResult.isOk()) {

                    for (int i = 0; i < myApiResult.getData().getPersonalTasks().size(); i++) {
                        if ("face".equals(myApiResult.getData().getPersonalTasks().get(i).getType())){
                            ImageLoaderUtil.getInstance().loadImage(TaskActivity.this, myApiResult.getData().getPersonalTasks().get(i).getUrl(), ivHead);
                            textViewHead.setText(myApiResult.getData().getPersonalTasks().get(i).getName());
                            textViewHeadInfo.setText(myApiResult.getData().getPersonalTasks().get(i).getDepict());
                            if ("1".equals(myApiResult.getData().getPersonalTasks().get(i).getStatus())) {
                                textViewHeadEnable.setBackgroundResource(R.drawable.shape_bt_finishi);
                                textViewHeadEnable.setText("已完成");
                                textViewHeadEnable.setEnabled(false);
                            } else {
                                textViewHeadEnable.setBackgroundResource(R.drawable.shape_bt_unfinishi);
                                textViewHeadEnable.setText("做任务");
                            }
                        } else if ("nick_name".equals(myApiResult.getData().getPersonalTasks().get(i).getType())){
                            ImageLoaderUtil.getInstance().loadImage(TaskActivity.this, myApiResult.getData().getPersonalTasks().get(i).getUrl(), ivNickname);
                            textViewNickName.setText(myApiResult.getData().getPersonalTasks().get(i).getName());
                            textViewNickNameInfo.setText(myApiResult.getData().getPersonalTasks().get(i).getDepict());
                            if ("1".equals(myApiResult.getData().getPersonalTasks().get(i).getStatus())) {
                                textViewNickNameEnable.setBackgroundResource(R.drawable.shape_bt_finishi);
                                textViewNickNameEnable.setText("已完成");
                                textViewNickNameEnable.setEnabled(false);
                            } else {
                                textViewNickNameEnable.setBackgroundResource(R.drawable.shape_bt_unfinishi);
                                textViewNickNameEnable.setText("做任务");
                            }
                        }
                    }
                    for (int i = 0; i < myApiResult.getData().getDailyTasks().size(); i++) {
                        if ("login".equals(myApiResult.getData().getDailyTasks().get(i).getType())) {
                            ImageLoaderUtil.getInstance().loadImage(TaskActivity.this, myApiResult.getData().getDailyTasks().get(i).getUrl(), ivLogin);
                            textViewLogin.setText(myApiResult.getData().getDailyTasks().get(i).getName());
                            textViewLoginInfo.setText(myApiResult.getData().getDailyTasks().get(i).getDepict());
                            if ("1".equals(myApiResult.getData().getDailyTasks().get(i).getStatus())) {
                                textViewLoginEnable.setBackgroundResource(R.drawable.shape_bt_finishi);
                                textViewLoginEnable.setText("已完成");
                                textViewLoginEnable.setEnabled(false);
                            } else {
                                textViewLoginEnable.setBackgroundResource(R.drawable.shape_bt_unfinishi);
                                textViewLoginEnable.setText("做任务");
                            }
                        } else if ("share".equals(myApiResult.getData().getDailyTasks().get(i).getType())) {
                            ImageLoaderUtil.getInstance().loadImage(TaskActivity.this, myApiResult.getData().getDailyTasks().get(i).getUrl(), ivShare);
                            textViewShare.setText(myApiResult.getData().getDailyTasks().get(i).getName());
                            textViewShareInfo.setText(myApiResult.getData().getDailyTasks().get(i).getDepict());
                            if ("1".equals(myApiResult.getData().getDailyTasks().get(i).getStatus())) {
                                textViewShareEnable.setBackgroundResource(R.drawable.shape_bt_finishi);
                                textViewShareEnable.setText("已完成");
                                textViewShareEnable.setEnabled(false);
                            } else {
                                textViewShareEnable.setBackgroundResource(R.drawable.shape_bt_unfinishi);
                                textViewShareEnable.setText("做任务");
                            }
                        } else if ("add_dynamic".equals(myApiResult.getData().getDailyTasks().get(i).getType())) {
                            ImageLoaderUtil.getInstance().loadImage(TaskActivity.this, myApiResult.getData().getDailyTasks().get(i).getUrl(), ivMoment);
                            textViewMoment.setText(myApiResult.getData().getDailyTasks().get(i).getName());
                            textViewMomentInfo.setText(myApiResult.getData().getDailyTasks().get(i).getDepict());
                            if ("1".equals(myApiResult.getData().getDailyTasks().get(i).getStatus())) {
                                textViewMomentEnable.setBackgroundResource(R.drawable.shape_bt_finishi);
                                textViewMomentEnable.setText("已完成");
                                textViewMomentEnable.setEnabled(false);
                            } else {
                                textViewMomentEnable.setBackgroundResource(R.drawable.shape_bt_unfinishi);
                                textViewMomentEnable.setText("做任务");
                            }
                        } else if ("gift".equals(myApiResult.getData().getDailyTasks().get(i).getType())) {
                            ImageLoaderUtil.getInstance().loadImage(TaskActivity.this, myApiResult.getData().getDailyTasks().get(i).getUrl(), ivGift);
                            textViewGift.setText(myApiResult.getData().getDailyTasks().get(i).getName());
                            textViewGiftInfo.setText(myApiResult.getData().getDailyTasks().get(i).getDepict());
                            if ("1".equals(myApiResult.getData().getDailyTasks().get(i).getStatus())) {
                                textViewGiftEnable.setBackgroundResource(R.drawable.shape_bt_finishi);
                                textViewGiftEnable.setText("已完成");
                                textViewGiftEnable.setEnabled(false);
                            } else {
                                textViewGiftEnable.setBackgroundResource(R.drawable.shape_bt_unfinishi);
                                textViewGiftEnable.setText("做任务");
                            }
                        } else if ("like".equals(myApiResult.getData().getDailyTasks().get(i).getType())) {
                            ImageLoaderUtil.getInstance().loadImage(TaskActivity.this, myApiResult.getData().getDailyTasks().get(i).getUrl(), ivPraise);
                            textViewPrase.setText(myApiResult.getData().getDailyTasks().get(i).getName());
                            textViewPraseInfo.setText(myApiResult.getData().getDailyTasks().get(i).getDepict());
                            if ("1".equals(myApiResult.getData().getDailyTasks().get(i).getStatus())) {
                                textViewPraseEnable.setBackgroundResource(R.drawable.shape_bt_finishi);
                                textViewPraseEnable.setText("已完成");
                                textViewPraseEnable.setEnabled(false);
                            } else {
                                textViewPraseEnable.setBackgroundResource(R.drawable.shape_bt_unfinishi);
                                textViewPraseEnable.setText("做任务");
                            }
                        } else if ("imgs_edit".equals(myApiResult.getData().getDailyTasks().get(i).getType())) {
                            ImageLoaderUtil.getInstance().loadImage(TaskActivity.this, myApiResult.getData().getDailyTasks().get(i).getUrl(), ivPhone);
                            textViewPhone.setText(myApiResult.getData().getDailyTasks().get(i).getName());
                            textViewPhoneInfo.setText(myApiResult.getData().getDailyTasks().get(i).getDepict());
                            if ("1".equals(myApiResult.getData().getDailyTasks().get(i).getStatus())) {
                                textViewPhoneEnable.setBackgroundResource(R.drawable.shape_bt_finishi);
                                textViewPhoneEnable.setText("已完成");
                                textViewPhoneEnable.setEnabled(false);
                            } else {
                                textViewPhoneEnable.setBackgroundResource(R.drawable.shape_bt_unfinishi);
                                textViewPhoneEnable.setText("做任务");
                            }
                        }


                    }
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    public static void start(Activity context) {
        Intent starter = new Intent(context, TaskActivity.class);
        context.startActivityForResult(starter,1992);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.textViewLoginEnable, R.id.textViewShareEnable, R.id.textViewMomentEnable, R.id.textViewGiftEnable, R.id.textViewPraseEnable, R.id.textViewPhoneEnable, R.id.textViewHeadEnable, R.id.textViewNickNameEnable})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewLoginEnable:
                break;
            case R.id.textViewShareEnable:
                getUserInviteTotal();
                break;
            case R.id.textViewMomentEnable:
                PublishMomentActivity.start(mContext);
                break;
            case R.id.textViewGiftEnable:
                // 获取用户计算后的结果
                intent.putExtra("numb", 2);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.textViewPraseEnable:
                // 获取用户计算后的结果
                intent.putExtra("numb", 1);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.textViewPhoneEnable:
                UpdataUserAllInfoActivity.start(mContext);
                finish();
                break;
            case R.id.textViewHeadEnable:
                break;
            case R.id.textViewNickNameEnable:
                break;
        }
    }

    private void getUserInviteTotal() {
        showWaitDialog(TaskActivity.this);
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
        customDialog = CustomDialog.show(TaskActivity.this, R.layout.dialog_ercode_shard, new CustomDialog.BindView() {
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
                            UserInvitationPeopleActivity.start(TaskActivity.this,numb);
                        }
                    });
                }
                ImageLoaderUtil.getInstance().loadCircleImage(TaskActivity.this, userBean.getFace(), iv_head);
                ImageLoaderUtil.getInstance().loadImage(TaskActivity.this, userBean.getQrCode(), iv_qrcode);
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
        customDialog = CustomDialog.show(TaskActivity.this, R.layout.dialog_shard, new CustomDialog.BindView() {
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
        showWaitDialog(TaskActivity.this);
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
            thumb = new UMImage(TaskActivity.this, R.mipmap.ic_shard_logo);
        }
        if (web == null) {
            web = new UMWeb(shareInfoBean.getShareUrl());
            web.setTitle(shareInfoBean.getTitle());//标题
            web.setThumb(thumb);  //缩略图
            web.setDescription(shareInfoBean.getDetail());//描述
        }
        if (type == 1) {
            new ShareAction(TaskActivity.this)
                    .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                    .setCallback(shareListener)//回调监听器
                    .withMedia(web)
                    .share();
        } else {
            new ShareAction(TaskActivity.this)
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
            textViewShareEnable.setBackgroundResource(R.drawable.shape_bt_finishi);
            textViewShareEnable.setText("已完成");
            textViewShareEnable.setEnabled(false);
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
            if (ActivityCompat.checkSelfPermission(TaskActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(TaskActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                ActivityCompat.requestPermissions(TaskActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
//                mLDialog.setDialogText("正在保存图片...");
//                mLDialog.show();
                showWaitDialog(TaskActivity.this, "正在保存图片...");
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String picFile = (String) msg.obj;
            String[] split = picFile.split("/");
            String fileName = split[split.length - 1];
            try {
                MediaStore.Images.Media.insertImage(TaskActivity.this.getContentResolver()
                        , picFile, fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            TaskActivity.this.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                    + picFile)));
            RxToast.showToast("图片保存图库成功");
            dissmissWaitDialog();
        }
    };
}

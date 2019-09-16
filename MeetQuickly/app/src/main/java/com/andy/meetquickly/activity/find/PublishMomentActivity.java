package com.andy.meetquickly.activity.find;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.GridImageAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.fragment.HomeFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.KeyboardUtils;
import com.andy.meetquickly.utils.LogUtil;
import com.andy.meetquickly.utils.SoftHideKeyBoardUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.baidu.location.b.f;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.listener.OnMenuItemClickListener;
import com.kongzue.dialog.v2.BottomMenu;
import com.kongzue.dialog.v2.DialogSettings;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxKeyboardTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.wode369.videocroplibrary.features.trim.VideoTrimmerActivity;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

import static com.kongzue.dialog.v2.DialogSettings.STYLE_IOS;

public class PublishMomentActivity extends BaseActivity {

    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.iv_video)
    ImageView ivVideo;
    @BindView(R.id.recycler_view_pic)
    RecyclerView recyclerViewPic;
    @BindView(R.id.iv_video_pic)
    ImageView ivVideoPic;
    @BindView(R.id.cardView_video)
    CardView cardViewVideo;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.textViewNumb)
    TextView textViewNumb;
    private GridImageAdapter adapter;
    private String videoPath;
    private int momentType = 0;
    private UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_publish_moment;
    }

    @Override
    public void initView() {
        setBarTitle("发布动态");
        setRightTextAndClick("发布", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishMoment();
            }
        });
        getPermissions();
        SoftHideKeyBoardUtil.assistActivity(PublishMomentActivity.this);
        userBean = UserCache.getInstance().getUser();
        GridLayoutManager manager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        recyclerViewPic.setLayoutManager(manager);
        adapter = new GridImageAdapter(mContext, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(9);
        recyclerViewPic.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create((Activity) mContext).themeStyle(R.style.picture_white_style).openExternalPreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create((Activity) mContext).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create((Activity) mContext).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textViewNumb.setText(s.toString().length()+"/200");
            }
        });
    }

    private void publishMoment() {
        if ((!StringUtil.isNotNull(etContent.getText().toString()))&&(selectList.size() == 0)&&(!StringUtil.isNotNull(videoPath))){
            RxToast.showToast("请选择要发布的内容");
            return;
        }
        if ((selectList.size() == 0)&&(!StringUtil.isNotNull(videoPath))){
            momentType = 0;
        }

        if (momentType == 1){
            List<File> files = new ArrayList<>();
            for (int i = 0; i < selectList.size(); i++) {
                files.add(new File(selectList.get(i).getCompressPath()));
            }
            showWaitDialog(PublishMomentActivity.this,"正在上传图片...");
            MQApi.addUserDynamicPic(userBean.getUId(), momentType, etContent.getText().toString(),
                    RxSPTool.getContent(PublishMomentActivity.this,Constant.KEY_LONGITUDE),
                    RxSPTool.getContent(PublishMomentActivity.this,Constant.KEY_LATITUDE),
                    RxSPTool.getContent(PublishMomentActivity.this,Constant.KEY_ADDRESS), files, "files", new ProgressResponseCallBack() {
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
                    MyApiResult myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());;
                    if (myApiResult.isOk()){
                        RxToast.showToast("发布成功");
                        finish();
                    }else {
                        RxToast.showToast(myApiResult.getMsg());
                    }
                }
            });
        }else if (momentType == 0){
            showWaitDialog(PublishMomentActivity.this);
            MQApi.addUserDynamicPic(userBean.getUId(), momentType, etContent.getText().toString(),
                    RxSPTool.getContent(PublishMomentActivity.this,Constant.KEY_LONGITUDE),
                    RxSPTool.getContent(PublishMomentActivity.this,Constant.KEY_LATITUDE),
                    RxSPTool.getContent(PublishMomentActivity.this,Constant.KEY_ADDRESS),
                    new ArrayList<>(), "files", new ProgressResponseCallBack() {
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
                    MyApiResult myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());;
                    if (myApiResult.isOk()){
                        RxToast.showToast("发布成功");
                        RxKeyboardTool.hideSoftInput((Activity) mContext);
                        finish();
                    }else {
                        RxToast.showToast(myApiResult.getMsg());
                    }
                }
            });
        }else {
            File file = new File(videoPath);
            showWaitDialog(PublishMomentActivity.this,"正在上传视频...");
            MQApi.addUserDynamicVideo(userBean.getUId(), momentType, etContent.getText().toString(),
                    RxSPTool.getContent(PublishMomentActivity.this,Constant.KEY_LONGITUDE),
                    RxSPTool.getContent(PublishMomentActivity.this,Constant.KEY_LATITUDE),
                    RxSPTool.getContent(PublishMomentActivity.this,Constant.KEY_ADDRESS),
                    file, "files", new ProgressResponseCallBack() {
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
                    MyApiResult myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());;
                    if (myApiResult.isOk()){
                        RxToast.showToast("发布成功");
                        finish();
                    }else {
                        RxToast.showToast(myApiResult.getMsg());
                    }
                }
            });
        }
    }

    private void getPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {

                } else {

                }

            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            selectPic();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initData() {
        selectPic();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PublishMomentActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_pic, R.id.iv_video, R.id.cardView_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pic:
                selectPic();
                break;
            case R.id.iv_video:
                showVideoDialog();
                break;
            case R.id.cardView_video:
                PictureSelector.create((Activity) mContext).externalPictureVideo(videoPath);
                break;
        }
    }

    private void showVideoDialog() {
        List<String> list = new ArrayList<>();
        list.add("拍摄");
        list.add("手机相册选择");
        BottomMenu.show(PublishMomentActivity.this, list, new OnMenuItemClickListener() {
            @Override
            public void onClick(String text, int index) {
                if (index == 0) {
                    //拍摄
                    VideoRecorderActivity.start(PublishMomentActivity.this);
                } else {
                    //选择
                    Intent intent = new Intent(PublishMomentActivity.this, VideoLocalAllActivity.class);
                    startActivityIfNeeded(intent, VIDEO_REQUESTCODE);
                }
            }
        }, true);
    }

    private void selectPic() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(PublishMomentActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_white_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(9)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
//                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//                .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
//                .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                .openClickSound(cb_voice.isChecked())// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片.................
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private void selectVideo() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(PublishMomentActivity.this)
                .openGallery(PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_white_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(0)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .isCamera(false)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
//                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//                .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
//                .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                .openClickSound(cb_voice.isChecked())// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片.................
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                .recordVideoSecond(20)//录制视频秒数 默认60s
                .forResult(PictureConfig.REQUEST_CAMERA);//结果回调onActivityResult code
    }

    private List<LocalMedia> selectList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    if (selectList.size() > 0 ) {
                        momentType = 1;
                        recyclerViewPic.setVisibility(View.VISIBLE);
                        cardViewVideo.setVisibility(View.GONE);
                        adapter.setList(selectList);
                        adapter.notifyDataSetChanged();
                        ivVideo.setImageResource(R.mipmap.ic_video_enable);
                        ivVideo.setEnabled(false);
                    }
                    break;
                case PictureConfig.REQUEST_CAMERA:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("视频-----》", media.getPath());
                    }
                    String path = "/storage/emulated/0/DCIM/Camera/VID_20190419_195621.mp4";
                    LogUtil.e(path);
                    videoCrop(path);
                    break;
                case VIDEO_TRIM_REQUEST_CODE:
                    //剪切回来的文件
                    videoPath = data.getStringExtra("cutPath");
                    if (StringUtil.isNotNull(videoPath)) {
                        momentType = 2;
                        recyclerViewPic.setVisibility(View.GONE);
                        cardViewVideo.setVisibility(View.VISIBLE);
                        ivVideoPic.setImageBitmap(getVideoThumb(videoPath));
                        ivPic.setImageResource(R.mipmap.ic_picture_enable);
                        ivPic.setEnabled(false);
                    }
                    break;
                case VIDEO_MY_REQUEST_CODE:
                    //拍摄返回
                    String pa1 = data.getStringExtra("video_path");
                    videoCrop(pa1);
                    break;
                case VIDEO_REQUESTCODE:
                    //选择返回
                    String pa2 = data.getStringExtra("video_path");
                    videoCrop(pa2);
                    break;
            }
        }
    }

    public static Bitmap getVideoThumb(String path) {

        MediaMetadataRetriever media = new MediaMetadataRetriever();

        media.setDataSource(path);

        return media.getFrameAtTime();

    }


    private final static int VIDEO_REQUESTCODE = 22;
    public static final int VIDEO_TRIM_REQUEST_CODE = 23;
    public static final int VIDEO_MY_REQUEST_CODE = 1992;
    private static final String VIDEO_PATH_KEY = "video-file-path";

    //得到视频路径，new 出视频file，我们就可以对视频进行剪裁处理了（文字，滤镜...）
    //这里推荐一个开源框架，Android-Video-Trimmer： https://github.com/iknow4/Android-Video-Trimmer 还不错。
    // 缺点是：使用ffmpeg进行视频裁剪。会让你的app增大许多,20-30M
    private void videoCrop(String videoPath) {

        if (!TextUtils.isEmpty(videoPath)) {
            Bundle bundle = new Bundle();
            bundle.putString(VIDEO_PATH_KEY, videoPath);

            Intent intent = new Intent(PublishMomentActivity.this, VideoTrimmerActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, VIDEO_TRIM_REQUEST_CODE);
        }


    }
}

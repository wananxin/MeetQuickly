package com.andy.meetquickly.activity.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.find.PublishMomentActivity;
import com.andy.meetquickly.activity.user.manager.StoreEventActivity;
import com.andy.meetquickly.adapter.GridImageAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.LogUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublishEventActivity extends BaseActivity {

    @BindView(R.id.tv_event_numb)
    TextView tvEventNumb;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_content_numb)
    TextView tvContentNumb;
    @BindView(R.id.tv_select_time)
    TextView tvSelectTime;
    @BindView(R.id.recycler_view_pic)
    RecyclerView recyclerViewPic;
    private int maxOrder = 10;
    private SimpleDateFormat dateFormat;


    private GridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_publish_event;
    }

    @Override
    public void initView() {
        setBarTitle("发布活动");
        setRightTextAndClick("历史活动", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreEventActivity.start(mContext);
            }
        });
        userBean = UserCache.getInstance().getUser();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvEventNumb.setText("(" + (progress + 1) + ")");
                maxOrder = progress + 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
                tvContentNumb.setText(s.toString().length() + "/100");
            }
        });

        GridLayoutManager manager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        recyclerViewPic.setLayoutManager(manager);
        adapter = new GridImageAdapter(mContext, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(6);
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
    }

    private void selectPic() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(PublishEventActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_white_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(6)// 最大图片选择数量
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
//                .cropCompressQuality(60)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            selectPic();
        }
    };

    @Override
    public void initData() {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PublishEventActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_select_time, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_select_time:
                //时间选择器
                TimePickerView pvStartTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvSelectTime.setText(dateFormat.format(date));
                    }
                }).setSubmitColor(Color.BLACK)//确定按钮文字颜色
                        .setCancelColor(Color.BLACK)//取消按钮文字颜色
                        .build();

                pvStartTime.show();
                break;
            case R.id.tv_submit:
                submit();
                break;
        }
    }

    private void submit() {
        if (!StringUtil.isNotNull(tvSelectTime.getText().toString())){
            RxToast.showToast("请选择时间");
            return;
        }
        if (!StringUtil.isNotNull(etContent.getText().toString())){
            RxToast.showToast("请填写活动内容");
            return;
        }
        List<File> files = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            files.add(new File(selectList.get(i).getCompressPath()));
            LogUtil.e(files.get(i).length()+"byte");
        }
        showWaitDialog(PublishEventActivity.this);
        MQApi.addStoreActivity(userBean.getStoreId(), files.size()>0?"1":"0", etContent.getText().toString(), tvSelectTime.getText().toString(),
                maxOrder + "", files, "files", new ProgressResponseCallBack() {
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
                        MyApiResult myApiResult =
                                new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());
//                                JSON.parseObject(s,new MyApiResult<String>().getClass());
                        if (myApiResult.isOk()){
                            RxToast.showToast("发布成功");
                            finish();
                        }else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }

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
                    recyclerViewPic.setVisibility(View.VISIBLE);
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
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
                    break;
            }
        }
    }
}

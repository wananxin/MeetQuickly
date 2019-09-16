package com.andy.meetquickly.activity.user.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.OrderDetailBean;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.ImageLoaderUtil;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadBillActivity extends BaseActivity {

    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @BindView(R.id.rl_add_pic)
    RelativeLayout rlAddPic;
    @BindView(R.id.rl_place_holder)
    RelativeLayout rlPlaceHolder;
    @BindView(R.id.tv_order_numb)
    TextView tvOrderNumb;
    private List<LocalMedia> selectList = new ArrayList<>();
    private OrderDetailBean bean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_bill;
    }

    @Override
    public void initView() {
        setBarTitle("上传账单");
        bean = (OrderDetailBean) this.getIntent().getSerializableExtra("bean");
        tvOrderNumb.setText("订单号"+bean.getOrderId());
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

    private void selectPic() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(UploadBillActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_white_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(2)// 最大图片选择数量
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

    @OnClick({R.id.iv1, R.id.iv2, R.id.rl_add_pic, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv1:
                selectList.remove(0);
                updatePic();
                break;
            case R.id.iv2:
                selectList.remove(1);
                updatePic();
                break;
            case R.id.rl_add_pic:
                selectPic();
                break;
            case R.id.tv_submit:
                submitBill();
                break;
        }
    }

    private void submitBill() {
        if (!(selectList.size() == 2)){
            RxToast.showToast("请您上传消费凭证2张");
            return;
        }

        showWaitDialog(UploadBillActivity.this);
        List<File> files = new ArrayList<>();
        files.add(new File(selectList.get(0).getCompressPath()));
        files.add(new File(selectList.get(1).getCompressPath()));
        MQApi.uploadConsumptionBills(bean.getId(), files, "files", new ProgressResponseCallBack() {
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
//                        JSON.parseObject(s,new MyApiResult<String>().getClass());
                if (myApiResult.isOk()){
                    finish();
                    RxToast.showToast("上传成功");
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });

    }

    public void updatePic() {
        if (selectList.size() == 0) {
            rl1.setVisibility(View.GONE);
            rl2.setVisibility(View.GONE);
            rlAddPic.setVisibility(View.VISIBLE);
            rlPlaceHolder.setVisibility(View.VISIBLE);
        } else if (selectList.size() == 1) {
            rl1.setVisibility(View.VISIBLE);
            rl2.setVisibility(View.GONE);
            rlAddPic.setVisibility(View.VISIBLE);
            rlPlaceHolder.setVisibility(View.GONE);
            ImageLoaderUtil.getInstance().loadImage(mContext,selectList.get(0).getCompressPath(),iv1);
        } else {
            rl1.setVisibility(View.VISIBLE);
            rl2.setVisibility(View.VISIBLE);
            rlAddPic.setVisibility(View.GONE);
            rlPlaceHolder.setVisibility(View.GONE);
            ImageLoaderUtil.getInstance().loadImage(mContext,selectList.get(0).getCompressPath(),iv1);
            ImageLoaderUtil.getInstance().loadImage(mContext,selectList.get(1).getCompressPath(),iv2);
        }
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
                    updatePic();
                    break;
            }
        }
    }

    public static void start(Context context, OrderDetailBean bean) {
        Intent starter = new Intent(context, UploadBillActivity.class);
        starter.putExtra("bean", bean);
        context.startActivity(starter);
    }
}

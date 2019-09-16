package com.andy.meetquickly.activity.user;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.CommentAgreementActivity;
import com.andy.meetquickly.adapter.OpenShopGridImageAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.OpenShopBean;
import com.andy.meetquickly.bean.ShopPlaceBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.SoftHideKeyBoardUtil;
import com.andy.meetquickly.utils.StringUtil;
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

public class OpenShopActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_place)
    TextView tvPlace;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.radioButton2)
    RadioButton radioButton2;
    @BindView(R.id.radioButton3)
    RadioButton radioButton3;
    @BindView(R.id.radioButton4)
    RadioButton radioButton4;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_content_numb)
    TextView tvContentNumb;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.textViewXieyi)
    TextView textViewXieyi;
    private ShopPlaceBean placeBean;
    private UserBean userBean;
    private OpenShopGridImageAdapter adapter;
    private String shopType = "1";
    private List<OpenShopBean.StoreImgsBean> selectList = new ArrayList<>();
    private String mId = "";
    private String companyId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_open_shop;
    }

    @Override
    public void initView() {
        setBarTitle("开店");
        userBean = UserCache.getInstance().getUser();
        SoftHideKeyBoardUtil.assistActivity(this);
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
        recyclerView.setLayoutManager(manager);
        adapter = new OpenShopGridImageAdapter(mContext, onAddPicClickListener, onDelPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(6);
        recyclerView.setAdapter(adapter);

        initData();
    }

    private OpenShopGridImageAdapter.onAddPicClickListener onAddPicClickListener = new OpenShopGridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            selectPic();
        }
    };

    private OpenShopGridImageAdapter.onDelPicClickListener onDelPicClickListener = new OpenShopGridImageAdapter.onDelPicClickListener() {
        @Override
        public void onDelPicClick(int position) {
            selectList.remove(position);
            adapter.notifyDataSetChanged();
        }
    };

    private void selectPic() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create((Activity) OpenShopActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_white_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(6 - selectList.size())// 最大图片选择数量
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

    @Override
    public void initData() {
        MQApi.getUserShopInfo(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<OpenShopBean> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<OpenShopBean>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<OpenShopBean>>() {});
                if (myApiResult.isOk()) {
                    if (null != myApiResult.getData()) {
                        mId = myApiResult.getData().getId();
                        companyId = myApiResult.getData().getCompanyId();
                        etName.setText(myApiResult.getData().getStoreName());
                        etPhone.setText(myApiResult.getData().getMobile());
                        tvPlace.setText(myApiResult.getData().getCompanyName());
                        tvAddress.setText(myApiResult.getData().getAddress());
                        if ("1".equals(myApiResult.getData().getKind())) {
                            radioButton1.setChecked(true);
                            shopType = "1";
                        } else if ("2".equals(myApiResult.getData().getKind())) {
                            radioButton2.setChecked(true);
                            shopType = "2";

                        } else if ("3".equals(myApiResult.getData().getKind())) {
                            radioButton3.setChecked(true);
                            shopType = "3";

                        } else if ("4".equals(myApiResult.getData().getKind())) {
                            radioButton4.setChecked(true);
                            shopType = "4";

                        }
                        etContent.setText(myApiResult.getData().getRemark());

                        if (null != myApiResult.getData().getStoreImgs()) {
                            for (int i = 0; i < myApiResult.getData().getStoreImgs().size(); i++) {
                                selectList.add(myApiResult.getData().getStoreImgs().get(i));
                            }
                            adapter.notifyDataSetChanged();
                        }

                        if ("1".equals(myApiResult.getData().getStatus())) {
                            tvSubmit.setEnabled(false);
                            tvSubmit.setText("审核中");
                        }
                    }
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, OpenShopActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.textViewXieyi, R.id.radioButton1, R.id.radioButton2, R.id.radioButton3, R.id.radioButton4, R.id.tv_submit, R.id.tv_place, R.id.tv_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewXieyi:
                CommentAgreementActivity.start(mContext,"快逅开店协议",Constant.SHOP_AGREEMENT);
                break;
            case R.id.radioButton1:
                shopType = "1";
                break;
            case R.id.radioButton2:
                shopType = "2";
                break;
            case R.id.radioButton3:
                shopType = "3";
                break;
            case R.id.radioButton4:
                shopType = "4";
                break;
            case R.id.tv_submit:
                openShop();
                break;
            case R.id.tv_place:
                SelectShopPlaceActivity.start(OpenShopActivity.this);
                break;
            case R.id.tv_address:
                SelectShopPlaceActivity.start(OpenShopActivity.this);
                break;
        }
    }

    private void openShop() {
        String imgs = "";
        if (selectList.size() == 0) {
            RxToast.showToast("请选择店铺图片！");
            return;
        }
        if (!StringUtil.isNotNull(etName.getText().toString())) {
            RxToast.showToast("请输入您的店名！");
            return;
        }
        if (!StringUtil.isNotNull(etPhone.getText().toString())) {
            RxToast.showToast("请输入联系方式！");
            return;
        }
        if (!StringUtil.isNotNull(tvPlace.getText().toString())) {
            RxToast.showToast("请选择所属场所！");
            return;
        }
        if (!StringUtil.isNotNull(etContent.getText().toString())) {
            RxToast.showToast("请输入场所介绍！");
            return;
        }

        if (!checkBox.isChecked()) {
            RxToast.showToast("请同意快逅开店协议");
            return;
        }
        for (int i = 0; i < selectList.size(); i++) {
            imgs = imgs + selectList.get(i).getThumb() + ",";
        }
        imgs = imgs.substring(0, imgs.length() - 1);
        showWaitDialog(OpenShopActivity.this);
        MQApi.userApplyForShop(mId, userBean.getUId(), etPhone.getText().toString(), etName.getText().toString(),
                companyId, shopType, etContent.getText().toString(), selectList.get(0).getThumb(), imgs, new SimpleCallBack<String>() {
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
//                                JSON.parseObject(s, new MyApiResult<String>().getClass());
                        if (myApiResult.isOk()) {
                            tvSubmit.setEnabled(false);
                            tvSubmit.setText("审核中");
                        } else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }

    private List<LocalMedia> resultSelectList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                placeBean = (ShopPlaceBean) data.getSerializableExtra("bean");
                tvPlace.setText(placeBean.getCompanyName());
                companyId = placeBean.getId();
                tvAddress.setText(placeBean.getAddress());
            } else if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                // 图片选择结果回调
                resultSelectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                uploadPics(resultSelectList);
            }
        }
    }

    private void uploadPics(List<LocalMedia> resultSelectList) {
        if (resultSelectList.size() == 0) {
            return;
        }

        List<File> files = new ArrayList<>();
        for (int i = 0; i < resultSelectList.size(); i++) {
            files.add(new File(resultSelectList.get(i).getCompressPath()));
        }

        showWaitDialog(OpenShopActivity.this);
        MQApi.uploadShopImgs(files, new ProgressResponseCallBack() {
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
                MyApiResult<List<OpenShopBean.StoreImgsBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<OpenShopBean.StoreImgsBean>>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<OpenShopBean.StoreImgsBean>>>() {});
                if (myApiResult.isOk()) {
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        selectList.add(myApiResult.getData().get(i));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }
}

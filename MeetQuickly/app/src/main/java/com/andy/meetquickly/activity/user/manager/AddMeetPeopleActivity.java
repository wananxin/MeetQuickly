package com.andy.meetquickly.activity.user.manager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.find.PublishMomentActivity;
import com.andy.meetquickly.adapter.GridImageAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.CardBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.fragment.updateUserInfo.UpdateBaseInfoFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.StringUtil;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.SelectDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMeetPeopleActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerViewPic;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.radioButtonMen)
    RadioButton radioButtonMen;
    @BindView(R.id.radioButtonWomen)
    RadioButton radioButtonWomen;

    private String nickName;
    private String birthday;
    private String height;
    private String weight;

    private ArrayList<CardBean> cardItem = new ArrayList<>();
    private ArrayList<CardBean> cardItem1 = new ArrayList<>();

    private GridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();

    private UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_meet_people;
    }

    @Override
    public void initView() {
        setBarTitle("添加可能邂逅的人");
        userBean = UserCache.getInstance().getUser();
        height = String.valueOf((int) (160 + Math.random() * (16)));
        weight = String.valueOf((int) (40 + Math.random() * (14)));
        tvHeight.setText(height + "cm");
        tvWeight.setText(weight + "kg");
        birthday = randomDate();
        tvBirthday.setText(birthday);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 145; i < 220; i++) {
            cardItem.add(new CardBean(i, i + ""));
        }
        for (int i = 40; i < 100; i++) {
            cardItem1.add(new CardBean(i, i + ""));
        }

        initCustomOptionPicker();

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
                    }
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
                    if (selectList.size() > 0) {
                        adapter.setList(selectList);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    private void selectPic() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(AddMeetPeopleActivity.this)
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

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            selectPic();
        }
    };


    public String randomDate() {
        Random rndYear = new Random();
        int year = rndYear.nextInt(7) + 1994;  //生成[2000,2017]的整数；年
        Random rndMonth = new Random();
        int month = rndMonth.nextInt(12) + 1;   //生成[1,12]的整数；月
        Random rndDay = new Random();
        int Day = rndDay.nextInt(28) + 1;       //生成[1,30)的整数；日
        return year + "-" + month + "-" + Day;
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.tv_birthday, R.id.tv_height, R.id.tv_weight, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_birthday:
                showSelectBirthdayDialog();
                break;
            case R.id.tv_height:
                pvCustomOptionsHeight.show();
                break;
            case R.id.tv_weight:
                pvCustomOptionsWeith.show();
                break;
            case R.id.tv_submit:
                submit();
                break;
        }
    }

    private void submit() {
        if (!StringUtil.isNotNull(etName.getText().toString())) {
            RxToast.showToast("请填写昵称");
            return;
        }
        if (selectList.size() == 0) {
            RxToast.showToast("请选择图片");
            return;
        }
        List<File> files = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            files.add(new File(selectList.get(i).getCompressPath()));
        }
        showWaitDialog(AddMeetPeopleActivity.this, "正在上传图片...");
        MQApi.regNewUserByVirtual(userBean.getUId(), etName.getText().toString(), height,
                radioButtonWomen.isChecked() ? "2" : "1", weight,
                birthday, files, "files", new ProgressResponseCallBack() {
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
                        MyApiResult<String> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<String>>() {
                        }.getType());
                        if (myApiResult.isOk()) {
                            RxToast.showToast("添加成功");
                            showAddVideoDialog(myApiResult.getData());
                        } else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }

    private void showAddVideoDialog(String uid) {
        SelectDialog.show(mContext, "提示", "已成功添加用户，继续添加视频", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddUserVideoActivity.start(AddMeetPeopleActivity.this,uid);
                finish();
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setCanCancel(true);
    }

    private SimpleDateFormat dateFormat;

    private void showSelectBirthdayDialog() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(selectedDate.get(Calendar.YEAR) - 100, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE));
        endDate.set(selectedDate.get(Calendar.YEAR) - 16, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE));

        TimePickerView pvStartTime = new TimePickerBuilder(AddMeetPeopleActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvBirthday.setText(dateFormat.format(date));
                birthday = dateFormat.format(date);
            }
        }).setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setRangDate(startDate, endDate)
                .setDate(endDate)
                .build();

        pvStartTime.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AddMeetPeopleActivity.class);
        context.startActivity(starter);
    }

    private OptionsPickerView pvCustomOptionsHeight, pvCustomOptionsWeith;

    private void initCustomOptionPicker() {
        pvCustomOptionsHeight = new OptionsPickerBuilder(AddMeetPeopleActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                height = cardItem.get(options1).getPickerViewText();
                tvHeight.setText(height + "cm");
            }
        }).setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                tvSubmit.setText("请选择身高");
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvCustomOptionsHeight.returnData();
                        pvCustomOptionsHeight.dismiss();
                    }
                });
            }
        }).build();
        pvCustomOptionsHeight.setPicker(cardItem);//添加数据

        pvCustomOptionsWeith = new OptionsPickerBuilder(AddMeetPeopleActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                weight = cardItem1.get(options1).getPickerViewText();
                tvWeight.setText(weight + "kg");
            }
        }).setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
//                        final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                tvSubmit.setText("请选择体重");
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvCustomOptionsWeith.returnData();
                        pvCustomOptionsWeith.dismiss();
                    }
                });
            }
        }).build();
        pvCustomOptionsWeith.setPicker(cardItem1);//添加数据
    }

}

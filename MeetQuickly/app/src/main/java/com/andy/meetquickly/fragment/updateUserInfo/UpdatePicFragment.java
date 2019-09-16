package com.andy.meetquickly.fragment.updateUserInfo;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.find.PublishMomentActivity;
import com.andy.meetquickly.adapter.GridImageAdapter;
import com.andy.meetquickly.adapter.UserGridImageAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserMediaBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.updateHeadMessage;
import com.andy.meetquickly.message.updatePicMessage;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.shinichi.library.ImagePreview;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdatePicFragment extends BaseLazyFragment {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private UserGridImageAdapter adapter;
    private UserBean userBean;

    public UpdatePicFragment() {
        // Required empty public constructor
    }

    @Override
    public void fetchData() {
        initDate();
    }

    private void initDate() {
        showWaitDialog(UpdatePicFragment.this.getActivity());
        MQApi.getUserImageOrVideoList(userBean.getUId(), 1, 1, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<UserMediaBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<UserMediaBean>>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<UserMediaBean>>>(){});
                if (myApiResult.isOk()) {
                    selectList.clear();
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

    @Override
    public void doItEvery() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_pic, container, false);
        view.setTag(2);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        return view;
    }

    private List<UserMediaBean> selectList = new ArrayList<>();

    private void initView() {
        userBean = UserCache.getInstance().getUser();
        GridLayoutManager manager = new GridLayoutManager(UpdatePicFragment.this.getContext(), 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new UserGridImageAdapter(UpdatePicFragment.this.getContext(), onAddPicClickListener,onDelPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(9);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new UserGridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    UserMediaBean media = selectList.get(position);
                }
            }
        });
        adapter.setOnItemClickListener(new UserGridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                List<String> lists = new ArrayList<>();
                for (int i = 0; i < selectList.size(); i++) {
                    lists.add(selectList.get(i).getThumb());
                }
                ImagePreview.getInstance()
                        .setContext(UpdatePicFragment.this.getContext())
                        .setIndex(position)
                        .setImageList(lists)
                        .setEnableDragClose(true)
                        .setErrorPlaceHolder(R.drawable.load_failed)
                        .start();
            }
        });
    }

    private UserGridImageAdapter.onAddPicClickListener onAddPicClickListener = new UserGridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            selectPic();
        }
    };

    private UserGridImageAdapter.onDelPicClickListener onDelPicClickListener = new UserGridImageAdapter.onDelPicClickListener() {
        @Override
        public void onDelPicClick(int position) {
            deletePic(position);
        }
    };

    private void deletePic(int position) {
        showWaitDialog(UpdatePicFragment.this.getActivity(),"正在删除图片...");
        MQApi.delUserImageOrVideoList(selectList.get(position).getId(), new SimpleCallBack<String>() {
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
                    selectList.remove(position);
                    adapter.notifyDataSetChanged();
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void selectPic() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create((Activity) UpdatePicFragment.this.getContext())
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_white_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(9  - selectList.size())// 最大图片选择数量
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(updatePicMessage message) {
        updateUserPic(message.getMessage());
    }

    private void updateUserPic(List<LocalMedia> resultSelectList) {
        if (resultSelectList.size() == 0){
            return;
        }
        showWaitDialog(UpdatePicFragment.this.getActivity(),"正在上传图片");
        List<File> files = new ArrayList<>();
        for (int i = 0; i < resultSelectList.size(); i++) {
            files.add(new File(resultSelectList.get(i).getCompressPath()));
        }
        MQApi.updateUserImageOrVideoList(userBean.getUId(), 1, files, "files", new ProgressResponseCallBack() {
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
                    initDate();
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

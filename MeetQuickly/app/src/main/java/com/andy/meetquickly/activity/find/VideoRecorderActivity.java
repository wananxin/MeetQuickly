package com.andy.meetquickly.activity.find;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class VideoRecorderActivity extends BaseActivity {
    private static final String TAG = "VideoRecorderActivity";
    @BindView(R.id.jcameraview)
    JCameraView mJCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getRecorderPermissions();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_recorder;
    }

    public void initView() {

        //设置视频保存路径
        mJCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");

        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        mJCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_RECORDER);
        mJCameraView.setTip("长按拍摄");


        //设置视频质量
        mJCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_HIGH);

        //JCameraView监听
        mJCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //打开Camera失败回调
                Log.i("CJT", "open camera error");
            }

            @Override
            public void AudioPermissionError() {
                //没有录取权限回调
                Log.i("CJT", "AudioPermissionError");
            }
        });

        mJCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                Log.i("JCameraView", "bitmap = " + bitmap.getWidth());
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                Log.i("CJT", "url = " + url);
                Intent intent = new Intent();
                intent.putExtra("video_path", url);
                setResult(-1, intent);
                finish();
            }
        });

        //左边按钮点击事件
        mJCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });


        //右边按钮点击事件
        mJCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {

            }
        });


    }

    @Override
    public void initData() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        mJCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mJCameraView.onPause();
    }

    public static void start(Activity activity) {
        Intent starter = new Intent(activity, VideoRecorderActivity.class);
        activity.startActivityForResult(starter,1992);
    }

    public void getRecorderPermissions() {

        RxPermissions rxPermissions = new RxPermissions(this);
        Disposable subscribe1 = rxPermissions.requestEach(Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            Log.i(TAG, "subscribe2 accept: All requested permissions are granted");
                            // `permission.name` is granted !
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again
                            Log.i(TAG, "subscribe2 accept: else if permission.name=" + permission.name);
                        } else {
                            Log.i(TAG, "subscribe2 accept: else permission.name=" + permission.name);
                            // Denied permission with ask never again
                            // Need to go to the settings
                        }
                    }
                });
    }
}


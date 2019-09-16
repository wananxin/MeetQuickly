package com.wode369.videocroplibrary.features.trim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.wode369.videocroplibrary.R;
import com.wode369.videocroplibrary.databinding.ActivityTrimmerLayoutBinding;
import com.wode369.videocroplibrary.interfaces.VideoTrimListener;
import com.wode369.videocroplibrary.utils.ToastUtil;
/*import com.iknow.android.R;
import com.iknow.android.databinding.ActivityTrimmerLayoutBinding;
import com.iknow.android.interfaces.VideoTrimListener;
import com.iknow.android.utils.ToastUtil;*/

/**
 * Author：J.Chou
 * Date：  2016.08.01 2:23 PM
 * Email： who_know_me@163.com
 * Describe:
 */
public class VideoTrimmerActivity extends AppCompatActivity implements VideoTrimListener {

    private static final String TAG = "jason";
    private static final String VIDEO_PATH_KEY = "video-file-path";
    private static final String COMPRESSED_VIDEO_FILE_NAME = "compress.mp4";
    public static final int VIDEO_TRIM_REQUEST_CODE = 0x001;
    private ActivityTrimmerLayoutBinding mBinding;
    private ProgressDialog mProgressDialog;

    public static void call(FragmentActivity from, String videoPath) {
        if (!TextUtils.isEmpty(videoPath)) {
            Bundle bundle = new Bundle();
            bundle.putString(VIDEO_PATH_KEY, videoPath);
            Intent intent = new Intent(from, VideoTrimmerActivity.class);
            intent.putExtras(bundle);
            from.startActivityForResult(intent, VIDEO_TRIM_REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_trimmer_layout);
        Bundle bd = getIntent().getExtras();
        String path = "";
        if (bd != null) path = bd.getString(VIDEO_PATH_KEY);
        if (mBinding.trimmerView != null) {
            mBinding.trimmerView.setOnTrimVideoListener(this);
            mBinding.trimmerView.initVideoByURI(Uri.parse(path));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.trimmerView.onVideoPause();
        mBinding.trimmerView.setRestoreState(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.trimmerView.onDestroy();
    }

    @Override
    public void onStartTrim() {
        buildDialog(getResources().getString(R.string.trimming)).show();
    }

    @Override
    public void onFinishTrim(String path) {
        Log.i(TAG, "onFinishTrim:剪裁完毕之后的保存路径： path=" + path);
        if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
        ToastUtil.longShow(this, getString(R.string.trimmed_done));
        Intent intent = new Intent();
        intent.putExtra("cutPath", path); //将计算的值回传回去
        //通过intent对象返回结果，必须要调用一个setResult方法，
        //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
        setResult(RESULT_OK, intent);
        finish();
        //TODO: please handle your trimmed video url here!!!
        //String out = StorageUtil.getCacheDir() + File.separator + COMPRESSED_VIDEO_FILE_NAME;
        //buildDialog(getResources().getString(R.string.compressing)).show();
        //VideoCompressor.compress(this, in, out, new VideoCompressListener() {
        //  @Override public void onSuccess(String message) {
        //  }
        //
        //  @Override public void onFailure(String message) {
        //  }
        //
        //  @Override public void onFinish() {
        //    if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
        //    finish();
        //  }
        //});
    }

    @Override
    public void onCancel() {
        mBinding.trimmerView.onDestroy();
        finish();
    }

    private ProgressDialog buildDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "", msg);
        }
        mProgressDialog.setMessage(msg);
        return mProgressDialog;
    }
}

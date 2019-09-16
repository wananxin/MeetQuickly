package com.andy.meetquickly.activity.user.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.fragment.updateUserInfo.UpdateVideoFragment;
import com.andy.meetquickly.message.updatePicMessage;
import com.andy.meetquickly.message.updateVideoMessage;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddUserVideoActivity extends BaseActivity {

    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_user_video;
    }

    @Override
    public void initView() {
        setBarTitle("添加视频");
    }

    @Override
    public void initData() {

    }


    public static void start(Context context,String uId) {
        Intent starter = new Intent(context, AddUserVideoActivity.class);
        starter.putExtra("uId",uId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uId = this.getIntent().getStringExtra("uId");
        UpdateVideoFragment firstFragment = UpdateVideoFragment.newInstance(uId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, firstFragment);
        transaction.commit();
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    public static final int VIDEO_TRIM_REQUEST_CODE = 23;
    public static final int VIDEO_MY_REQUEST_CODE = 1992;
    private final static int VIDEO_REQUESTCODE = 22;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case VIDEO_TRIM_REQUEST_CODE:
                    //剪切回来的文件
                    String videoPath = data.getStringExtra("cutPath");
                    EventBus.getDefault().post(new updateVideoMessage(videoPath, 1));

                    break;
                case VIDEO_MY_REQUEST_CODE:
                    //拍摄返回
                    String pa1 = data.getStringExtra("video_path");
                    EventBus.getDefault().post(new updateVideoMessage(pa1, 0));
                    break;
                case VIDEO_REQUESTCODE:
                    //选择返回
                    String pa2 = data.getStringExtra("video_path");
                    EventBus.getDefault().post(new updateVideoMessage(pa2, 0));
                    break;
            }
        }
    }

}

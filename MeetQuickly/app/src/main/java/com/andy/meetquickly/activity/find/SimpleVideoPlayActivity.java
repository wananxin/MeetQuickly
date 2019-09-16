package com.andy.meetquickly.activity.find;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseInvasionActivity;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.andy.meetquickly.utils.JZMediaExo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class SimpleVideoPlayActivity extends BaseInvasionActivity {

    @BindView(R.id.jz_video)
    JzvdStd jzVideo;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private String url;

    @Override
    public int getLayoutId() {
        return R.layout.activity_simple_video_play;
    }

    @Override
    public void initView() {
        url = this.getIntent().getStringExtra("url");
        jzVideo.setUp(url, "", JzvdStd.SCREEN_NORMAL, JZMediaExo.class);
        ImageLoaderUtil.getInstance().loadImage(SimpleVideoPlayActivity.this, url, jzVideo.thumbImageView);
        jzVideo.startVideo();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initView();
    }

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, SimpleVideoPlayActivity.class);
        starter.putExtra("url", url);
        context.startActivity(starter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.resetAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();

    }

    @OnClick({R.id.jz_video, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.jz_video:
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}

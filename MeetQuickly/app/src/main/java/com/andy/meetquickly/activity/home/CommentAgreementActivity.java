package com.andy.meetquickly.activity.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAgreementActivity extends BaseActivity {


    @BindView(R.id.webView)
    WebView webView;
    private String title;
    private String url;

    @Override
    public int getLayoutId() {
        return R.layout.activity_comment_agreement;
    }

    @Override
    public void initView() {
        title = this.getIntent().getStringExtra("title");
        url = this.getIntent().getStringExtra("url");
        setBarTitle(title);

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.

        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showWaitDialog(CommentAgreementActivity.this);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dissmissWaitDialog();
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public void initData() {

    }

    public static void start(Context context, String title, String url) {
        Intent starter = new Intent(context, CommentAgreementActivity.class);
        starter.putExtra("title", title);
        starter.putExtra("url", url);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

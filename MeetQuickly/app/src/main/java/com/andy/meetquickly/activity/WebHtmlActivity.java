package com.andy.meetquickly.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebHtmlActivity extends BaseActivity {


    @BindView(R.id.webview)
    WebView webview;
    private String html;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_html;
    }

    @Override
    public void initView() {
        setBarTitle("协议");
        html = this.getIntent().getStringExtra("html");
        webview.loadDataWithBaseURL(null, html, "text/html", "utf-8",null);
    }

    @Override
    public void initData() {

    }

    public static void start(Context context, String html) {
        Intent starter = new Intent(context, WebHtmlActivity.class);
        starter.putExtra("html", html);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

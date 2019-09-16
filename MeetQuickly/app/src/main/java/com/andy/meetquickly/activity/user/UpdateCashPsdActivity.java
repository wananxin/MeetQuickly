package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;

public class UpdateCashPsdActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_cash_psd;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, UpdateCashPsdActivity.class);
        context.startActivity(starter);
    }
}

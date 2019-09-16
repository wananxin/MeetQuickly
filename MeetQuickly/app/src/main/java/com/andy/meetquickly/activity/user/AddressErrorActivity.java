package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressErrorActivity extends BaseActivity {

    @BindView(R.id.textViewName)
    TextView textViewName;
    @BindView(R.id.textViewAddress)
    TextView textViewAddress;
    @BindView(R.id.textViewUserName)
    TextView textViewUserName;
    @BindView(R.id.textViewPhone)
    TextView textViewPhone;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_content_numb)
    TextView tvContentNumb;

    @Override
    public int getLayoutId() {
        return R.layout.activity_address_error;
    }

    @Override
    public void initView() {
        setBarTitle("反馈");

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AddressErrorActivity.class);
        context.startActivity(starter);
    }
}

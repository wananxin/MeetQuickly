package com.andy.meetquickly.activity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.utils.CommentUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.vondear.rxtool.RxKeyboardTool;
import com.vondear.rxtool.view.RxToast;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditInfomationActivity extends BaseActivity {

    @BindView(R.id.et_nickname)
    AppCompatEditText etNickname;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.rb_man)
    RadioButton rbMan;
    @BindView(R.id.rb_women)
    RadioButton rbWomen;
    @BindView(R.id.et_password)
    AppCompatEditText etPassword;
    @BindView(R.id.et_confirm_password)
    AppCompatEditText etConfirmPassword;
    @BindView(R.id.view_password)
    View viewPassword;
    @BindView(R.id.view_confirm_password)
    View viewConfirmPassword;

    private String birthDay = "";
    private String phone = "";
    private String sex = "1";
    private Pattern pattern;
    private int type;// 0为需要密码   1为不需要密码

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_infomation;
    }

    @Override
    public void initView() {
        type = this.getIntent().getIntExtra("type",0);
        phone = this.getIntent().getStringExtra("phone");
        pattern = Pattern.compile("^[0-9a-zA-Z#@!~%^&*.]{6,16}+$");
        if (type == 1){
            viewPassword.setVisibility(View.GONE);
            viewConfirmPassword.setVisibility(View.GONE);
            etPassword.setVisibility(View.GONE);
            etConfirmPassword.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {

    }

    public static void start(Context context, String phone, int type) {
        Intent starter = new Intent(context, EditInfomationActivity.class);
        starter.putExtra("phone", phone);
        starter.putExtra("type", type);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_birthday, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_birthday:
                showBirthdayDialog();
                break;
            case R.id.tv_sure:
                toUserHead();
                break;
        }
    }

    private void showBirthdayDialog() {
        RxKeyboardTool.hideSoftInput((Activity) mContext);
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(selectedDate.get(Calendar.YEAR)-100,selectedDate.get(Calendar.MONTH),selectedDate.get(Calendar.DATE));
        endDate.set(selectedDate.get(Calendar.YEAR)-16,selectedDate.get(Calendar.MONTH),selectedDate.get(Calendar.DATE));

        TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                birthDay = CommentUtil.getTime(date);
                tvBirthday.setText(birthDay);
            }
        }).setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setRangDate(startDate,endDate)
                .setDate(endDate)
                .build();
        pvTime.show();
    }

    private void toUserHead() {
        if (TextUtils.isEmpty(etNickname.getText().toString().trim())) {
            RxToast.showToast("请您填写昵称！");
            return;
        }

        if (birthDay.isEmpty()) {
            RxToast.showToast("请您选择生日！");
            return;
        }

        if (type == 0) {

            if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
                RxToast.showToast("请您填写密码！");
                return;
            }

            if (etPassword.getText().toString().trim().length() < 6) {
                RxToast.showToast("密码最少6位数");
                return;
            }

            if (TextUtils.isEmpty(etConfirmPassword.getText().toString().trim())) {
                RxToast.showToast("请您填写确认密码！");
                return;
            }

            if (!etPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())) {
                RxToast.showToast("两次密码不一致");
                return;
            }

            if (!pattern.matcher(etPassword.getText().toString().trim()).matches()) {
                RxToast.showToast("密码只能6-18位且不包含特殊字符");
                return;
            }
        }

        if (rbMan.isChecked()) {
            sex = "1";
        } else {
            sex = "2";
        }

        UploadHeadActivity.start(mContext, etNickname.getText().toString().trim(),
                phone, sex, etPassword.getText().toString().trim(), birthDay);
    }
}

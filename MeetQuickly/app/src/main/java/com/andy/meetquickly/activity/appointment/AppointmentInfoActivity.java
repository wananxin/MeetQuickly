package com.andy.meetquickly.activity.appointment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.SoftHideKeyBoardUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.listener.DialogLifeCycleListener;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.MessageDialog;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/22  19:16
 * 描述：     填写订单需求
 */
public class AppointmentInfoActivity extends BaseActivity {

    @BindView(R.id.et_need)
    EditText etNeed;
    @BindView(R.id.tv_numb)
    TextView tvNumb;
    @BindView(R.id.rb_shangwu)
    RadioButton rbShangwu;
    @BindView(R.id.rb_liangfan)
    RadioButton rbLiangfan;
    @BindView(R.id.rb_jiuba)
    RadioButton rbJiuba;
    @BindView(R.id.rb_qingba)
    RadioButton rbQingba;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.rb_man)
    RadioButton rbMan;
    @BindView(R.id.rb_women)
    RadioButton rbWomen;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.textView143)
    TextView textViewSure;
    @BindView(R.id.textViewPeople)
    TextView textViewPeople;
    @BindView(R.id.editTextPhone)
    EditText editTextPhone;
    @BindView(R.id.radioGroup2)
    RadioGroup radioGroup2;
    @BindView(R.id.textViewCompanyName)
    TextView textViewCompanyName;

    private int orderType;
    private String storeId = "";  //
    private String coupon = "";   //
    private String userCouponId = ""; //
    private String missLovers = "";  //
    private String activityId = "";  //
    private String kind = "";  //
    private String companyName = "";
    private String storeName = "";
    private int peopleNumb = 6;
    private UserBean userBean;
    private String targetDate;
    private String reachTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_appointment_info;
    }

    @Override
    public void initView() {
        setBarTitle("填写信息");
        userBean = UserCache.getInstance().getUser();
        SoftHideKeyBoardUtil.assistActivity(this);
        seekBar.setProgress(5);
        textViewPeople.setText("( 6 )");
        orderType = this.getIntent().getIntExtra("orderType", 1);
        storeId = this.getIntent().getStringExtra("storeId");
        coupon = this.getIntent().getStringExtra("coupon");
        userCouponId = this.getIntent().getStringExtra("userCouponId");
        missLovers = this.getIntent().getStringExtra("missLovers");
        activityId = this.getIntent().getStringExtra("activityId");
        kind = this.getIntent().getStringExtra("kind");
        companyName = this.getIntent().getStringExtra("companyName");
        storeName = this.getIntent().getStringExtra("storeName");
        editTextPhone.setText(userBean.getMobile());
        if ("1".equals(userBean.getSex())) {
            rbMan.setChecked(true);
        } else {
            rbWomen.setChecked(true);
        }
        if (StringUtil.isNotNull(companyName)) {
            radioGroup2.setVisibility(View.GONE);
            textViewCompanyName.setVisibility(View.VISIBLE);
            textViewCompanyName.setText("您当前选择的场所：" + companyName);
        } else {
            radioGroup2.setVisibility(View.VISIBLE);
            textViewCompanyName.setVisibility(View.GONE);
        }
        if (StringUtil.isNotNull(kind)) {
            rbShangwu.setEnabled(false);
            rbJiuba.setEnabled(false);
            rbLiangfan.setEnabled(false);
            rbQingba.setEnabled(false);
            if ("1".equals(kind)) rbShangwu.setChecked(true);
            if ("2".equals(kind)) rbJiuba.setChecked(true);
            if ("3".equals(kind)) rbLiangfan.setChecked(true);
            if ("4".equals(kind)) rbQingba.setChecked(true);
        } else {
            kind = "1";
            rbShangwu.setChecked(true);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                peopleNumb = progress + 1;
                textViewPeople.setText("( " + String.valueOf(progress + 1) + " )");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        etNeed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvNumb.setText(s.length() + "/120");
            }
        });
    }

    @Override
    public void initData() {

    }

    public static void start(Context context, int orderType, String storeId, String coupon, String userCouponId,
                             String missLovers, String activityId, String kind, String companyName, String storeName) {
        Intent starter = new Intent(context, AppointmentInfoActivity.class);
        starter.putExtra("orderType", orderType);
        starter.putExtra("storeId", storeId);
        starter.putExtra("coupon", coupon);
        starter.putExtra("userCouponId", userCouponId);
        starter.putExtra("missLovers", missLovers);
        starter.putExtra("activityId", activityId);
        starter.putExtra("kind", kind);
        starter.putExtra("companyName", companyName);
        starter.putExtra("storeName", storeName);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.textView143)
    public void onViewClicked() {
        submitOrder();
    }

    int useCoupon;

    private void submitOrder() {

        if (!StringUtil.isNotNull(tvTime.getText().toString())) {
            RxToast.showToast("请选择到店时间");
            return;
        }

        if (!StringUtil.isNotNull(etName.getText().toString())) {
            RxToast.showToast("请输入您的尊称");
            return;
        }

        if (!StringUtil.isNotNull(editTextPhone.getText().toString())) {
            RxToast.showToast("请输入您的联系电话");
            return;
        }


        if (StringUtil.isNotNull(userCouponId)) {
            useCoupon = 1;
        } else {
            useCoupon = 2;
        }
        showWaitDialog(AppointmentInfoActivity.this);
        MQApi.addUserOrder(userBean.getUId(), orderType, storeId, useCoupon, coupon, userCouponId, etName.getText().toString()
                , editTextPhone.getText().toString(), kind, targetDate, reachTime, String.valueOf(peopleNumb),
                etNeed.getText().toString(), missLovers, activityId, rbMan.isChecked() ? "1" : "2", new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        dissmissWaitDialog();
                        RxToast.showToast(e.toString());
                    }

                    @Override
                    public void onSuccess(String s) {
                        dissmissWaitDialog();
                        MyApiResult myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                        }.getType());
                        if (myApiResult.isOk()) {
                            showMessageDialog();
                        } else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }

    private void showMessageDialog() {
        DialogSettings.dialog_cancelable_default = false;
        MessageDialog messageDialog = MessageDialog.show(AppointmentInfoActivity.this, "系统已接单", "稍后客服会与您联系，请注意接听", "知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        messageDialog.setCanCancel(false);
        messageDialog.setDialogLifeCycleListener(new DialogLifeCycleListener() {
            @Override
            public void onCreate(Dialog alertDialog) {

            }

            @Override
            public void onShow(Dialog alertDialog) {

            }

            @Override
            public void onDismiss() {
                finish();
            }
        });
    }

    @OnClick({R.id.rb_shangwu, R.id.rb_liangfan, R.id.rb_jiuba, R.id.rb_qingba, R.id.tv_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_shangwu:
                kind = "1";
                break;
            case R.id.rb_liangfan:
                kind = "3";
                break;
            case R.id.rb_jiuba:
                kind = "2";
                break;
            case R.id.rb_qingba:
                kind = "4";
                break;
            case R.id.tv_time:
                showSelectTime();
                break;
        }
    }

    private SimpleDateFormat dateFormatDate;
    private SimpleDateFormat dateFormatTime;

    private void showSelectTime() {
        if (null == dateFormatDate || null == dateFormatTime) {
            dateFormatDate = new SimpleDateFormat("MM-dd");
            dateFormatTime = new SimpleDateFormat("HH:mm");
        }
        TimePickerView pvStartTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                targetDate = dateFormatDate.format(date);
                reachTime = dateFormatTime.format(date);
                tvTime.setText(targetDate + " " + reachTime);
            }
        }).setType(new boolean[]{false, true, true, true, true, false})// 默认全部显示
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .build();

        pvStartTime.show();
    }

    @Override
    protected void onDestroy() {
        DialogSettings.dialog_cancelable_default = true;
        super.onDestroy();
    }
}

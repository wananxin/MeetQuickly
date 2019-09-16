package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.user.manager.SelectImgActivity;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserShopDetailBean;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/1  16:36
 * 描述：     酒水配置（单点）
 */
public class EditDanDianActivity extends BaseActivity {

    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.radioButton0)
    RadioButton radioButton0;
    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.radioButton2)
    RadioButton radioButton2;
    @BindView(R.id.radioButton3)
    RadioButton radioButton3;
    @BindView(R.id.radioButton4)
    RadioButton radioButton4;
    @BindView(R.id.radioButton5)
    RadioButton radioButton5;
    @BindView(R.id.radioButton6)
    RadioButton radioButton6;
    @BindView(R.id.et_numb)
    EditText etNumb;
    @BindView(R.id.et_yuanjia)
    EditText etYuanjia;
    @BindView(R.id.et_zhekoujia)
    EditText etZhekoujia;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.rb_custom1)
    RadioButton rbCustom1;
    @BindView(R.id.rb_custom2)
    RadioButton rbCustom2;
    @BindView(R.id.rb_custom3)
    RadioButton rbCustom3;
    @BindView(R.id.radioGroup_custom)
    RadioGroup radioGroupCustom;
    @BindView(R.id.et_custom_numb)
    EditText etCustomNumb;
    @BindView(R.id.ll_unit_custom)
    LinearLayout llUnitCustom;
    @BindView(R.id.radioGroup_all)
    RadioGroup radioGroupAll;
    @BindView(R.id.ll_custom_edit)
    LinearLayout llCustomEdit;
    @BindView(R.id.tv_show_pic)
    TextView tvShowPic;
    @BindView(R.id.tv_show_name)
    TextView tvShowName;
    @BindView(R.id.tv_show_unit)
    TextView tvShowUnit;
    @BindView(R.id.tv_show_remark)
    TextView tvShowRemark;

    private int type;// 0为增加，1为修改
    private int jiushuiType;
    private String storeId;
    private String title;
    private UserShopDetailBean.SpendTypeListBean bean;
    private String imgUrl;
    private int unitType = 0;
    private int selectType = -1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_dan_dian;
    }

    @Override
    public void initView() {
        type = this.getIntent().getIntExtra("type", 0);
        jiushuiType = this.getIntent().getIntExtra("jiushuiType", 0);
        storeId = this.getIntent().getStringExtra("storeId");
        title = this.getIntent().getStringExtra("title");
        bean = (UserShopDetailBean.SpendTypeListBean) this.getIntent().getSerializableExtra("bean");
        setBarTitle(title);
        if (type == 1) {
            setRightTextAndClick("删除", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteType();
                }
            });
            imgUrl = bean.getDrinksImg();
            ImageLoaderUtil.getInstance().loadImage(mContext, bean.getDrinksImg(), ivPic);
            etName.setText(bean.getName());
            etContent.setText(bean.getRemark());
            etYuanjia.setText(bean.getCost());
            etZhekoujia.setText(bean.getPrice());
        }

        if (type == 1) {
            switch (jiushuiType) {
                case 1:
                    if (bean.getUnit().equals("瓶")) {
                        rbCustom3.setChecked(true);
                        break;
                    } else if (bean.getUnit().equals("打")) {
                        rbCustom2.setChecked(true);
                        break;
                    } else {
                        rbCustom1.setChecked(true);
                        break;
                    }
                case 2:
                    if (bean.getUnit().indexOf("ml") != -1) {
                        etCustomNumb.setText(bean.getUnit().replace("ml", ""));
                        break;
                    }
                    if (bean.getUnit().equals("瓶")) {
                        rbCustom2.setChecked(true);
                        break;
                    } else {
                        rbCustom1.setChecked(true);
                        break;
                    }
                case 3:
                    if (bean.getUnit().indexOf("ml") != -1) {
                        etCustomNumb.setText(bean.getUnit().replace("ml", ""));
                        break;
                    }
                    if (bean.getUnit().equals("瓶")) {
                        rbCustom2.setChecked(true);
                        break;
                    } else {
                        rbCustom1.setChecked(true);
                        break;
                    }
                case 4:
                    if (bean.getUnit().indexOf("ml") != -1) {
                        etCustomNumb.setText(bean.getUnit().replace("ml", ""));
                        break;
                    }
                    if (bean.getUnit().equals("杯")) {
                        rbCustom1.setChecked(true);
                        break;
                    }
                case 5:
                    if (bean.getUnit().equals("个")) {
                        rbCustom3.setChecked(true);
                        break;
                    } else if (bean.getUnit().equals("盒")) {
                        rbCustom2.setChecked(true);
                        break;
                    } else {
                        rbCustom1.setChecked(true);
                        break;
                    }
                case 6:
                    if (bean.getUnit().indexOf("ml") != -1) {
                        etNumb.setText(bean.getUnit().replace("ml", ""));
                        break;
                    }
                    if (bean.getUnit().equals("盒")) {
                        radioButton6.setChecked(true);
                        break;
                    } else if (bean.getUnit().equals("杯")) {
                        radioButton5.setChecked(true);
                        break;
                    } else if (bean.getUnit().equals("盘")) {
                        radioButton4.setChecked(true);
                        break;
                    } else if (bean.getUnit().equals("个")) {
                        radioButton3.setChecked(true);
                        break;
                    } else if (bean.getUnit().equals("瓶")) {
                        radioButton2.setChecked(true);
                        break;
                    } else if (bean.getUnit().equals("打")) {
                        radioButton1.setChecked(true);
                        break;
                    } else {
                        radioButton0.setChecked(true);
                        break;
                    }
            }
        }

        switch (jiushuiType) {
            case 1:
                radioGroupAll.setVisibility(View.GONE);
                llUnitCustom.setVisibility(View.VISIBLE);
                rbCustom1.setText("箱");
                rbCustom2.setText("打");
                rbCustom3.setText("瓶");
                llCustomEdit.setVisibility(View.GONE);
                break;
            case 2:
                radioGroupAll.setVisibility(View.GONE);
                llUnitCustom.setVisibility(View.VISIBLE);
                rbCustom1.setText("箱");
                rbCustom2.setText("瓶");
                rbCustom3.setVisibility(View.GONE);
                llCustomEdit.setVisibility(View.VISIBLE);
                break;
            case 3:
                radioGroupAll.setVisibility(View.GONE);
                llUnitCustom.setVisibility(View.VISIBLE);
                rbCustom1.setText("箱");
                rbCustom2.setText("瓶");
                rbCustom3.setVisibility(View.GONE);
                llCustomEdit.setVisibility(View.VISIBLE);
                break;
            case 4:
                radioGroupAll.setVisibility(View.GONE);
                llUnitCustom.setVisibility(View.VISIBLE);
                rbCustom1.setText("杯");
                rbCustom2.setVisibility(View.GONE);
                rbCustom3.setVisibility(View.GONE);
                llCustomEdit.setVisibility(View.VISIBLE);
                break;
            case 5:
                radioGroupAll.setVisibility(View.GONE);
                llUnitCustom.setVisibility(View.VISIBLE);
                rbCustom1.setText("盘");
                rbCustom2.setText("盒");
                rbCustom3.setText("个");
                llCustomEdit.setVisibility(View.GONE);
                break;
            case 6:
                radioGroupAll.setVisibility(View.VISIBLE);
                llUnitCustom.setVisibility(View.GONE);
                break;
            case 7:
                tvShowPic.setText("套餐图片");
                tvShowName.setText("套餐名称");
                tvShowUnit.setVisibility(View.GONE);
                tvShowRemark.setText("套餐内容");
                etName.setHint("请输入套餐名称");
                etYuanjia.setHint("请输入此套餐的原价");
                etZhekoujia.setHint("请输入此套餐的折后价");
                etContent.setHint("请输入套餐详细内容");
                radioGroupAll.setVisibility(View.GONE);
                llUnitCustom.setVisibility(View.GONE);
                break;
        }

        etCustomNumb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isNotNull(s.toString())) {
                    if (Integer.parseInt(s.toString()) > 0) {
                        unitType = 1;
                        selectType = -1;
                        rbCustom1.setChecked(false);
                        rbCustom2.setChecked(false);
                        rbCustom3.setChecked(false);
                    }
                }
            }
        });

        etNumb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isNotNull(s.toString())) {
                    if (Integer.parseInt(s.toString()) > 0) {
                        unitType = 1;
                        selectType = -1;
                        radioButton0.setChecked(false);
                        radioButton1.setChecked(false);
                        radioButton2.setChecked(false);
                        radioButton3.setChecked(false);
                        radioButton4.setChecked(false);
                        radioButton5.setChecked(false);
                        radioButton6.setChecked(false);
                    }
                }
            }
        });

    }

    private void deleteType() {
        showWaitDialog(EditDanDianActivity.this);
        MQApi.delStoreConsumption(bean.getId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());
//                        JSON.parseObject(s, new MyApiResult<String>().getClass());
                if (myApiResult.isOk()) {
                    RxToast.showToast("删除成功");
                    finish();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    public static void start(Context context, int type, int jiushuiType, String storeId, String title, UserShopDetailBean.SpendTypeListBean bean) {
        Intent starter = new Intent(context, EditDanDianActivity.class);
        starter.putExtra("type", type);
        starter.putExtra("jiushuiType", jiushuiType);
        starter.putExtra("title", title);
        starter.putExtra("storeId", storeId);
        starter.putExtra("bean", bean);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_pic, R.id.rb_custom1, R.id.rb_custom2, R.id.rb_custom3, R.id.radioButton0, R.id.radioButton1, R.id.radioButton2, R.id.radioButton3, R.id.radioButton4, R.id.radioButton5, R.id.radioButton6, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pic:
                SelectImgActivity.start(EditDanDianActivity.this, jiushuiType);
                break;
            case R.id.radioButton0:
                clearRb();
                selectType = 1;
                radioButton0.setChecked(true);
                break;
            case R.id.radioButton1:
                clearRb();
                selectType = 2;
                radioButton1.setChecked(true);
                break;
            case R.id.radioButton2:
                clearRb();
                selectType = 3;
                radioButton2.setChecked(true);
                break;
            case R.id.radioButton3:
                clearRb();
                selectType = 4;
                radioButton3.setChecked(true);
                break;
            case R.id.radioButton4:
                clearRb();
                selectType = 5;
                radioButton4.setChecked(true);
                break;
            case R.id.radioButton5:
                clearRb();
                selectType = 6;
                radioButton5.setChecked(true);
                break;
            case R.id.radioButton6:
                clearRb();
                selectType = 7;
                radioButton6.setChecked(true);
                break;
            case R.id.rb_custom1:
                clearRb();
                selectType = 1;
                break;
            case R.id.rb_custom2:
                clearRb();
                selectType = 2;
                break;
            case R.id.rb_custom3:
                clearRb();
                selectType = 3;
                break;
            case R.id.tv_sure:
                submit();
                break;
        }
    }

    public void clearRb() {
        unitType = 2;
        radioButton0.setChecked(false);
        radioButton1.setChecked(false);
        radioButton2.setChecked(false);
        radioButton3.setChecked(false);
        radioButton4.setChecked(false);
        radioButton5.setChecked(false);
        radioButton6.setChecked(false);
        etNumb.setText("");
        etCustomNumb.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            if (requestCode == 200) {
                imgUrl = data.getStringExtra("imgUrl");
                ImageLoaderUtil.getInstance().loadImage(EditDanDianActivity.this, imgUrl, ivPic);
            }
        }
    }

    private void submit() {

        String unit;
        switch (jiushuiType) {
            case 7:
                unit = "份";
                break;
            case 6:
                if (unitType == 1){
                    if (StringUtil.isNotNull(etNumb.getText().toString())){
                        if (Integer.parseInt(etNumb.getText().toString())>0){
                            unit = etNumb.getText().toString()+"ml";
                        }else {
                            RxToast.showToast("请选择单位");
                            return;
                        }
                    }else {
                        RxToast.showToast("请选择单位");
                        return;
                    }
                }else if (unitType == 2){
                    if (selectType == 1){
                        unit = "箱";
                    }else if (selectType == 2){
                        unit = "打";

                    }else if (selectType == 3){
                        unit = "瓶";

                    }else if (selectType == 4){
                        unit = "个";

                    }else if (selectType == 5){
                        unit = "盘";

                    }else if (selectType == 6){
                        unit = "杯";
                    }else if (selectType == 7){
                        unit = "盒";

                    }else{
                        RxToast.showToast("请选择单位");
                        return;
                    }
                }else {
                    RxToast.showToast("请选择单位");
                    return;
                }
                break;
                default:
                    if (unitType == 1){
                        if (StringUtil.isNotNull(etCustomNumb.getText().toString())){
                            if (Integer.parseInt(etCustomNumb.getText().toString())>0){
                                unit = etCustomNumb.getText().toString()+"ml";
                            }else {
                                RxToast.showToast("请选择单位");
                                return;
                            }
                        }else {
                            RxToast.showToast("请选择单位");
                            return;
                        }
                    }else if (unitType == 2){
                        if (selectType == 1){
                            if (jiushuiType == 4){
                                unit = "杯";
                            }else if (jiushuiType == 5){
                                unit = "盘";
                            }else {
                                unit = "箱";
                            }
                        }else if (selectType == 2){
                            if (jiushuiType == 1) {
                                unit = "打";
                            }else if (jiushuiType == 5){
                                unit = "盒";
                            }else {
                                unit = "瓶";
                            }
                        }else if (selectType == 3){
                            unit = "瓶";
                        }else{
                            RxToast.showToast("请选择单位");
                            return;
                        }
                    }else {
                        RxToast.showToast("请选择单位");
                        return;
                    }
                    break;
        }
        showWaitDialog(EditDanDianActivity.this);
        if (type == 0) {
            MQApi.addStoreConsumption(storeId, etName.getText().toString(), jiushuiType, unit,
                    etZhekoujia.getText().toString(), etYuanjia.getText().toString(), imgUrl,
                    etContent.getText().toString(), new SimpleCallBack<String>() {
                        @Override
                        public void onError(ApiException e) {
                            dissmissWaitDialog();
                            RxToast.showToast(e.toString());
                        }

                        @Override
                        public void onSuccess(String s) {
                            dissmissWaitDialog();
                            MyApiResult myApiResult =
                                    new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());
//                                    JSON.parseObject(s, new MyApiResult<String>().getClass());
                            if (myApiResult.isOk()) {
                                RxToast.showToast("添加消费成功");
                                finish();
                            } else {
                                RxToast.showToast(myApiResult.getMsg());
                            }
                        }
                    });
        } else {
            MQApi.updateStoreConsumption(bean.getId(), etName.getText().toString(), jiushuiType, unit,
                    etZhekoujia.getText().toString(), etYuanjia.getText().toString(), imgUrl,
                    etContent.getText().toString(), new SimpleCallBack<String>() {
                        @Override
                        public void onError(ApiException e) {
                            dissmissWaitDialog();
                            RxToast.showToast(e.toString());
                        }

                        @Override
                        public void onSuccess(String s) {
                            dissmissWaitDialog();
                            MyApiResult myApiResult =
                                    new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());
//                                    JSON.parseObject(s, new MyApiResult<String>().getClass());
                            if (myApiResult.isOk()) {
                                RxToast.showToast("消费修改成功");
                                finish();
                            } else {
                                RxToast.showToast(myApiResult.getMsg());
                            }
                        }
                    });
        }
    }
}

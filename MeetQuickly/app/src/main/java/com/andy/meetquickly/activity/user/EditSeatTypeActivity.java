package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.UserShopDetailBean;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.LogUtil;
import com.appyvet.materialrangebar.RangeBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditSeatTypeActivity extends BaseActivity {

    @BindView(R.id.et_seat_type)
    EditText etSeatType;
    @BindView(R.id.tv_people_numb)
    TextView tvPeopleNumb;
    @BindView(R.id.rangebar)
    RangeBar rangebar;
    @BindView(R.id.et_box_free)
    EditText etBoxFree;
    @BindView(R.id.et_min_free)
    EditText etMinFree;
    @BindView(R.id.et_content)
    EditText etContent;
    private int type;// 0为增加，1为修改
    private String storeId;
    private UserShopDetailBean.StoreTableTypeBean bean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_seat_type;
    }

    @Override
    public void initView() {
        setBarTitle("台位类型");
        type = this.getIntent().getIntExtra("type", 0);
        storeId = this.getIntent().getStringExtra("storeId");
        bean = (UserShopDetailBean.StoreTableTypeBean) this.getIntent().getSerializableExtra("bean");
        tvPeopleNumb.setText("(1~40)");
        if (type == 1) {
            setRightTextAndClick("删除", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteTable();
                }
            });
            etSeatType.setText(bean.getName());
            etBoxFree.setText(bean.getBoxFee());
            etMinFree.setText(bean.getMinCharge());
            etContent.setText(bean.getRemark());
            rangebar.setRangePinsByIndices(Integer.parseInt(bean.getMinPeople())-1,Integer.parseInt(bean.getMaxPeople())-1);
            tvPeopleNumb.setText("("+bean.getMinPeople() + "~" + bean.getMaxPeople() + ")");
        }

        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                tvPeopleNumb.setText("("+(leftPinIndex+1) + "~" + (rightPinIndex+1) + ")");
                LogUtil.e(leftPinValue+":"+rightPinValue);
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {

            }
        });
    }

    @Override
    public void initData() {

    }

    public static void start(Context context, int type, String storeId, UserShopDetailBean.StoreTableTypeBean bean) {
        Intent starter = new Intent(context, EditSeatTypeActivity.class);
        starter.putExtra("type", type);
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

    @OnClick(R.id.tv_sure)
    public void onViewClicked() {
        submit();
    }

    private void deleteTable() {
        showWaitDialog(EditSeatTypeActivity.this);
        MQApi.delShopTableInfo(bean.getId(), new SimpleCallBack<String>() {
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

    private void submit() {
        showWaitDialog(EditSeatTypeActivity.this);
        if (type == 0) {
            MQApi.addShopTableInfo(storeId, etSeatType.getText().toString(), rangebar.getLeftPinValue(),
                    rangebar.getRightPinValue(),etBoxFree.getText().toString(),
                    etMinFree.getText().toString(),etContent.getText().toString(),new SimpleCallBack<String>() {
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
                                RxToast.showToast("添加台位成功");
                                finish();
                            } else {
                                RxToast.showToast(myApiResult.getMsg());
                            }
                        }
                    });
        } else {
            MQApi.updateShopTableInfo(bean.getId(), etSeatType.getText().toString(), rangebar.getLeftPinValue(),
                    rangebar.getRightPinValue(),etBoxFree.getText().toString(),
                    etMinFree.getText().toString(),etContent.getText().toString(), new SimpleCallBack<String>() {
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
                                RxToast.showToast("修改台位成功");
                                finish();
                            } else {
                                RxToast.showToast(myApiResult.getMsg());
                            }
                        }
                    });
        }
    }
}

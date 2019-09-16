package com.andy.meetquickly.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.appointment.UseCouponActivity;
import com.andy.meetquickly.adapter.FindPeoplePlayAdapter;
import com.andy.meetquickly.adapter.WherePlayAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.MeetPeopleBean;
import com.andy.meetquickly.bean.ShopInfoBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.fragment.HomeFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.KeyboardUtils;
import com.andy.meetquickly.utils.SoftHideKeyBoardUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeSearchActivity extends BaseActivity {

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<ShopInfoBean> mDataShop = new ArrayList<>();
    private WherePlayAdapter adapterShop;

    private List<MeetPeopleBean> mDataUser = new ArrayList<>();
    private FindPeoplePlayAdapter adapterUser;

    private UserBean userBean;
    private int type = 1; //1是网店  2是用户
    private TextView textViewLingqu;
    private View notDataView;
    private View errorView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_home_search;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, HomeSearchActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void initView() {
        userBean = UserCache.getInstance().getUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapterShop = new WherePlayAdapter(mDataShop, HomeSearchActivity.this);
        adapterShop.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShopDetailActivity.start(mContext, mDataShop.get(position).getStoreId(), mDataShop.get(position).getStoreName()
                        , mDataShop.get(position).getKind(), mDataShop.get(position).getStoreCoupon(), mDataShop.get(position).getIsCoupon(),mDataShop.get(position).getCompanyName());
            }
        });
        adapterShop.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.constraintLayout_coupon) {
                    if ("0".equals(mDataShop.get(position).getIsCoupon())) {
                        showReceiveCoupon(position);
                    }
                }else if (view.getId() == R.id.tv_reserve){
                    UseCouponActivity.start(mContext, 1, mDataShop.get(position).getKind(), mDataShop.get(position).getStoreId(),mDataShop.get(position).getCompanyName(),mDataShop.get(position).getStoreName());
                }
            }
        });

        adapterUser = new FindPeoplePlayAdapter(mDataUser,HomeSearchActivity.this);
        adapterUser.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserDataActivity.start(HomeSearchActivity.this, mDataUser.get(position).getUid());
            }
        });

        notDataView = getLayoutInflater().inflate(R.layout.layout_empty_view, (ViewGroup) recyclerView.getParent(), false);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        errorView = getLayoutInflater().inflate(R.layout.layout_error_view, (ViewGroup) recyclerView.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    CustomDialog customDialog1;
    private void showReceiveCoupon(int position) {
        customDialog1 = CustomDialog.show(HomeSearchActivity.this, R.layout.dialog_receive_coupon, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
                TextView textViewMoney = rootView.findViewById(R.id.textViewMoney);
                textViewLingqu = rootView.findViewById(R.id.textViewLingqu);
                TextView textViewTime = rootView.findViewById(R.id.textViewTime);
                TextView textViewNumb = rootView.findViewById(R.id.textViewNumb);
                TextView textViewYuding = rootView.findViewById(R.id.textViewYuding);
                textViewMoney.setText(mDataShop.get(position).getStoreCoupon().getCouponAmountX() + "元");
                textViewTime.setText("有效日期："+ mDataShop.get(position).getStoreCoupon().getBeginTime()+"~"+ mDataShop.get(position).getStoreCoupon().getEndTime());
                textViewNumb.setText("剩余"+ mDataShop.get(position).getStoreCoupon().getHavaRecevied()+"/"+ mDataShop.get(position).getStoreCoupon().getAvailableNo()+"张");
                textViewLingqu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lingquCoupon(position);
                    }
                });
                textViewYuding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog1.doDismiss();
                        UseCouponActivity.start(HomeSearchActivity.this, 1, mDataShop.get(position).getKind(), mDataShop.get(position).getStoreId(),mDataShop.get(position).getCompanyName(),mDataShop.get(position).getStoreName());
                    }
                });

            }
        }).setCanCancel(true);
    }

    private void lingquCoupon(int mPosition) {
        showWaitDialog(HomeSearchActivity.this);
        MQApi.userReceiveShopCoupon(userBean.getUId(), mDataShop.get(mPosition).getStoreCoupon().getStoreIdX(), mDataShop.get(mPosition).getStoreCoupon().getIdX(), mDataShop.get(mPosition).getStoreCoupon().getCouponAmountX(), new SimpleCallBack<String>() {
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
                if (myApiResult.isOk()){
                    textViewLingqu.setText("已领取");
                    mDataShop.get(mPosition).setIsCoupon("1");
                    adapterShop.notifyItemChanged(mPosition);
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    public void initData() {
        if (!StringUtil.isNotNull(etContent.getText().toString())){
            RxToast.showToast("搜索内容不能为空");
            return;
        }
        showWaitDialog(HomeSearchActivity.this);
        if (type == 1) {
            recyclerView.setAdapter(adapterShop);
        } else {
            recyclerView.setAdapter(adapterUser);
        }
        MQApi.searchHomePage(userBean.getUId(), type, etContent.getText().toString(),
                RxSPTool.getContent(HomeSearchActivity.this,Constant.KEY_LONGITUDE),
                RxSPTool.getContent(HomeSearchActivity.this,Constant.KEY_LATITUDE),
                new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        dissmissWaitDialog();
                        RxToast.showToast(e.toString());
                        if (type == 1) {
                            setView(adapterShop,errorView);
//                            adapterShop.setEmptyView(errorView);
                        } else {
                            setView(adapterUser,errorView);
//                            adapterUser.setEmptyView(errorView);
                        }
                    }

                    @Override
                    public void onSuccess(String s) {
                        dissmissWaitDialog();
                        if (type == 1){
                            MyApiResult<List<ShopInfoBean>> myApiResult =
                                    new Gson().fromJson(s, new TypeToken<MyApiResult<List<ShopInfoBean>>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<ShopInfoBean>>>(){});
                            if (myApiResult.isOk()) {
                                mDataShop.clear();
                                for (int i = 0; i < myApiResult.getData().size(); i++) {
                                    mDataShop.add(myApiResult.getData().get(i));
                                }
                                adapterShop.notifyDataSetChanged();
                                if (mDataShop.size() == 0){
                                    setView(adapterShop,notDataView);
//                                    adapterShop.setEmptyView(notDataView);
                                }
                            } else {
                                setView(adapterShop,errorView);
//                                adapterShop.setEmptyView(errorView);
                                RxToast.showToast(myApiResult.getMsg());
                            }
                        }else {
                            MyApiResult<List<MeetPeopleBean>> myApiResult =
                                    new Gson().fromJson(s, new TypeToken<MyApiResult<List<MeetPeopleBean>>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<MeetPeopleBean>>>(){});
                            if (myApiResult.isOk()) {
                                mDataUser.clear();
                                for (int i = 0; i < myApiResult.getData().size(); i++) {
                                    mDataUser.add(myApiResult.getData().get(i));
                                }
                                adapterUser.notifyDataSetChanged();
                                if (mDataUser.size() == 0){
                                    setView(adapterUser,notDataView);
//                                    adapterUser.setEmptyView(notDataView);
                                }
                            } else {
                                setView(adapterUser,errorView);
//                                adapterUser.setEmptyView(errorView);
                                RxToast.showToast(myApiResult.getMsg());
                            }
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public void setView(BaseQuickAdapter adapter,View view){
        if (view != null){
            ViewGroup parent = (ViewGroup)view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                parent.removeView(view);
            }
        }
        adapter.setEmptyView(view);
    }

    @OnClick({R.id.iv_back, R.id.tv_search, R.id.radioButtonShop, R.id.radioButtonUser})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                initData();
                KeyboardUtils.hideSoftInput(HomeSearchActivity.this);
                break;
            case R.id.radioButtonShop:
                type = 1;
                break;
            case R.id.radioButtonUser:
                type = 2;
                break;
        }
    }
}

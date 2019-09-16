package com.andy.meetquickly.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.user.wallte.WallteActivity;
import com.andy.meetquickly.adapter.WelfareCenterAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.CouponBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.IFwLogCallback;

public class WelfareCenterActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    List<CouponBean> mData = new ArrayList<>();
    private WelfareCenterAdapter adapter;
    UserBean userBean;
    private View notDataView;
    private View errorView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_welfare_center;
    }

    @Override
    public void initView() {
        setBarTitle("福利中心");
        userBean = UserCache.getInstance().getUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new WelfareCenterAdapter(mData, mContext);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.imageViewIsSelect){
                    if (!"1".equals(mData.get(position).getIfReceive())){
                        receiveWelfare(position);
                    }
                }
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
        initData();
    }

    private void receiveWelfare(int position) {
        showWaitDialog(WelfareCenterActivity.this);
        MQApi.receiveCoupon(userBean.getUId(), mData.get(position).getId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult>(){}.getType());
                if (myApiResult.isOk()){
                    mData.get(position).setIfReceive("1");
                    adapter.notifyItemChanged(position);
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    public void initData() {
        showWaitDialog(WelfareCenterActivity.this);
        MQApi.getWelfareCenterList(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                adapter.setEmptyView(errorView);
                RxToast.showToast(e.toString());
            }
            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<CouponBean>> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<List<CouponBean>>>() {
                }.getType());
                if (myApiResult.isOk()) {
                    if (null != myApiResult.getData()) {
                        mData.clear();
                        for (int i = 0; i < myApiResult.getData().size(); i++) {
                            mData.add(myApiResult.getData().get(i));
                        }
                        adapter.notifyDataSetChanged();
                        if (mData.size() == 0) {
                            adapter.setEmptyView(notDataView);
                        }
                    }
                } else {
                    adapter.setEmptyView(errorView);
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, WelfareCenterActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}

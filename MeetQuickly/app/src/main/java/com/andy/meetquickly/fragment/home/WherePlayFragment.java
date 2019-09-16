package com.andy.meetquickly.fragment.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.appointment.UseCouponActivity;
import com.andy.meetquickly.activity.home.MeetPeopleActivity;
import com.andy.meetquickly.activity.home.ShopDetailActivity;
import com.andy.meetquickly.adapter.WherePlayAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.ShopInfoBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.receiveCouponMessage;
import com.andy.meetquickly.message.screenShopMessage;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class WherePlayFragment extends BaseLazyFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_POSITION = "arf-position";
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    Unbinder unbinder;
    // TODO: Customize parameters
    private int mPosition = 0;
    private int mPage = 1;    //当前页面
    private int mKind = 0;    //所属类型 0:全部，1:商务KTV，2:酒吧，3:量贩KTV，4:清吧
    private int mDiscountLeft = 0; //最小折扣
    private int mDiscountRight = 10;  //最大折扣
    private int mActivityName = 0;  //活动 0全部 1有 2无
    private List<ShopInfoBean> mData = new ArrayList<>();
    private WherePlayAdapter adapter;
    private UserBean userBean;
    private String storeId = "";
    private TextView textViewLingqu;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WherePlayFragment() {
    }

    @Override
    public void fetchData() {
        userBean = UserCache.getInstance().getUser();
        EventBus.getDefault().register(this);
        refreshData();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(receiveCouponMessage message) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getStoreId().equals(message.getMessage())) {
                mData.get(i).setIsCoupon("1");
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void initData() {
        String uID = "";
        if (null != userBean) {
            uID = userBean.getUId();
        }
        MQApi.getShopsList(uID, storeId, mPage, mKind, mDiscountLeft, mDiscountRight, mActivityName,
                RxSPTool.getContent(WherePlayFragment.this.getContext(), Constant.KEY_LONGITUDE),
                RxSPTool.getContent(WherePlayFragment.this.getContext(), Constant.KEY_LATITUDE), new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        RxToast.showToast(e.toString());
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(String s) {
//                MyApiResult<List<ShopInfoBean>> myApiResult = JSON.parseObject(s, new MyApiResult<List<ShopInfoBean>>().getClass());
                        MyApiResult<List<ShopInfoBean>> myApiResult =
                                new Gson().fromJson(s, new TypeToken<MyApiResult<List<ShopInfoBean>>>() {
                                }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<ShopInfoBean>>>(){});
                        if (myApiResult.isOk()) {
                            if (mPage == 1) {
                                mData.clear();
                            }
                            swipeRefreshLayout.setRefreshing(false);
                            if (myApiResult.isIsLastPage()) {
                                adapter.setEnableLoadMore(false);
                            }else {
                                adapter.setEnableLoadMore(true);
                                adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                    @Override
                                    public void onLoadMoreRequested() {
                                        mPage = mPage + 1;
                                        initData();
                                    }
                                }, recyclerView);
                            }
                            adapter.loadMoreComplete();
                            for (int i = 0; i < myApiResult.getData().size(); i++) {
                                mData.add(myApiResult.getData().get(i));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter.loadMoreFail();
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }

    @Override
    public void doItEvery() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static WherePlayFragment newInstance(int columnCount, int position) {
        WherePlayFragment fragment = new WherePlayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        // Set the adapter
        view.setTag(mPosition);
        unbinder = ButterKnife.bind(this, view);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new WherePlayAdapter(mData, context);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShopDetailActivity.start(WherePlayFragment.this.getContext(), mData.get(position).getStoreId(), mData.get(position).getStoreName()
                        , mData.get(position).getKind(), mData.get(position).getStoreCoupon(), mData.get(position).getIsCoupon(),mData.get(position).getCompanyName());
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.constraintLayout_coupon) {
                    if ("0".equals(mData.get(position).getIsCoupon())) {
                        showReceiveCoupon(position);
                    }
                } else if (view.getId() == R.id.tv_reserve) {
                    UseCouponActivity.start(WherePlayFragment.this.getContext(), 1, mData.get(position).getKind(), mData.get(position).getStoreId(),mData.get(position).getCompanyName(),mData.get(position).getStoreName());
                } else if (view.getId() == R.id.iv_more) {
                    MeetPeopleActivity.start(WherePlayFragment.this.getContext(), mData.get(position).getStoreId());
                }
            }
        });
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPage = mPage + 1;
                initData();
            }
        }, recyclerView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void refreshData() {
        mPage = 1;
        initData();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(screenShopMessage message) {
        mKind = message.getmKind();
        mDiscountLeft = message.getmDiscountLeft();
        mDiscountRight = message.getmDiscountRight();
        mActivityName = message.getmActivityName();
        storeId = message.getmStoreId();
        initData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    CustomDialog customDialog1;

    private void showReceiveCoupon(int position) {
        customDialog1 = CustomDialog.show(WherePlayFragment.this.getContext(), R.layout.dialog_receive_coupon, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
                TextView textViewMoney = rootView.findViewById(R.id.textViewMoney);
                textViewLingqu = rootView.findViewById(R.id.textViewLingqu);
                TextView textViewTime = rootView.findViewById(R.id.textViewTime);
                TextView textViewNumb = rootView.findViewById(R.id.textViewNumb);
                TextView textViewYuding = rootView.findViewById(R.id.textViewYuding);
                TextView textViewBiLi = rootView.findViewById(R.id.textViewBiLi);
                textViewBiLi.setText("1.此代金券按酒水消费金额的10%进行抵现，最高可抵现"+mData.get(position).getStoreCoupon().getCouponAmountX()+"元");
                textViewMoney.setText(mData.get(position).getStoreCoupon().getCouponAmountX() + "元");
                textViewTime.setText("有效日期：" + mData.get(position).getStoreCoupon().getBeginTime() + "~" + mData.get(position).getStoreCoupon().getEndTime());
                textViewNumb.setText("剩余" + mData.get(position).getStoreCoupon().getHavaRecevied() + "/" + mData.get(position).getStoreCoupon().getAvailableNo() + "张");
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
                        UseCouponActivity.start(WherePlayFragment.this.getContext(), 1, mData.get(position).getKind(), mData.get(position).getStoreId(),mData.get(position).getCompanyName(),mData.get(position).getStoreName());
                    }
                });

            }
        }).setCanCancel(true);
    }

    private void lingquCoupon(int mPosition) {
        showWaitDialog(WherePlayFragment.this.getActivity());
        MQApi.userReceiveShopCoupon(userBean.getUId(), mData.get(mPosition).getStoreCoupon().getStoreIdX(), mData.get(mPosition).getStoreCoupon().getIdX(), mData.get(mPosition).getStoreCoupon().getCouponAmountX(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                        }.getType());
                if (myApiResult.isOk()) {
                    textViewLingqu.setText("已领取");
                    mData.get(mPosition).setIsCoupon("1");
                    adapter.notifyItemChanged(mPosition);
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

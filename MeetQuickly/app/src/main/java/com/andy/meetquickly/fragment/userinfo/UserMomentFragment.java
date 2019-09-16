package com.andy.meetquickly.fragment.userinfo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.find.MomentDetailActivity;
import com.andy.meetquickly.activity.find.SimpleVideoPlayActivity;
import com.andy.meetquickly.adapter.ReportAdapter;
import com.andy.meetquickly.adapter.UserMomentAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.MomnetListBean;
import com.andy.meetquickly.bean.ReportBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.fragment.find.NearFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.shinichi.library.ImagePreview;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserMomentFragment extends BaseLazyFragment {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private static final String U_ID = "uId";
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String uId = "";
    private int mPage = 1;
    private List<MomnetListBean> mData = new ArrayList<>();
    private UserMomentAdapter adapter;
    private UserBean userBean;

    public UserMomentFragment() {
        // Required empty public constructor
    }

    @Override
    public void fetchData() {
        initData();
    }

    private void initData() {
        String Uid;
        if (null != userBean) {
            Uid = userBean.getUId();
        } else {
            Uid = "";
        }
        MQApi.getUserDynamicList(uId, mPage, Uid, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                adapter.loadMoreFail();
                swipeRefreshLayout.setRefreshing(false);
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<MomnetListBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<MomnetListBean>>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<MomnetListBean>>>(){});
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
                    swipeRefreshLayout.setRefreshing(false);
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    public void doItEvery() {

    }

    // TODO: Customize parameter initialization
    public static UserMomentFragment newInstance(String uId) {
        UserMomentFragment fragment = new UserMomentFragment();
        Bundle args = new Bundle();
        args.putString(U_ID, uId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uId = getArguments().getString(U_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_moment, container, false);
        view.setTag(0);
        unbinder = ButterKnife.bind(this, view);
        userBean = UserCache.getInstance().getUser();
        initView();
        return view;
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(UserMomentFragment.this.getContext()));
        adapter = new UserMomentAdapter(mData, UserMomentFragment.this.getContext(), 1);
        //adapter设置点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MomentDetailActivity.start(UserMomentFragment.this.getContext(), mData.get(position).getId());
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_more) {
                    showMoreDialog(position);
                } else if (view.getId() == R.id.iv_parse) {
                    if (!mData.get(position).getIsThumbsUp().equals("1")) {
                        parse(position);
                    }
                } else if (view.getId() == R.id.relativeLayoutOne) {
                    if ("1".equals(mData.get(position).getType())) {
                        List<String> lists = new ArrayList<>();
                        for (int i = 0; i < mData.get(position).getUserDynamicFiles().size(); i++) {
                            lists.add(mData.get(position).getUserDynamicFiles().get(i).getThumb());
                        }
                        ImagePreview.getInstance()
                                .setContext(UserMomentFragment.this.getContext())
                                .setIndex(0)
                                .setImageList(lists)
                                .setEnableDragClose(true)
                                .setErrorPlaceHolder(R.drawable.load_failed)
                                .start();
                    } else {
                        SimpleVideoPlayActivity.start(UserMomentFragment.this.getContext(), mData.get(position).getUserDynamicFiles().get(0).getUrl());
                    }
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
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    public void refreshData() {
        mPage = 1;
        initData();
    }

    private void parse(int position) {
        String Uid;
        if (null != userBean) {
            Uid = userBean.getUId();
        } else {
            Uid = "";
        }
        MQApi.addUserThumbsUp(Uid, mData.get(position).getId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult>() {
                        }.getType());
//                        JSON.parseObject(s,new MyApiResult<String>().getClass());
                if (myApiResult.isOk()) {
                    mData.get(position).setIsThumbsUp("1");
                    String praiseNum;
                    try {
                        praiseNum = String.valueOf(Integer.parseInt(mData.get(position).getPraiseNum()) + 1);
                    } catch (Exception e) {
                        praiseNum = "1";
                    }
                    mData.get(position).setPraiseNum(praiseNum);
                    adapter.notifyItemChanged(position);
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    CustomDialog customDialog;

    private void showMoreDialog(int position) {
        customDialog = CustomDialog.show(UserMomentFragment.this.getActivity(), R.layout.dialog_add_reserve, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
//                        //绑定布局
//                        ImageView btnOk = rootView.findViewById(R.id.btn_ok);
                TextView tv_one = rootView.findViewById(R.id.tv_one);
                TextView tv_two = rootView.findViewById(R.id.tv_two);
                TextView tv_cancel = rootView.findViewById(R.id.tv_cancel);
                tv_one.setText("屏蔽此条动态");
                tv_two.setText("举报此条动态");
                tv_one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                        shieldMoment(position);
                    }
                });
                tv_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                        getReportContent(position);
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                    }
                });


            }
        }).setCanCancel(true);
    }

    private List<ReportBean> reportBeans = new ArrayList<>();

    //获取举报信息
    private void getReportContent(int position) {
        MQApi.getReportContentList("report_content", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<ReportBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<ReportBean>>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<ReportBean>>>() {});
                if (myApiResult.isOk()) {
                    reportBeans.clear();
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        reportBeans.add(myApiResult.getData().get(i));
                    }
                    showReportDialog(position);
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private String reportContent = "";

    private void showReportDialog(int position) {
        reportContent = "";
        //活动
        customDialog = CustomDialog.show(UserMomentFragment.this.getContext(), R.layout.dialog_report, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
                ////绑定布局
                //ImageView btnOk = rootView.findViewById(R.id.btn_ok);
                RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(UserMomentFragment.this.getContext());
                ReportAdapter recycleAdapter = new ReportAdapter(reportBeans);
                recycleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        reportContent = reportBeans.get(position).getTypeName();
                        recycleAdapter.setSelect(reportContent);
                        recycleAdapter.notifyDataSetChanged();
                    }
                });
                //设置布局管理器
                recyclerView.setLayoutManager(layoutManager);
                //设置为垂直布局，这也是默认的
                layoutManager.setOrientation(OrientationHelper.VERTICAL);
                //设置Adapter
                recyclerView.setAdapter(recycleAdapter);
                TextView tv_cancel = rootView.findViewById(R.id.tv_cancel);
                TextView tv_sure = rootView.findViewById(R.id.tv_sure);
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                    }
                });
                tv_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (reportContent.isEmpty()) {
                            RxToast.showToast("请选择举报内容");
                        } else {
                            reportMoment(position);
                            customDialog.doDismiss();
                        }
                    }
                });
            }
        });
    }

    private void reportMoment(int position) {
        String Uid;
        if (null != userBean) {
            Uid = userBean.getUId();
        } else {
            Uid = "";
        }
        MQApi.addReportDynamic(Uid, mData.get(position).getId(), reportContent, false, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult myApiResult = JSON.parseObject(s, new MyApiResult().getClass());
                if (myApiResult.isOk()) {
                    RxToast.showToast("投诉举报成功");
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    //屏蔽动态
    private void shieldMoment(int position) {
        showWaitDialog(UserMomentFragment.this.getActivity());
        MQApi.addDynamicShield(userBean.getUId(), mData.get(position).getUId(), new SimpleCallBack<String>() {
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
                    RxToast.showToast("屏蔽动态成功");
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

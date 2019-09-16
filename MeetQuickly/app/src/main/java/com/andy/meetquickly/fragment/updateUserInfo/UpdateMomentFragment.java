package com.andy.meetquickly.fragment.updateUserInfo;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.find.MomentDetailActivity;
import com.andy.meetquickly.activity.find.PublishMomentActivity;
import com.andy.meetquickly.activity.find.SimpleVideoPlayActivity;
import com.andy.meetquickly.adapter.UserMomentAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.MomnetListBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.fragment.userinfo.UserMomentFragment;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.SelectDialog;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cc.shinichi.library.ImagePreview;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateMomentFragment extends BaseLazyFragment {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private int mPage = 1;
    private List<MomnetListBean> mData = new ArrayList<>();
    private UserMomentAdapter adapter;
    private UserBean userBean;
    private View notDataView;
    private View errorView;

    public UpdateMomentFragment() {
        // Required empty public constructor
    }

    @Override
    public void fetchData() {
        initData();
    }

    private void initData() {
        MQApi.getUserDynamicList(userBean.getUId(), mPage, "", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
                adapter.loadMoreFail();
                swipeRefreshLayout.setRefreshing(false);
                adapter.setEmptyView(errorView);
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<MomnetListBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<MomnetListBean>>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<MomnetListBean>>>() {
//                });
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
                    if (mData.size() == 0) {
                        adapter.setEmptyView(notDataView);
                    }
                } else {
                    adapter.loadMoreFail();
                    swipeRefreshLayout.setRefreshing(false);
                    RxToast.showToast(myApiResult.getMsg());
                    adapter.setEmptyView(errorView);
                }
            }
        });
    }

    @Override
    public void doItEvery() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_moment, container, false);
        view.setTag(1);
        userBean = UserCache.getInstance().getUser();
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(UpdateMomentFragment.this.getContext()));
        adapter = new UserMomentAdapter(mData, UpdateMomentFragment.this.getContext(), 2);
        //adapter设置点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MomentDetailActivity.start(UpdateMomentFragment.this.getContext(), mData.get(position).getId());
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_parse) {
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
                                .setContext(UpdateMomentFragment.this.getContext())
                                .setIndex(0)
                                .setImageList(lists)
                                .setEnableDragClose(true)
                                .setErrorPlaceHolder(R.drawable.load_failed)
                                .start();
                    } else {
                        SimpleVideoPlayActivity.start(UpdateMomentFragment.this.getContext(), mData.get(position).getUserDynamicFiles().get(0).getUrl());
                    }
                } else {
                    showDeleteDialog(position);
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

    private void showDeleteDialog(int position) {
        SelectDialog.show(UpdateMomentFragment.this.getContext(), "提示", "是否删除此条动态", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMoment(position);
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCanCancel(true);
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

    private void deleteMoment(int position) {
        showWaitDialog(UpdateMomentFragment.this.getActivity());
        MQApi.delUserDynamic(mData.get(position).getId(), new SimpleCallBack<String>() {
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
//                        JSON.parseObject(s,new MyApiResult<String>().getClass());
                if (myApiResult.isOk()) {
                    mData.remove(position);
                    adapter.notifyDataSetChanged();
                    if (mData.size() == 0) {
                        adapter.setEmptyView(notDataView);
                    }
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

    @OnClick(R.id.ll_save)
    public void onViewClicked() {

        PublishMomentActivity.start(UpdateMomentFragment.this.getContext());

    }
}

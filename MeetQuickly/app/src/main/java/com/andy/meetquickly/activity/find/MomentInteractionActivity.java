package com.andy.meetquickly.activity.find;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.MomentInteractionAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.CommentBean;
import com.andy.meetquickly.bean.MomnetListBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.KeyboardUtils;
import com.andy.meetquickly.utils.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener;
import com.kongzue.dialog.v2.InputDialog;
import com.luck.picture.lib.PictureSelector;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.shinichi.library.ImagePreview;

import static com.kongzue.dialog.v2.DialogSettings.STYLE_KONGZUE;
import static com.kongzue.dialog.v2.DialogSettings.TYPE_IOS;

public class MomentInteractionActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private UserBean userBean;
    List<MomnetListBean> mData = new ArrayList<>();
    private MomentInteractionAdapter adapter;
    private int mPage = 1;
    private View notDataView;
    private View errorView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_moment_interaction;
    }

    @Override
    public void initView() {
        setBarTitle("互动");
        userBean = UserCache.getInstance().getUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MomentInteractionAdapter(mData, mContext);
        //adapter设置点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MomentDetailActivity.start(MomentInteractionActivity.this, mData.get(position).getId());
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.cardView) {
                    if ("0".equals(mData.get(position).getType())) {
                        MomentDetailActivity.start(MomentInteractionActivity.this, mData.get(position).getId());
                    } else if ("1".equals(mData.get(position).getType())) {
                        List<String> lists = new ArrayList<>();
                        for (int i = 0; i < mData.get(position).getUserDynamicFiles().size(); i++) {
                            lists.add(mData.get(position).getUserDynamicFiles().get(i).getThumb());
                        }
                        ImagePreview.getInstance()
                                .setContext(MomentInteractionActivity.this)
                                .setIndex(0)
                                .setImageList(lists)
                                .setEnableDragClose(true)
                                .setErrorPlaceHolder(R.drawable.load_failed)
                                .start();
                    } else if ("2".equals(mData.get(position).getType())) {
                        SimpleVideoPlayActivity.start(mContext,mData.get(position).getUserDynamicFiles().get(0).getUrl());
//                        PictureSelector.create((Activity) mContext).externalPictureVideo(mData.get(position).getUserDynamicFiles().get(position).getUrl());
                    }
                } else {
                    showCommentDialod(position);
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
        initData();
    }

    public void refreshData() {
        mPage = 1;
        initData();
    }

    private void showCommentDialod(int position) {
        InputDialog.show(MomentInteractionActivity.this, "回复评论", "回复" + mData.get(position).getNickName(), "确定", new InputDialogOkButtonClickListener() {
            @Override
            public void onClick(Dialog dialog, String inputText) {
                if (StringUtil.isNotNull(inputText)) {
                    commentMessage(inputText, position, dialog);
                } else {
                    RxToast.showToast("评论内容不能为空");
                }
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setDialogStyle(STYLE_KONGZUE);
    }

    private void commentMessage(String inputText, int position, Dialog dialog) {
        MQApi.addUserComment(userBean.getUId(), mData.get(position).getId(), inputText, mData.get(position).getUId()
                , new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        RxToast.showToast(e.toString());
                    }

                    @Override
                    public void onSuccess(String s) {
                        MyApiResult<CommentBean> myApiResult =
                                new Gson().fromJson(s, new TypeToken<MyApiResult<CommentBean>>() {
                                }.getType());
//                                JSON.parseObject(s,new TypeReference<MyApiResult<CommentBean>>(){});
                        if (myApiResult.isOk()) {
                            CommentBean commentBean = myApiResult.getData();
                            RxToast.showToast("评论成功");
                            if (null != dialog && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            KeyboardUtils.hideSoftInput(MomentInteractionActivity.this);
                        } else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });
    }

    @Override
    public void initData() {
        showWaitDialog(MomentInteractionActivity.this);
        MQApi.getUnreadMessageList(userBean.getUId(), mPage,
                RxSPTool.getContent(MomentInteractionActivity.this, Constant.KEY_LONGITUDE),
                RxSPTool.getContent(MomentInteractionActivity.this, Constant.KEY_LATITUDE), new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        dissmissWaitDialog();
                        adapter.loadMoreFail();
                        swipeRefreshLayout.setRefreshing(false);
                        RxToast.showToast(e.toString());
                        adapter.setEmptyView(errorView);
                    }

                    @Override
                    public void onSuccess(String s) {
                        dissmissWaitDialog();
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

    public static void start(Context context) {
        Intent starter = new Intent(context, MomentInteractionActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

package com.andy.meetquickly.fragment.updateUserInfo;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.find.VideoLocalAllActivity;
import com.andy.meetquickly.activity.find.VideoPlayActivity;
import com.andy.meetquickly.activity.find.VideoRecorderActivity;
import com.andy.meetquickly.activity.message.FollowListActivity;
import com.andy.meetquickly.adapter.UserVideoAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserMediaBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.message.updateVideoMessage;
import com.andy.meetquickly.utils.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.listener.OnMenuItemClickListener;
import com.kongzue.dialog.v2.BottomMenu;
import com.kongzue.dialog.v2.SelectDialog;
import com.vondear.rxtool.view.RxToast;
import com.wode369.videocroplibrary.features.trim.VideoTrimmerActivity;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateVideoFragment extends BaseLazyFragment {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private UserBean userBean;
    private int mPage = 1;
    private String uId;

    private List<UserMediaBean> mData = new ArrayList<>();
    private UserVideoAdapter adapter;

    public UpdateVideoFragment() {
        // Required empty public constructor
    }

    public static UpdateVideoFragment newInstance(String uId) {
        UpdateVideoFragment fragment = new UpdateVideoFragment();
        Bundle args = new Bundle();
        args.putString("uId", uId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userBean = UserCache.getInstance().getUser();
        if (getArguments() != null) {
            uId = getArguments().getString("uId");
        }else {
            uId = userBean.getUId();
        }
    }

    @Override
    public void fetchData() {
    }


    private void initData() {
        showWaitDialog(UpdateVideoFragment.this.getActivity());
        MQApi.getUserImageOrVideoList(uId, 2, mPage, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                adapter.loadMoreFail();
                swipeRefreshLayout.setRefreshing(false);
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<UserMediaBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<UserMediaBean>>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<UserMediaBean>>>(){});
                if (myApiResult.isOk()) {
                    if (mPage == 1) {
                        mData.clear();
                        mData.add(new UserMediaBean());
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
        initData();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_video, container, false);
        view.setTag(3);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(updateVideoMessage message) {
        if (message.getType() == 0) {
            videoCrop(message.getPath());
        } else {
            updateUserVideo(message.getPath());
        }
    }

    private void updateUserVideo(String path) {
        if (!StringUtil.isNotNull(path)) {
            RxToast.showToast("请选择视频");
            return;
        }
        File file = new File(path);
        showWaitDialog(UpdateVideoFragment.this.getActivity(), "正在上传视频");
        MQApi.updateUserImageOrVideoList(uId, 2, file, "video", new ProgressResponseCallBack() {
            @Override
            public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {

            }
        }, new SimpleCallBack<String>() {
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
                    refreshData();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }


    public static final int VIDEO_TRIM_REQUEST_CODE = 23;
    private static final String VIDEO_PATH_KEY = "video-file-path";

    //得到视频路径，new 出视频file，我们就可以对视频进行剪裁处理了（文字，滤镜...）
    //这里推荐一个开源框架，Android-Video-Trimmer： https://github.com/iknow4/Android-Video-Trimmer 还不错。
    // 缺点是：使用ffmpeg进行视频裁剪。会让你的app增大许多,20-30M
    private void videoCrop(String videoPath) {

        if (!TextUtils.isEmpty(videoPath)) {
            Bundle bundle = new Bundle();
            bundle.putString(VIDEO_PATH_KEY, videoPath);

            Intent intent = new Intent(UpdateVideoFragment.this.getActivity(), VideoTrimmerActivity.class);
            intent.putExtras(bundle);
            UpdateVideoFragment.this.getActivity().startActivityForResult(intent, VIDEO_TRIM_REQUEST_CODE);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        userBean = UserCache.getInstance().getUser();
        recyclerView.setLayoutManager(new GridLayoutManager(UpdateVideoFragment.this.getContext(), 2));
        adapter = new UserVideoAdapter(mData, 2, UpdateVideoFragment.this.getContext());
        //adapter设置点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (StringUtil.isNotNull(mData.get(position).getId())) {
                    VideoPlayActivity.start(UpdateVideoFragment.this.getContext(), mData.get(position));
                } else {
                    showVideoDialog();
                }
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.imageViewDelete){
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
        refreshData();
    }

    private void showDeleteDialog(int position) {
        SelectDialog.show(UpdateVideoFragment.this.getContext(), "提示", "是否删除此视频", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteVideo(position);
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCanCancel(true);
    }

    private void deleteVideo(int position) {
        showWaitDialog(UpdateVideoFragment.this.getActivity(),"正在删除图片...");
        MQApi.delUserImageOrVideoList(mData.get(position).getId(), new SimpleCallBack<String>() {
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
//                        JSON.parseObject(s,new MyApiResult<String>().getClass());
                if (myApiResult.isOk()){
                    mData.remove(position);
                    adapter.notifyDataSetChanged();
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    public void refreshData() {
        mPage = 1;
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private final static int VIDEO_REQUESTCODE = 22;

    private void showVideoDialog() {
        List<String> list = new ArrayList<>();
        list.add("拍摄");
        list.add("手机相册选择");
        BottomMenu.show((AppCompatActivity) UpdateVideoFragment.this.getActivity(), list, new OnMenuItemClickListener() {
            @Override
            public void onClick(String text, int index) {
                if (index == 0) {
                    //拍摄
                    VideoRecorderActivity.start(UpdateVideoFragment.this.getActivity());
                } else {
                    //选择
                    Intent intent = new Intent(UpdateVideoFragment.this.getActivity(), VideoLocalAllActivity.class);
                    UpdateVideoFragment.this.getActivity().startActivityIfNeeded(intent, VIDEO_REQUESTCODE);
                }
            }
        }, true);
    }


}

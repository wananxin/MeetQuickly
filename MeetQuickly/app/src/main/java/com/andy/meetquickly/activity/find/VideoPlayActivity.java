package com.andy.meetquickly.activity.find;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.adapter.ReportAdapter;
import com.andy.meetquickly.adapter.VideoCommentAdapter;
import com.andy.meetquickly.base.BaseInvasionActivity;
import com.andy.meetquickly.bean.ReportBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserMediaBean;
import com.andy.meetquickly.bean.VideoCommentBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.andy.meetquickly.utils.JZMediaExo;
import com.andy.meetquickly.utils.StringUtil;
import com.andy.meetquickly.view.MaxHeightRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.kongzue.dialog.v2.SelectDialog;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZMediaInterface;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoPlayActivity extends BaseInvasionActivity {

    @BindView(R.id.jz_video)
    JzvdStd jzVideo;
    @BindView(R.id.imageViewPrase)
    ImageView imageViewPrase;
    @BindView(R.id.imageViewComment)
    ImageView imageViewComment;
    @BindView(R.id.textViewComment)
    TextView textViewComment;
    @BindView(R.id.textViewPrase)
    TextView textViewPrase;
    private UserMediaBean userMediaBean;
    private UserBean userBean;
    private Dialog mCommentDialog;
    private int mPage = 1;
    private List<VideoCommentBean> mData = new ArrayList<>();
    private VideoCommentAdapter adapter;
    private RecyclerView recyclerView;
    private TextView textViewNumb;
    private ImageView imageViewDelete;
    private EditText editTextContent;
    private TextView textViewSend;

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_play;
    }

    @Override
    public void initView() {
        userMediaBean = (UserMediaBean) this.getIntent().getSerializableExtra("userMediaBean");
        userBean = UserCache.getInstance().getUser();
        jzVideo.setUp(userMediaBean.getUrl(), "", JzvdStd.SCREEN_NORMAL, JZMediaExo.class);
        textViewComment.setText(userMediaBean.getCommentNo());
        textViewPrase.setText(userMediaBean.getThumbsUpNo());
        if ("1".equals(userMediaBean.getIsThumbsUp())) {
            imageViewPrase.setImageResource(R.mipmap.ic_praise_select);
        } else {
            imageViewPrase.setImageResource(R.mipmap.ic_prase_white);
        }
        ImageLoaderUtil.getInstance().loadImage(VideoPlayActivity.this, userMediaBean.getUrl(), jzVideo.thumbImageView);
        jzVideo.startVideo();
        initData();
    }

    @Override
    public void initData() {
        if (!userBean.getUId().equals(userMediaBean.getUId())){
            MQApi.addUserVideoPlayNo(userMediaBean.getId(), new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {

                }

                @Override
                public void onSuccess(String s) {

                }
            });
        }
    }

    public static void start(Context context, UserMediaBean userMediaBean) {
        Intent starter = new Intent(context, VideoPlayActivity.class);
        starter.putExtra("userMediaBean", userMediaBean);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initView();
    }

    @OnClick({R.id.iv_back, R.id.iv_more, R.id.imageViewPrase, R.id.textViewPrase, R.id.imageViewComment, R.id.textViewComment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:
                showSelectDialog();
                break;
            case R.id.imageViewPrase:
                thumbsVideo();
                break;
            case R.id.textViewPrase:
                thumbsVideo();
                break;
            case R.id.imageViewComment:
                getCommentData();
                break;
            case R.id.textViewComment:
                getCommentData();
                break;
        }
    }

    private void showSelectDialog() {
        SelectDialog.show(mContext, "举报", "是否要举报此条动态", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getReportContent();
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCanCancel(true);
    }

    private List<ReportBean> reportBeans = new ArrayList<>();

    //获取举报信息
    private void getReportContent() {
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
                    showReportDialog();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    String reportContent;

    CustomDialog customDialog;
    private void showReportDialog() {
        reportContent = "";
        //活动
        customDialog = CustomDialog.show(VideoPlayActivity.this, R.layout.dialog_report, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
                ////绑定布局
                //ImageView btnOk = rootView.findViewById(R.id.btn_ok);
                RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(VideoPlayActivity.this);
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
                            reportMoment();
                            customDialog.doDismiss();
                        }
                    }
                });
            }
        });
    }

    private void reportMoment() {
        String Uid;
        if (null != userBean) {
            Uid = userBean.getUId();
        } else {
            Uid = "";
        }
        MQApi.addReportDynamic(Uid, userMediaBean.getId(), reportContent, true, new SimpleCallBack<String>() {
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

    private void getCommentData() {
        showWaitDialog(VideoPlayActivity.this);
        MQApi.getVideoCommentsList(userBean.getUId(), userMediaBean.getId(), mPage, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<VideoCommentBean>> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<List<VideoCommentBean>>>() {
                }.getType());
                if (myApiResult.isOk()) {
                    if (null != myApiResult.getData()) {
                        mData = myApiResult.getData();
                    }
                    showCommentDialog();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }

            }
        });
    }

    private void showCommentDialog() {
        if (null == mCommentDialog) {
            mCommentDialog = new Dialog(VideoPlayActivity.this, R.style.dialog_bottom_full);
            mCommentDialog.setCanceledOnTouchOutside(true); //手指触碰到外界取消
            mCommentDialog.setCancelable(true);             //可取消 为true
            Window window = mCommentDialog.getWindow();      // 得到dialog的窗体
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.share_animation);
            View viewComment = View.inflate(VideoPlayActivity.this, R.layout.dialog_video_comment, null);//获取布局视图
            recyclerView = viewComment.findViewById(R.id.recycler_view);
            textViewNumb = viewComment.findViewById(R.id.textViewNumb);
            imageViewDelete = viewComment.findViewById(R.id.imageViewDelete);
            editTextContent = viewComment.findViewById(R.id.et_comment);
            textViewSend = viewComment.findViewById(R.id.tv_send);
            textViewSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentVideo();
                }
            });
            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommentDialog.dismiss();
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            adapter = new VideoCommentAdapter(mData,mContext);
            recyclerView.setAdapter(adapter);
            window.setContentView(viewComment);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
        }
        adapter.notifyDataSetChanged();
        if (mData.size()>0) {
            textViewNumb.setText(mData.get(0).getCommentTotal()+"条评论");
        }
        mCommentDialog.show();
    }

    private void commentVideo() {
        if (!StringUtil.isNotNull(editTextContent.getText().toString())){
            RxToast.showToast("请输入评论的内容");
            return;
        }
        showWaitDialog(VideoPlayActivity.this);
        MQApi.commentUserVideo(userBean.getUId(), userMediaBean.getId(), editTextContent.getText().toString(), new SimpleCallBack<String>() {
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
                    editTextContent.setText("");
                    userMediaBean.setCommentNo(String.valueOf(Integer.parseInt(userMediaBean.getCommentNo())+1));
                    textViewComment.setText(String.valueOf(Integer.parseInt(userMediaBean.getCommentNo())+1));
                }else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void thumbsVideo() {
        showWaitDialog(VideoPlayActivity.this);
        MQApi.thumbsUpUserVideo(userMediaBean.getId(), userBean.getUId(), new SimpleCallBack<String>() {
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
                    userMediaBean.setIsThumbsUp("1");
                    imageViewPrase.setImageResource(R.mipmap.ic_praise_select);
                    textViewPrase.setText((Integer.parseInt(userMediaBean.getThumbsUpNo()) + 1) + "");
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.resetAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();

    }

}

package com.andy.meetquickly.activity.find;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.UserDataActivity;
import com.andy.meetquickly.adapter.MomentCommentAdapter;
import com.andy.meetquickly.adapter.MomentMediaAdapter;
import com.andy.meetquickly.adapter.ReportAdapter;
import com.andy.meetquickly.base.BaseActivity;
import com.andy.meetquickly.bean.CommentBean;
import com.andy.meetquickly.bean.MomnetListBean;
import com.andy.meetquickly.bean.ReportBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.CommentUtil;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.andy.meetquickly.utils.KeyboardUtils;
import com.andy.meetquickly.utils.SoftHideKeyBoardUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.luck.picture.lib.PictureSelector;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.shinichi.library.ImagePreview;

public class MomentDetailActivity extends BaseActivity {

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.recyclerView_pic)
    RecyclerView recyclerViewPic;
    @BindView(R.id.tv_coment_numb)
    TextView tvComentNumb;
    @BindView(R.id.iv_prise)
    ImageView ivPrise;
    @BindView(R.id.tv_prase_numb)
    TextView tvPraseNumb;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.recyclerView_comment)
    RecyclerView recyclerViewComment;
    @BindView(R.id.iv_more)
    ImageView ivMore;

    private MomnetListBean momnetListBean;
    private int mPage = 1;    //当前页面
    private List<CommentBean> mData = new ArrayList<>();
    private MomentCommentAdapter adapter;
    private UserBean userBean;
    private String momentId;
    private String toUid = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_moment_detail;
    }

    @Override
    public void initView() {
        setBarTitle("详情");
        userBean = UserCache.getInstance().getUser();
        SoftHideKeyBoardUtil.assistActivity(this);
        momentId = this.getIntent().getStringExtra("momentId");
        initCommentRecyclerView();
        initData();
    }

    private void initCommentRecyclerView() {
        recyclerViewComment.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MomentCommentAdapter(mData, mContext);
        //adapter设置点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toUid = mData.get(position).getUId();
                etComment.setHint("回复:" + mData.get(position).getNickName());
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_head) {
                    UserDataActivity.start(MomentDetailActivity.this, mData.get(position).getUId());
                }
            }
        });
        recyclerViewComment.setAdapter(adapter);
    }

    private void initMomentRecyclerView() {
        recyclerViewPic.setVisibility(View.GONE);
        if (momnetListBean.getUserDynamicFiles().size() > 0) {
            //大于0再去加载recycler
            recyclerViewPic.setVisibility(View.VISIBLE);
            recyclerViewPic.setLayoutManager(new GridLayoutManager(mContext, 3));
            MomentMediaAdapter adapter;
            if (momnetListBean.getType().equals("1")) {
                //图片
                adapter = new MomentMediaAdapter(momnetListBean.getUserDynamicFiles(), mContext, 1);
                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        List<String> lists = new ArrayList<>();
                        for (int i = 0; i < momnetListBean.getUserDynamicFiles().size(); i++) {
                            lists.add(momnetListBean.getUserDynamicFiles().get(i).getThumb());
                        }
                        ImagePreview.getInstance()
                                .setContext(MomentDetailActivity.this)
                                .setIndex(0)
                                .setImageList(lists)
                                .setEnableDragClose(true)
                                .setErrorPlaceHolder(R.drawable.load_failed)
                                .start();
                    }
                });
            } else {
                //视频
                adapter = new MomentMediaAdapter(momnetListBean.getUserDynamicFiles(), mContext, 2);
                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        SimpleVideoPlayActivity.start(mContext, momnetListBean.getUserDynamicFiles().get(position).getUrl());
//                        PictureSelector.create((Activity) mContext).externalPictureVideo(momnetListBean.getUserDynamicFiles().get(position).getUrl());
                    }
                });
            }
            recyclerViewPic.setAdapter(adapter);
        }
    }

    @Override
    public void initData() {
        showWaitDialog(MomentDetailActivity.this);
        MQApi.getDynamicDetailInfoById(userBean.getUId(), momentId,
                RxSPTool.getContent(MomentDetailActivity.this, Constant.KEY_LONGITUDE),
                RxSPTool.getContent(MomentDetailActivity.this, Constant.KEY_LATITUDE),
                new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        dissmissWaitDialog();
                        RxToast.showToast(e.toString());
                    }

                    @Override
                    public void onSuccess(String s) {
                        dissmissWaitDialog();
                        MyApiResult<MomnetListBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<MomnetListBean>>() {
                        }.getType());
                        if (myApiResult.isOk()) {
                            momnetListBean = myApiResult.getData();
                            showInfo();
                        } else {
                            RxToast.showToast(myApiResult.getMsg());
                        }

                    }
                });
        MQApi.getUserCommentsList(momentId, mPage, userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<List<CommentBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<CommentBean>>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<CommentBean>>>(){});
                if (myApiResult.isOk()) {
                    mData.clear();
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        mData.add(myApiResult.getData().get(i));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    private void showInfo() {
        ImageLoaderUtil.getInstance().loadCircleImage(mContext, momnetListBean.getFace(), ivHead);
        tvName.setText(momnetListBean.getNickName());
        tvAge.setText(String.valueOf(momnetListBean.getAge()));
        if ("1".equals(momnetListBean.getSex())) {
            ivSex.setImageResource(R.mipmap.ic_men);
        } else {
            ivSex.setImageResource(R.mipmap.ic_women);
        }
        tvTime.setText(CommentUtil.getStandardDate(momnetListBean.getCreateTime()));
        tvDistance.setText(momnetListBean.getDistance() + "km");
        tvContent.setText(momnetListBean.getContent());
        if (StringUtil.isNotNull(momnetListBean.getContent())) {
            tvContent.setVisibility(View.VISIBLE);
        } else {
            tvContent.setVisibility(View.GONE);
        }
        tvComentNumb.setText(momnetListBean.getCommentNum());
        tvPraseNumb.setText(momnetListBean.getPraiseNum());
        if (momnetListBean.getIsThumbsUp().equals("1")) {
            ivPrise.setImageResource(R.mipmap.ic_praise_select);
        } else {
            ivPrise.setImageResource(R.mipmap.ic_praise_unselect);
        }
        if (userBean.getUId().equals(momnetListBean.getUId())) {
            ivMore.setVisibility(View.GONE);
        }
        initMomentRecyclerView();
    }

    public static void start(Context context, String momentId) {
        Intent starter = new Intent(context, MomentDetailActivity.class);
        starter.putExtra("momentId", momentId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_prise, R.id.iv_more, R.id.tv_send, R.id.iv_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_prise:
                if (!"1".equals(momnetListBean.getIsThumbsUp())) {
                    parse();
                }
                break;
            case R.id.iv_more:
                showMoreDialog();
                break;
            case R.id.tv_send:
                CommentMoment();
                break;
            case R.id.iv_head:
                UserDataActivity.start(MomentDetailActivity.this, momnetListBean.getUId());
                break;
        }
    }

    private void parse() {
        String Uid;
        if (null != userBean) {
            Uid = userBean.getUId();
        } else {
            Uid = "";
        }
        MQApi.addUserThumbsUp(Uid, momnetListBean.getId(), new SimpleCallBack<String>() {
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
                    momnetListBean.setIsThumbsUp("1");
                    String praiseNum;
                    try {
                        praiseNum = String.valueOf(Integer.parseInt(momnetListBean.getPraiseNum()) + 1);
                    } catch (Exception e) {
                        praiseNum = "1";
                    }
                    momnetListBean.setPraiseNum(praiseNum);
                    ivPrise.setImageResource(R.mipmap.ic_praise_select);
                    tvPraseNumb.setText(momnetListBean.getPraiseNum());
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }

    CustomDialog customDialog;

    private void showMoreDialog() {
        customDialog = CustomDialog.show(MomentDetailActivity.this, R.layout.dialog_add_reserve, new CustomDialog.BindView() {
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
                        shieldMoment();
                    }
                });
                tv_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                        getReportContent();
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

    private void showReportDialog() {
        reportContent = "";
        //活动
        customDialog = CustomDialog.show(MomentDetailActivity.this, R.layout.dialog_report, new CustomDialog.BindView() {
            @Override
            public void onBind(CustomDialog dialog, View rootView) {
                ////绑定布局
                //ImageView btnOk = rootView.findViewById(R.id.btn_ok);
                RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MomentDetailActivity.this);
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
        MQApi.addReportDynamic(Uid, momnetListBean.getId(), reportContent, false, new SimpleCallBack<String>() {
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
    private void shieldMoment() {
        showWaitDialog(MomentDetailActivity.this);
        MQApi.addDynamicShield(userBean.getUId(), momnetListBean.getUId(), new SimpleCallBack<String>() {
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
                    RxToast.showToast("屏蔽成功");
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });
    }


    private void CommentMoment() {
        if (!StringUtil.isNotNull(etComment.getText().toString().trim())) {
            RxToast.showToast("请您输入评论内容");
            return;
        }

        MQApi.addUserComment(userBean.getUId(), momnetListBean.getId(), etComment.getText().toString().trim(), toUid
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
                            etComment.setText("");
                            KeyboardUtils.hideSoftInput(MomentDetailActivity.this);
                            initData();
                        } else {
                            RxToast.showToast(myApiResult.getMsg());
                        }
                    }
                });

    }
}

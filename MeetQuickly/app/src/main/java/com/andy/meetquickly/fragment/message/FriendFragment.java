package com.andy.meetquickly.fragment.message;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.UserDataActivity;
import com.andy.meetquickly.activity.message.FansListActivity;
import com.andy.meetquickly.activity.message.FollowListActivity;
import com.andy.meetquickly.activity.message.NewFriendActivity;
import com.andy.meetquickly.adapter.MyFraiendAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.RelationInfoBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserFriendBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.andy.meetquickly.utils.StringUtil;
import com.andy.meetquickly.view.indexbar.FloatingBarItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;
import qdx.indexbarlayout.IndexBar;
import qdx.indexbarlayout.IndexLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends BaseLazyFragment {


    Unbinder unbinder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.index_layout)
    IndexLayout indexLayout;
    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayoutManager mLayoutManager;
    private LinkedHashMap<Integer, String> mHeaderList;
    private ArrayList<UserFriendBean> mData = new ArrayList<>();
    private MyFraiendAdapter adapter;
    private UserBean userBean;
    private View mHeadView;
    private TextView tvNewFriend;
    private TextView tvFollow;
    private TextView tvFans;

    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void fetchData() {

        headNotify();
        userBean = UserCache.getInstance().getUser();

        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
        mHeadView = mLayoutInflater.inflate(R.layout.layout_friend_head,
                null);
        RelativeLayout rlNewFriend = mHeadView.findViewById(R.id.rl_new_frined);
        tvNewFriend = mHeadView.findViewById(R.id.tv_new_friend);
        tvFollow = mHeadView.findViewById(R.id.tv_user_follow);
        tvFans = mHeadView.findViewById(R.id.tv_user_fans);
        RelativeLayout rlUserFollow = mHeadView.findViewById(R.id.rl_user_follow);
        RelativeLayout rlUserFans = mHeadView.findViewById(R.id.rl_user_fans);
        rlNewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewFriendActivity.start(FriendFragment.this.getContext());
            }
        });
        rlUserFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowListActivity.start(FriendFragment.this.getContext());
            }
        });
        rlUserFans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FansListActivity.start(FriendFragment.this.getContext());
            }
        });

        mLayoutManager = new LinearLayoutManager(FriendFragment.this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(
//                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(
                new FloatingBarItemDecoration(FriendFragment.this.getContext(), new FloatingBarItemDecoration.DecorationCallback() {
                    @Override
                    public long getGroupId(int position) {
                        if (position == 0) {
                            return 0;
                        }
                        if (mData.size() == 0) {
                            return position;
                        }
                        if (StringUtil.isNotNull(mData.get(position - 1).getInitial())) {
                            return Character.toUpperCase(mData.get(position - 1).getInitial().charAt(0));
                        } else {
                            return 0;
                        }
                    }

                    @Override
                    public String getGroupFirstLine(int position) {
                        if (position == 0) {
                            return "###";
                        }
                        if (mData.size() == 0) {
                            return "###";
                        }
                        if (StringUtil.isNotNull(mData.get(position - 1).getInitial().substring(0, 1).toUpperCase())) {
                            return mData.get(position - 1).getInitial().substring(0, 1).toUpperCase();
                        } else {
                            return "###";
                        }
                    }
                }));
        adapter = new MyFraiendAdapter(FriendFragment.this.getContext(), mData);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RongIM.getInstance().startPrivateChat(getActivity(), mData.get(position).getUid(), mData.get(position).getNickName());
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                UserDataActivity.start(getActivity(),mData.get(position).getUid());
            }
        });
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        initData();
    }

    private void initData() {
        String Uid;
        if (null != userBean) {
            Uid = userBean.getUId();
        } else {
            Uid = "";
        }
        MQApi.getUserFriendList(Uid, "", new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                swipeRefreshLayout.setRefreshing(false);
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                swipeRefreshLayout.setRefreshing(false);
                MyApiResult<List<UserFriendBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<UserFriendBean>>>() {
                        }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<UserFriendBean>>>() {});
                if (myApiResult.isOk()) {
                    mData.clear();
                    adapter.removeAllHeaderView();
                    for (int i = 0; i < myApiResult.getData().size(); i++) {
                        UserFriendBean userFriendBean = myApiResult.getData().get(i);
                        userFriendBean.setNickName(myApiResult.getData().get(i).getNickName());
                        mData.add(userFriendBean);
                    }
                    Collections.sort(mData);
                    adapter.notifyDataSetChanged();
                    headNotify();
                    if (mData.size() > 0) {
                        List<String> mHeaders = new ArrayList<>(mHeaderList.values());
                        indexLayout.getIndexBar().setIndexsList(mHeaders);
                        float i = (float) mHeaders.size() / 27;
                        i = i * (float) 0.9;
                        indexLayout.setIndexBarHeightRatio(i);
                        indexLayout.setCircleColor(Color.RED);
                        indexLayout.setCircleRadius(100);
                        indexLayout.setCircleDuration(500);
                        indexLayout.setCircleColor(ContextCompat.getColor(FriendFragment.this.getContext(), R.color.circle_bg));
                        indexLayout.getIndexBar().setIndexChangeListener(new IndexBar.IndexChangeListener() {
                            @Override
                            public void indexChanged(String indexName) {
                                for (int i = 0; i < mData.size(); i++) {
                                    if (indexName.equals(mData.get(i).getInitial())) {
                                        mLayoutManager.scrollToPositionWithOffset(i + 1, 0);
                                        return;
                                    }
                                }
                            }
                        });
                    }
                    adapter.addHeaderView(mHeadView);
                } else {
                    RxToast.showToast(myApiResult.getMsg());
                }
            }
        });

        MQApi.getUserRelationInfo(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                MyApiResult<RelationInfoBean> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<RelationInfoBean>>() {
                }.getType());
                if (myApiResult.isOk()) {
                    if (null != myApiResult.getData()) {
                        tvNewFriend.setText("新的朋友(" + myApiResult.getData().getNewFriendsNo() + ")");
                        tvFollow.setText("我的关注(" + myApiResult.getData().getMyFollowNo() + ")");
                        tvFans.setText("我的粉丝(" + myApiResult.getData().getMyFansNo() + ")");
                    }
                } else {
                    RxToast.showToast(myApiResult.getMsg());
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
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        unbinder = ButterKnife.bind(this, view);
        view.setTag(1);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void headNotify() {
        if (mHeaderList == null) {
            mHeaderList = new LinkedHashMap<>();
        }
        mHeaderList.clear();
        if (mData.size() == 0) {
            return;
        }
        try {

            addHeaderToList(0, mData.get(0).getInitial());
        } catch (Exception e) {

        }
        for (int i = 1; i < mData.size(); i++) {
            try {
                if (!mData.get(i - 1).getInitial().equalsIgnoreCase(mData.get(i).getInitial())) {
                    addHeaderToList(i, mData.get(i).getInitial());
                }
            } catch (Exception e) {

            }

        }
    }

    private void addHeaderToList(int index, String header) {
        mHeaderList.put(index, header);
    }
}

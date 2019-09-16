package com.andy.meetquickly.fragment.userinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.find.VideoPlayActivity;
import com.andy.meetquickly.adapter.UserMomentAdapter;
import com.andy.meetquickly.adapter.UserVideoAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.bean.UserMediaBean;
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
import butterknife.Unbinder;

public class UserVideoFragment extends BaseLazyFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private static final String U_ID = "uId";
    private String uId = "";
    private UserBean userBean;

    private List<UserMediaBean> mData = new ArrayList<>();
    private UserVideoAdapter adapter;


    public UserVideoFragment() {

    }

    @Override
    public void fetchData() {
        initData();
    }

    private void initData() {
        showWaitDialog(UserVideoFragment.this.getActivity());
        MQApi.getUserImageOrVideoList(uId, 2, 1, new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                dissmissWaitDialog();
                RxToast.showToast(e.toString());
            }

            @Override
            public void onSuccess(String s) {
                dissmissWaitDialog();
                MyApiResult<List<UserMediaBean>> myApiResult =
                        new Gson().fromJson(s, new TypeToken<MyApiResult<List<UserMediaBean>>>(){}.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<UserMediaBean>>>(){});
                if (myApiResult.isOk()) {
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

    @Override
    public void doItEvery() {

    }

    // TODO: Customize parameter initialization
    public static UserVideoFragment newInstance(String uId) {
        UserVideoFragment fragment = new UserVideoFragment();
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
        userBean = UserCache.getInstance().getUser();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        recyclerView.setTag(2);
        recyclerView.setLayoutManager(new GridLayoutManager(UserVideoFragment.this.getContext() , 2));
        adapter = new UserVideoAdapter(mData,1,UserVideoFragment.this.getContext());
        //adapter设置点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VideoPlayActivity.start(UserVideoFragment.this.getContext(),mData.get(position));
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

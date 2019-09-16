package com.andy.meetquickly.fragment.message;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.message.BindShopActivity;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.ShopBindUserBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserMessageFragment extends BaseLazyFragment {

    Unbinder unbinder;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.tv_numb)
    TextView tvNumb;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private UserBean userBean;
    private List<ShopBindUserBean> mData;

    public UserMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void fetchData() {
//        Map<String, Boolean> map = new HashMap<>();
//        map.put(Conversation.ConversationType.PRIVATE.getName(), false); // 会话列表需要显示私聊会话, 第二个参数 true 代表私聊会话需要聚合显示
//        map.put(Conversation.ConversationType.GROUP.getName(), false);  // 会话列表需要显示群组会话, 第二个参数 false 代表群组会话不需要聚合显示
//        RongIM.getInstance().startConversationList(UserMessageFragment.this.getActivity().getApplicationContext(), map);
    }

    @Override
    public void doItEvery() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.conversationlist, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        linearLayout.setTag(0);
        userBean = UserCache.getInstance().getUser();
        swipeRefreshLayout.setEnabled(false);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                initData();
//            }
//        });
//        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        MQApi.getShopBindUserList(userBean.getUId(), new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(String s) {
                swipeRefreshLayout.setRefreshing(false);
                MyApiResult<List<ShopBindUserBean>> myApiResult = new Gson().fromJson(s, new TypeToken<MyApiResult<List<ShopBindUserBean>>>() {
                }.getType());
                if (myApiResult.isOk()) {
                    if ((null != myApiResult.getData()) && myApiResult.getData().size() > 0) {
                        if (!"0".equals(myApiResult.getData().get(0).getTotal())) {
                            mData = myApiResult.getData();
                            tvNumb.setVisibility(View.VISIBLE);
                            tvNumb.setText("有" + myApiResult.getData().get(0).getTotal() + "位网店经理人想邀请你驻店");
                        } else {
                            tvNumb.setVisibility(View.GONE);
                        }
                    } else {
                        tvNumb.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_numb)
    public void onViewClicked() {
        BindShopActivity.start(UserMessageFragment.this.getContext(), mData);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (null != userBean) {
                initData();
            }
        }
    }
}

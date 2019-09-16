package com.andy.meetquickly.fragment.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andy.meetquickly.R;
import com.andy.meetquickly.activity.home.UserDataActivity;
import com.andy.meetquickly.adapter.FindPeoplePlayAdapter;
import com.andy.meetquickly.base.BaseLazyFragment;
import com.andy.meetquickly.bean.MeetPeopleBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.cache.UserCache;
import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.fragment.HomeFragment;
import com.andy.meetquickly.fragment.home.dummy.DummyContent;
import com.andy.meetquickly.http.MQApi;
import com.andy.meetquickly.http.MyApiResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class FindPeoplePlayFragment extends BaseLazyFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_POSITION = "arf-position";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private int mPosition = 0;
    private int mPage = 1;    //当前页面
    private List<MeetPeopleBean> mData = new ArrayList<>();
    private FindPeoplePlayAdapter adapter;
    private UserBean userBean;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FindPeoplePlayFragment() {
    }

    @Override
    public void fetchData() {
        initData();
    }

    private void initData() {
        String Uid;
        String sex;
        if (null != userBean) {
            Uid = userBean.getUId();
            sex = userBean.getSex();
        } else {
            Uid = "";
            sex = "";
        }
        MQApi.getUsersList(mPage, Uid, sex,
                RxSPTool.getContent(FindPeoplePlayFragment.this.getContext(), Constant.KEY_LONGITUDE),
                RxSPTool.getContent(FindPeoplePlayFragment.this.getContext(), Constant.KEY_LATITUDE), new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        adapter.loadMoreFail();
                        RxToast.showToast(e.toString());
                    }

                    @Override
                    public void onSuccess(String s) {
                        MyApiResult<List<MeetPeopleBean>> myApiResult =
                                new Gson().fromJson(s, new TypeToken<MyApiResult<List<MeetPeopleBean>>>() {
                                }.getType());
//                        JSON.parseObject(s, new TypeReference<MyApiResult<List<MeetPeopleBean>>>(){});
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
    public static FindPeoplePlayFragment newInstance(int columnCount, int position) {
        FindPeoplePlayFragment fragment = new FindPeoplePlayFragment();
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
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        userBean = UserCache.getInstance().getUser();
        // Set the adapter
        view.setTag(mPosition);
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.list);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        if (true) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        adapter = new FindPeoplePlayAdapter(mData, context);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserDataActivity.start(FindPeoplePlayFragment.this.getContext(), mData.get(position).getUid());
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
}

package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.UserFriendBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class UserFriendAdapter extends RecyclerView.Adapter {
    private ArrayList<UserFriendBean> mList;
    private LayoutInflater mLayoutInflater;
    private OnContactsBeanClickListener mOnClickListener;
    private Context context;

    public UserFriendAdapter(Context context,LayoutInflater layoutInflater, ArrayList<UserFriendBean> list) {
        this.mList = list;
        mLayoutInflater = layoutInflater;
        this.context = context;
    }

    public void setOnContactsBeanClickListener(OnContactsBeanClickListener listener) {
        mOnClickListener = listener;
    }

    private UserFriendBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactsViewHolder(
                mLayoutInflater.inflate(R.layout.item_user_friend, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserFriendBean target = getItem(position);
        if (holder instanceof ContactsViewHolder) {
            ((ContactsViewHolder) holder).bindBean(target);
        } else {
            throw new IllegalStateException("Illegal state Exception onBindviewHolder");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class ContactsViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvAge;
        private ImageView ivHead;
        private ImageView ivSex;

        ContactsViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_username);
            tvAge = itemView.findViewById(R.id.tv_age);
            ivHead = itemView.findViewById(R.id.iv_user_head);
            ivSex = itemView.findViewById(R.id.iv_sex);
        }

        void bindBean(final UserFriendBean bean) {
            tvName.setText(bean.getNickName());
            tvAge.setText(String.valueOf(bean.getAge()));
            ImageLoaderUtil.getInstance().loadCircleImage(context,bean.getFace(),ivHead);
            if ("1".equals(bean.getSex())){
                ivSex.setImageResource(R.mipmap.ic_men);
            }else {
                ivSex.setImageResource(R.mipmap.ic_women);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onContactsBeanClicked(bean);
                }
            });
        }
    }

    public interface OnContactsBeanClickListener {
        void onContactsBeanClicked(UserFriendBean bean);
    }
}


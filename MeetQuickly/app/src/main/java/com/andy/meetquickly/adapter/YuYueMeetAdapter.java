package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.UserBean;
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
public class YuYueMeetAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {

    public List<UserBean> list = new ArrayList<>();
    public Context context;

    public YuYueMeetAdapter(@Nullable List<UserBean> data, Context context) {
        super(R.layout.item_shop_meet_people, data);
        this.context = context;
    }

    public List<UserBean> getSelectUser(){
        return list;
    }

    public boolean selectCoupon(UserBean id) {
        if (list.contains(id)) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(id)) {
                    list.remove(i);
                    return false;
                }
            }
        } else {
            if (list.size() >= 5) {
                return true;
            } else {
                list.add(id);
                return false;
            }
        }
        return false;
    }

    public int selectSize() {
        return list.size();
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBean item) {
        ImageView iv_head = helper.getView(R.id.iv_head);
        ImageView iv_sex = helper.getView(R.id.iv_sex);
        helper.setText(R.id.tv_name,item.getNickName())
                .setText(R.id.tv_age,item.getAge()+"")
                .setText(R.id.tv_level,"LV"+item.getLevel())
                .setText(R.id.tv_numb,item.getHeight()+"cm   "+item.getWeight()+"kg");
        ImageLoaderUtil.getInstance().loadRoundImage(context,item.getFace(),iv_head,20);
        if ("1".equals(item.getSex())){
            iv_sex.setImageResource(R.mipmap.ic_men);
        }else if ("2".equals(item.getSex())){
            iv_sex.setImageResource(R.mipmap.ic_women);
        }else {
            iv_sex.setImageResource(R.mipmap.ic_women);
        }
        View view = helper.getView(R.id.view_bg);
        view.setVisibility(View.GONE);
        ImageView iv_select = helper.getView(R.id.iv_select);
        iv_select.setVisibility(View.VISIBLE);
        iv_select.setImageResource(R.mipmap.ic_bt_unselect);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(item)) {
                iv_select.setImageResource(R.mipmap.ic_coupon_select_red);
                view.setVisibility(View.VISIBLE);
            }
        }
    }
}

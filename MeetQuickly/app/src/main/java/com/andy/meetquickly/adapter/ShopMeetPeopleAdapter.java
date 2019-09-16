package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.ShopInfoBean;
import com.andy.meetquickly.bean.UserBaseBean;
import com.andy.meetquickly.bean.UserBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class ShopMeetPeopleAdapter extends BaseQuickAdapter<UserBean,BaseViewHolder> {

    private Context context;
    public ShopMeetPeopleAdapter(@Nullable List<UserBean> data , Context context) {
        super(R.layout.item_round_head, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBean item) {
        ImageView iv_head = helper.getView(R.id.iv_head);
        ImageLoaderUtil.getInstance().loadCircleImage(context,item.getFace(),iv_head);
    }
}

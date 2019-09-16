package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.MeetPeopleBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class FindPeoplePlayAdapter extends BaseQuickAdapter<MeetPeopleBean,BaseViewHolder> {

    private Context context;

    public FindPeoplePlayAdapter(@Nullable List<MeetPeopleBean> data, Context context) {
        super(R.layout.item_find_people_play, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MeetPeopleBean item) {
        ImageView iv_user = helper.getView(R.id.iv_user);
        ImageView iv_sex = helper.getView(R.id.iv_sex);
        helper.setText(R.id.tv_name,item.getNickName())
                .setText(R.id.tv_age,item.getAge()+"")
                .setText(R.id.tv_distance,item.getDistance()+"km");
        if (item.getSex().equals("1")){
            iv_sex.setImageResource(R.mipmap.ic_men);
        }else if (item.getSex().equals("2")){
            iv_sex.setImageResource(R.mipmap.ic_women);
        }

        ImageLoaderUtil.getInstance().loadRoundImage(context,item.getFace(),iv_user,20);
    }
}

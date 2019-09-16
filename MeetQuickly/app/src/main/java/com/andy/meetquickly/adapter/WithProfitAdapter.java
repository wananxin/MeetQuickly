package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.WithProfitBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class WithProfitAdapter extends BaseQuickAdapter<WithProfitBean,BaseViewHolder> {

    private Context context;
    public WithProfitAdapter(@Nullable List<WithProfitBean> data, Context context) {
        super(R.layout.item_with_profit, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, WithProfitBean item) {

        helper.setText(R.id.tv_user_name,item.getNickName())
                .setText(R.id.tv_age,String.valueOf(item.getAge()))
                .setText(R.id.tv_recommend_name,"推荐人："+item.getRecommenderNickName())
                .setText(R.id.tv_numb,"+"+item.getIncome())
                .addOnClickListener(R.id.tv_button)
                .addOnClickListener(R.id.iv_user_head);

        if ("0".equals(item.getGroupLevel())){
            helper.setText(R.id.tv_time,"注册时间："+item.getTime());
        }else {
            helper.setText(R.id.tv_time,"开通时间："+item.getTime());
        }

        TextView tvWith = helper.getView(R.id.tv_type_with);
        TextView tvUser = helper.getView(R.id.tv_type_user);
        ImageView imageView = helper.getView(R.id.iv_user_head);
        if ("0".equals(item.getGroupLevel())){
            tvWith.setVisibility(View.GONE);
            tvUser.setVisibility(View.VISIBLE);
        }else {
            tvWith.setVisibility(View.VISIBLE);
            tvUser.setVisibility(View.GONE);
        }
        ImageView ivSex = helper.getView(R.id.iv_sex);
        if ("1".equals(item.getSex())){
            ivSex.setImageResource(R.mipmap.ic_men);
        }else {
            ivSex.setImageResource(R.mipmap.ic_women);
        }
        ImageLoaderUtil.getInstance().loadCircleImage(context,item.getFace(),imageView);

    }
}

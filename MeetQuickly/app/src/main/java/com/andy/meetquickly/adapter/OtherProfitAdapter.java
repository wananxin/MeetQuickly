package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.OtherProfitBean;
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
public class OtherProfitAdapter extends BaseQuickAdapter<OtherProfitBean,BaseViewHolder> {

    private Context context;
    private int type;//1为礼物收益 按钮为联系他   2为代金券收益  按钮为查看
    public OtherProfitAdapter(@Nullable List<OtherProfitBean> data, Context context ,int type) {
        super(R.layout.item_other_profit, data);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, OtherProfitBean item) {

        helper.setText(R.id.tv_user_name,item.getNickName())
                .setText(R.id.tv_age,String.valueOf(item.getAge()))
                .setText(R.id.tv_time,item.getTime())
                .setText(R.id.tv_numb,"+"+item.getIncome())
                .addOnClickListener(R.id.tv_button);

        if (type == 1){
            helper.setText(R.id.tv_button,"联系TA");
        }else {
            helper.setText(R.id.tv_button,"查看");
        }
        ImageView imageView = helper.getView(R.id.iv_user_head);
        ImageView ivSex = helper.getView(R.id.iv_sex);
        if ("1".equals(item.getSex())){
            ivSex.setImageResource(R.mipmap.ic_men);
        }else {
            ivSex.setImageResource(R.mipmap.ic_women);
        }
        ImageLoaderUtil.getInstance().loadCircleImage(context,item.getFace(),imageView);

    }
}

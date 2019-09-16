package com.andy.meetquickly.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.SettlementBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class SettlementAdapter extends BaseQuickAdapter<SettlementBean, BaseViewHolder> {


    private Context context;

    public SettlementAdapter(@Nullable List<SettlementBean> data, Context context) {
        super(R.layout.item_bill_flow, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SettlementBean item) {
        /*@BindView(R.id.tv_type)
          @BindView(R.id.tv_time)
          @BindView(R.id.tv_money)
          @BindView(R.id.tv_balance)
          @BindView(R.id.tv_account)
          @BindView(R.id.tv_apply_time)
          @BindView(R.id.tv_yuji_time)
          @BindView(R.id.iv_daozhang)
          @BindView(R.id.view_daozhang)
          @BindView(R.id.tv_end)
          @BindView(R.id.tv_end_time)*/
        helper.setText(R.id.tv_time,item.getTime())
                .setText(R.id.tv_money,"-"+item.getMoney())
                .setText(R.id.tv_apply_time,item.getTime())
                .setText(R.id.tv_account,"支付宝账号："+item.getAccountNumber())
                .setText(R.id.tv_yuji_time,item.getCanAccountTime())
        .setText(R.id.tv_balance,item.getBalance());
        View view = helper.getView(R.id.view_daozhang);
        ImageView imageView = helper.getView(R.id.iv_daozhang);
        TextView textView = helper.getView(R.id.tv_end_time);
        TextView tvEnd = helper.getView(R.id.tv_end);
        if ("1".equals(item.getStatus())){
            view.setBackgroundColor(Color.parseColor("#4CB3FF"));
            imageView.setImageResource(R.mipmap.ic_daozhang_ok);
            textView.setText(item.getUpdateTime());
            tvEnd.setText("已到账");
            textView.setVisibility(View.VISIBLE);
        }else if ("0".equals(item.getStatus())){
            view.setBackgroundColor(Color.parseColor("#999999"));
            imageView.setImageResource(R.mipmap.ic_daozhang_error);
            tvEnd.setText("未到账");
            textView.setVisibility(View.GONE);
        }else if ("2".equals(item.getStatus())){
            view.setBackgroundColor(Color.parseColor("#999999"));
            imageView.setImageResource(R.mipmap.ic_daozhang_error);
            tvEnd.setText("已拒绝");
            textView.setVisibility(View.GONE);
        }

    }
}

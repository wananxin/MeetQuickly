package com.andy.meetquickly.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.ReportBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class ReportAdapter extends BaseQuickAdapter<ReportBean,BaseViewHolder> {

    String select = null;

    public ReportAdapter(@Nullable List<ReportBean> data) {
        super(R.layout.item_report_content, data);
    }

    public void setSelect(String s){
        select = s;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportBean item) {
        helper.setText(R.id.tv_content,item.getTypeName());
        RadioButton radioButton = helper.getView(R.id.radioButton);
        if (item.getTypeName().equals(select)){
            radioButton.setChecked(true);
        }else {
            radioButton.setChecked(false);
        }
    }
}

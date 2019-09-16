package com.andy.meetquickly.adapter;

import android.support.annotation.Nullable;

import com.andy.meetquickly.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class DialogWelfareAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public DialogWelfareAdapter(@Nullable List<String> data) {
        super(R.layout.item_welfare_home_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.textViewContent,item);
    }
}

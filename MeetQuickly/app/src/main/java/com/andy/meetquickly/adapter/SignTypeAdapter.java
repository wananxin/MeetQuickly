package com.andy.meetquickly.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.TagTypeBean;
import com.andy.meetquickly.bean.UserDataInfoBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class SignTypeAdapter extends BaseQuickAdapter<TagTypeBean,BaseViewHolder> {

    public SignTypeAdapter(@Nullable List<TagTypeBean> data) {
        super(R.layout.item_sign_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TagTypeBean item) {
        helper.setText(R.id.textViewName,item.getName());

        ImageView imageView = helper.getView(R.id.imageViewSelect);
        if (item.isSelect()){
            imageView.setImageResource(R.mipmap.ic_select);
        }else {
            imageView.setImageResource(R.mipmap.ic_unselect);
        }
    }
}

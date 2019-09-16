package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.UserDataInfoBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class OftenPlaceAdapter extends BaseQuickAdapter<UserDataInfoBean.ManagerStoreBean,BaseViewHolder> {

    private Context context;
    private int type; //1为查看  2位编辑

    public OftenPlaceAdapter(@Nullable List<UserDataInfoBean.ManagerStoreBean> data , Context context ,int type) {
        super(R.layout.item_often_space, data);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserDataInfoBean.ManagerStoreBean item) {

        helper.setText(R.id.tv_name,item.getCompanyName())
        .addOnClickListener(R.id.iv_delete)
        .addOnClickListener(R.id.iv_img);
        if (type == 1){
            helper.setVisible(R.id.iv_delete,false);
        }else {
            helper.setVisible(R.id.iv_delete,true);
        }

        ImageView imageView = helper.getView(R.id.iv_img);
        ImageLoaderUtil.getInstance().loadCircleImage(context,item.getCoverImg(),imageView);

    }
}

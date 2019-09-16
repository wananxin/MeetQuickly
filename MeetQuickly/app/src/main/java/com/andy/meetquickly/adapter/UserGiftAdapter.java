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
public class UserGiftAdapter extends BaseQuickAdapter<UserDataInfoBean.GiftsEntityBean,BaseViewHolder> {

    private Context context;

    public UserGiftAdapter(@Nullable List<UserDataInfoBean.GiftsEntityBean> data, Context context) {
        super(R.layout.item_user_gift, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserDataInfoBean.GiftsEntityBean item) {
        ImageView imageView = helper.getView(R.id.imageViewPrase);
        ImageLoaderUtil.getInstance().loadCircleImage(context,item.getUrl(),imageView);
    }

}

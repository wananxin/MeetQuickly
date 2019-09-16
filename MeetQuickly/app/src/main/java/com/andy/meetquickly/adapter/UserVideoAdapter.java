package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.UserMediaBean;
import com.andy.meetquickly.utils.ImageLoaderUtil;
import com.andy.meetquickly.utils.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class UserVideoAdapter extends BaseQuickAdapter<UserMediaBean,BaseViewHolder> {

    private int type;  //1位查看 2位编辑
    private Context context;
    public UserVideoAdapter(@Nullable List<UserMediaBean> data,int type,Context context) {
        super(R.layout.item_user_data_video, data);
        this.type = type;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserMediaBean item) {

        ImageView imageViewPic = helper.getView(R.id.iv_share_moment);
        ImageView imageViewPrase = helper.getView(R.id.imageViewPrase);
        ImageView imageViewDelete = helper.getView(R.id.imageViewDelete);
        if (StringUtil.isNotNull(item.getId())) {
            ImageLoaderUtil.getInstance().loadImage(context, item.getUrl(), imageViewPic);
            if (type == 1) {
                imageViewDelete.setVisibility(View.GONE);
            } else {
                imageViewDelete.setVisibility(View.VISIBLE);
                helper.addOnClickListener(R.id.imageViewDelete);
            }
            if ("1".equals(item.getIsThumbsUp())) {
                imageViewPrase.setImageResource(R.mipmap.ic_praise_select);
            } else {
                imageViewPrase.setImageResource(R.mipmap.ic_praise_unselect);
            }

            helper.setText(R.id.textViewWatch, item.getPlayNo())
                    .setText(R.id.textViewPrase, item.getThumbsUpNo());
        }else {
            helper.setVisible(R.id.imageViewWatch,false)
                    .setVisible(R.id.textViewWatch,false)
                    .setVisible(R.id.imageViewPrase,false)
                    .setVisible(R.id.textViewPrase,false)
                    .setVisible(R.id.imageViewDelete,false);
            imageViewPic.setImageResource(R.mipmap.img_user_add_video);
        }

    }
}

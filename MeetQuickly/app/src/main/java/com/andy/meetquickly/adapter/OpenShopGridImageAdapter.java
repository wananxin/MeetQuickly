package com.andy.meetquickly.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.OpenShopBean;
import com.andy.meetquickly.bean.UserMediaBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/4/23 20:58
 * 描述：
 */
public class OpenShopGridImageAdapter extends
        RecyclerView.Adapter<OpenShopGridImageAdapter.ViewHolder> {
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private LayoutInflater mInflater;
    private List<OpenShopBean.StoreImgsBean> list = new ArrayList<>();
    private int selectMax = 9;
    private Context context;
    /**
     * 点击添加图片跳转
     */
    private onAddPicClickListener mOnAddPicClickListener;
    private onDelPicClickListener mOnDelPicClickListener;

    public interface onAddPicClickListener {
        void onAddPicClick();
    }

    public interface onDelPicClickListener {
        void onDelPicClick(int position);
    }

    public OpenShopGridImageAdapter(Context context, onAddPicClickListener mOnAddPicClickListener , onDelPicClickListener mOnDelPicClickListener) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
        this.mOnDelPicClickListener = mOnDelPicClickListener;
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    public void setList(List<OpenShopBean.StoreImgsBean> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        ImageView ll_del;

        public ViewHolder(View view) {
            super(view);
            mImg = (ImageView) view.findViewById(R.id.iv_img);
            ll_del = (ImageView) view.findViewById(R.id.iv_delete);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_select_pic,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private boolean isShowAddItem(int position) {
        int size = list.size() == 0 ? 0 : list.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        //少于8张，显示继续添加的图标
        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.mImg.setImageResource(R.mipmap.ic_add_img);
            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnAddPicClickListener.onAddPicClick();
                }
            });
            viewHolder.ll_del.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.ll_del.setVisibility(View.VISIBLE);
            viewHolder.ll_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnDelPicClickListener.onDelPicClick(position);
                }
            });
            OpenShopBean.StoreImgsBean media = list.get(position);
            String path = "";
            path = media.getUrl();
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.color.line_gray)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(viewHolder.itemView.getContext())
                    .load(path)
                    .apply(options)
                    .into(viewHolder.mImg);
            //itemView 的点击事件
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = viewHolder.getAdapterPosition();
                        mItemClickListener.onItemClick(adapterPosition, v);
                    }
                });
            }
        }
    }

    protected OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}

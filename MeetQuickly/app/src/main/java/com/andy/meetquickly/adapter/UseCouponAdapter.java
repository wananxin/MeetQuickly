package com.andy.meetquickly.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.meetquickly.R;
import com.andy.meetquickly.bean.CouponBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/3/28 14:02
 * 描述：举报页面RecyclerViewAdapter
 */
public class UseCouponAdapter extends BaseQuickAdapter<CouponBean, BaseViewHolder> {

    public List<CouponBean> list = new ArrayList<>();

    public UseCouponAdapter(@Nullable List<CouponBean> data) {
        super(R.layout.item_coupon_ing, data);
    }

    public List<CouponBean> getSelect(){
        return list;
    }

    public void selectCoupon(CouponBean id) {
        if (list.contains(id)) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(id)) {
                    list.remove(i);
                    return;
                }
            }
        } else {
//            1:商务KTV，2:酒吧，3:量贩KTV，4:清吧 5.无限制 6.固定网店 7.网店领取
            if (id.getUseScope().equals("5")) {
                for (int i = 0; i < list.size(); i++) {
                    if ((list.get(i).getUseScope().equals("6"))||(list.get(i).getUseScope().equals("7"))){
                        list.remove(i);
                    }
                }
                list.add(id);
            }else if (id.getUseScope().equals("1")){
                for (int i = 0; i < list.size(); i++) {
                    if ((!list.get(i).getUseScope().equals("1"))&&(!list.get(i).getUseScope().equals("5"))){
                        list.remove(i);
                    }
                }
                list.add(id);
            }else if (id.getUseScope().equals("2")){
                for (int i = 0; i < list.size(); i++) {
                    if ((!list.get(i).getUseScope().equals("2"))&&(!list.get(i).getUseScope().equals("5"))){
                        list.remove(i);
                    }
                }
                list.add(id);
            }else if (id.getUseScope().equals("3")){
                for (int i = 0; i < list.size(); i++) {
                    if ((!list.get(i).getUseScope().equals("3"))&&(!list.get(i).getUseScope().equals("5"))){
                        list.remove(i);
                    }
                }
                list.add(id);
            }else if (id.getUseScope().equals("4")){
                for (int i = 0; i < list.size(); i++) {
                    if ((!list.get(i).getUseScope().equals("4"))&&(!list.get(i).getUseScope().equals("5"))){
                        list.remove(i);
                    }
                }
                list.add(id);
            }else if (id.getUseScope().equals("6")){
                list.clear();
                list.add(id);
            }else if (id.getUseScope().equals("7")){
                list.clear();
                list.add(id);
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponBean item) {
        helper.setText(R.id.textViewMoney, item.getCouponAmount());
        // 1:商务KTV，2:酒吧，3:量贩KTV，4:清吧 5.无限制 6.固定网店 7.网店领取
        if ("1".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限商务KTV使用");
            helper.setVisible(R.id.textViewMengkan,false);
            helper.setVisible(R.id.textViewStoreName,false);
        } else if ("2".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限酒吧使用");
            helper.setVisible(R.id.textViewMengkan,false);
            helper.setVisible(R.id.textViewStoreName,false);

        } else if ("3".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限量贩KTV使用");
            helper.setVisible(R.id.textViewMengkan,false);
            helper.setVisible(R.id.textViewStoreName,false);

        } else if ("4".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限清吧使用");
            helper.setVisible(R.id.textViewMengkan,false);
            helper.setVisible(R.id.textViewStoreName,false);

        } else if ("5".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "平台内任意网店均可使用");
            helper.setVisible(R.id.textViewMengkan,true);
            helper.setVisible(R.id.textViewStoreName,false);

        } else if ("6".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限"+item.getCompanyName()+"使用");
            helper.setText(R.id.textViewStoreName, "("+item.getStoreName()+"经理)");
            helper.setVisible(R.id.textViewMengkan,false);
            helper.setVisible(R.id.textViewStoreName,true);

        } else if ("7".equals(item.getUseScope())) {
            helper.setText(R.id.textViewContent, "仅限"+item.getCompanyName()+"使用");
            helper.setText(R.id.textViewStoreName, "("+item.getStoreName()+"经理)");
            helper.setVisible(R.id.textViewMengkan,false);
            helper.setVisible(R.id.textViewStoreName,true);

        }

        if ("1".equals(item.getUseType())){
            helper.setText(R.id.textViewTime,"有效日期：永久有效");
        }else {
            helper.setText(R.id.textViewTime,"有效日期：" + item.getBeginTime() + "至" + item.getEndTime());
        }
        TextView tv_use = helper.getView(R.id.tv_use);
        tv_use.setVisibility(View.GONE);
        ImageView iv_select = helper.getView(R.id.iv_select);
        iv_select.setVisibility(View.VISIBLE);
        iv_select.setImageResource(R.mipmap.ic_bt_unselect);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(item.getId())) {
                iv_select.setImageResource(R.mipmap.ic_coupon_select_red);
            }
        }
    }
}

package com.andy.meetquickly.bean;

import com.andy.meetquickly.utils.StringUtil;
import com.andy.meetquickly.view.indexbar.Abbreviated;
import com.andy.meetquickly.view.indexbar.ContactsUtils;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/6/13 22:28
 * 描述：
 */
public class ShieldPhoneBean implements Abbreviated, Comparable<ShieldPhoneBean>{


    /**
     * addTime : 2019-05-14 11:58:29
     * id : 586
     * shieldMobile : 15512345678
     * shieldName : 导航
     * status : 1
     * uId : 20005630
     */

    private String addTime;
    private String id;
    private String shieldMobile;
    private String shieldName;
    private String status;
    private String uId;

    private  String mAbbreviation;
    private  String mInitial;

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShieldMobile() {
        return shieldMobile;
    }

    public void setShieldMobile(String shieldMobile) {
        this.shieldMobile = shieldMobile;
    }

    public String getShieldName() {
        return shieldName;
    }

    public void setShieldName(String shieldName) {
        this.mAbbreviation = ContactsUtils.getAbbreviation(shieldName);
        this.mInitial = mAbbreviation.substring(0, 1);
        this.shieldName = shieldName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    @Override
    public String getInitial() {
        return mInitial;
    }

    @Override
    public int compareTo(ShieldPhoneBean o) {
        if (StringUtil.isNotNull(mAbbreviation)) {
            if (mAbbreviation.equals(o.mAbbreviation)) {
                return 0;
            }
            boolean flag;
            if ((flag = mAbbreviation.startsWith("#")) ^ o.mAbbreviation.startsWith("#")) {
                return flag ? 1 : -1;
            }
            return getInitial().compareTo(o.getInitial());
        } else {
            return -1;
        }
    }
}

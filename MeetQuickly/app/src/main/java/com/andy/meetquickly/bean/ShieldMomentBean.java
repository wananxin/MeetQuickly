package com.andy.meetquickly.bean;

import com.andy.meetquickly.utils.StringUtil;
import com.andy.meetquickly.view.indexbar.Abbreviated;
import com.andy.meetquickly.view.indexbar.ContactsUtils;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/6/13 22:29
 * 描述：
 */
public class ShieldMomentBean implements Abbreviated, Comparable<ShieldMomentBean>{


    /**
     * createTime : 2019-06-13 22:30:09
     * face : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/5/31/402881e76b0ca7a2016b0cb088ca0002.jpg
     * id : 4
     * nickName : 愉快的卤蛋
     * shielUid : 20000241
     * status : 1
     * uId : 20005630
     */

    private String createTime;
    private String face;
    private String id;
    private String nickName;
    private String shielUid;
    private String status;
    private String uId;

    private  String mAbbreviation;
    private  String mInitial;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
            this.mAbbreviation = ContactsUtils.getAbbreviation(nickName);
            this.mInitial = mAbbreviation.substring(0, 1);
            this.nickName = nickName;
    }

    public String getShielUid() {
        return shielUid;
    }

    public void setShielUid(String shielUid) {
        this.shielUid = shielUid;
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
    public int compareTo(ShieldMomentBean o) {
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

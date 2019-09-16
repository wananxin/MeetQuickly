package com.andy.meetquickly.message;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/24 16:35
 * 描述：
 */
public class screenShopMessage {

    private int mKind;
    private int mDiscountLeft;
    private int mDiscountRight;
    private int mActivityName;
    private String mStoreId;
    public screenShopMessage(int mKind,int mDiscountLeft,int mDiscountRight,int mActivityName,String mStoreId){
        this.mKind=mKind;
        this.mDiscountLeft=mDiscountLeft;
        this.mDiscountRight=mDiscountRight;
        this.mActivityName=mActivityName;
        this.mStoreId = mStoreId;
    }

    public String getmStoreId() {
        return mStoreId;
    }

    public void setmStoreId(String mStoreId) {
        this.mStoreId = mStoreId;
    }

    public int getmKind() {
        return mKind;
    }

    public void setmKind(int mKind) {
        this.mKind = mKind;
    }

    public int getmDiscountLeft() {
        return mDiscountLeft;
    }

    public void setmDiscountLeft(int mDiscountLeft) {
        this.mDiscountLeft = mDiscountLeft;
    }

    public int getmDiscountRight() {
        return mDiscountRight;
    }

    public void setmDiscountRight(int mDiscountRight) {
        this.mDiscountRight = mDiscountRight;
    }

    public int getmActivityName() {
        return mActivityName;
    }

    public void setmActivityName(int mActivityName) {
        this.mActivityName = mActivityName;
    }
}

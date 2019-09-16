package com.andy.meetquickly.view.plugin;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.andy.meetquickly.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/9 10:32
 * 描述：
 */
@MessageTag(value = "RCD:GiftMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class GiftMessage extends MessageContent {
    private String content;
    private String extra;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }


    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("content", this.getContent());
            jsonObj.put("extra", this.getExtra());
        } catch (JSONException e) {
            LogUtil.e(e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GiftMessage(byte[] data) {
//        super(data);
//        String jsonStr=null;
//        try {
//            jsonStr =new String(data,"UTF-8");
//            org.json.JSONObject jsonObj = new org.json.JSONObject(jsonStr);
//            JSONObject object = JSON.parseObject(jsonStr);
//            if (jsonObj.has("content")) {
//                this.setContent(jsonObj.optString("content"));
//            }
//            setGiftNum(object.getString("giftNum"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String jsonStr = "";

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("content"))
                setContent(jsonObj.optString("content"));
            if (jsonObj.has("extra"))
                setExtra(jsonObj.optString("extra"));
        } catch (JSONException e) {
            LogUtil.e(e.getMessage());
        }

    }

    public static final Creator<GiftMessage> CREATOR = new Creator<GiftMessage>() {
        @Override
        public GiftMessage createFromParcel(Parcel source) {
            return new GiftMessage(source);
        }
        @Override
        public GiftMessage[] newArray(int size) {
            return new GiftMessage[size];
        }
    };

    public GiftMessage(){

    }

    public GiftMessage(Parcel parcel){
        content = ParcelUtils.readFromParcel(parcel);
        extra = ParcelUtils.readFromParcel(parcel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, content);
        ParcelUtils.writeToParcel(dest, extra);
    }
}

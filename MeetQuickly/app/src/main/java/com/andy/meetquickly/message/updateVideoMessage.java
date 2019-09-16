package com.andy.meetquickly.message;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/24 16:35
 * 描述：
 */
public class updateVideoMessage {

    private String path;   //为视频的地址
    private int type;     //为视频的类型0，为去剪裁  1为去添加

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public updateVideoMessage(String path, int type) {
        this.path = path;
        this.type = type;
    }


}

package com.andy.meetquickly.message;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/24 16:35
 * 描述：
 */
public class updatePicMessage {

    private List<LocalMedia> message = new ArrayList<>();
    public updatePicMessage(List<LocalMedia> message){
        this.message=message;
    }
    public List<LocalMedia> getMessage() {
        return message;
    }

    public void setMessage(List<LocalMedia> message) {
        this.message = message;
    }

}

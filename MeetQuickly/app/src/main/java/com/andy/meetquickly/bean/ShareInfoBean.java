package com.andy.meetquickly.bean;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/7/1 17:42
 * 描述：
 */
public class ShareInfoBean {


    /**
     * detail : 喝酒不打烊,服务不打折,买单折上折。【 让娱乐生活更美好！】
     * title : 万代(谢经理)
     * img : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/2019/6/11/402881e76b467c07016b467c07010000.jpg
     * shareUrl : http://www.woquyule.com:8080/rapidMeet/mineDetails/mineData.html?storeId=1000&uId=20005630
     */

    private String detail;
    private String title;
    private String img;
    private String shareUrl;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}

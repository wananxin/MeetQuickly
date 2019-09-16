package com.andy.meetquickly.bean;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/6/4 10:04
 * 描述：
 */
public class HomeBannerBean {


    /**
     * bannerTitle : 达人权益说明
     * content :  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
     <html>
     <head>
     <meta name="viewport" content="width=device-width"/>
     <style>
     body, html {
     margin: 0px;
     padding: 0px;
     width:100%;
     }
     .content { width: 100%; height: auto; margin: 0 auto; text-align: center;}
     .content img{width:100%;height:100%;}
     </style>
     </head>
     <body>
     <div class="content">
     <img src="http://image.aiyueta.com/image/banner/2017/09/27/banner11.png" />
     </div>
     </body>
     </html>
     * createTime : 2017-09-14 14:40:24
     * id : 297edff85724f00c01574c10b8552c22
     * sort : 0
     * url : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/banner/2017/09/27/banner1.png
     */

    private String bannerTitle;
    private String content;
    private String createTime;
    private String id;
    private String sort;
    private String url;

    public String getBannerTitle() {
        return bannerTitle;
    }

    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

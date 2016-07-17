package com.bean.simplenews.module.news.model.bean;

import java.io.Serializable;

public class NewsBean implements Serializable {

    private String docid;
    private String title;
    private String imgsrc;  //图片地址
    private String ptime;   //时间
    private String replyCount;

    public String getDocid() {
        return docid;
    }
    public void setDocid(String docid) {
        this.docid = docid;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getImgsrc() {
        return imgsrc;
    }
    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }
    public String getPtime() {
        return ptime.substring(5, 10);
    }
    public void setPtime(String ptime) {
        this.ptime = ptime;
    }
    public String getReplyCount() {
        return replyCount;
    }
    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }
}

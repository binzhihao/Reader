package com.bean.simplenews.module.news.model.bean;

import java.io.Serializable;

public class NewsBean implements Serializable {

    private String docid;
    private String title;
    private String digest;  //小内容
    private String imgsrc;  //图片地址
    private String source;  //来源
    private String ptime;   //时间
    private String tag;

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
    public String getDigest() {
        return digest;
    }
    public void setDigest(String digest) {
        this.digest = digest;
    }
    public String getImgsrc() {
        return imgsrc;
    }
    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getPtime() {
        return ptime;
    }
    public void setPtime(String ptime) {
        this.ptime = ptime;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
}

package com.bean.simplenews.module.news.model.bean;

import java.io.Serializable;
import java.util.List;

public class NewsDetailBean implements Serializable {

    private String title;
    private String body;
    private List<String> imgList;   //图片列表

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public List<String> getImgList() {
        return imgList;
    }
    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }
}

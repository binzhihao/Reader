package com.bean.simplenews.module.news.model.bean;

import java.io.Serializable;
import java.util.List;

public class NewsDetailBean implements Serializable {

    private String title;
    private String body;
    private List<Image> img;   //图片列表

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
    public List<Image> getImg() {
        return img;
    }
    public void setImg(List<Image> imgList) {
        this.img = imgList;
    }

    public class Image {
        private String ref;
        private String src;

        public String getRef() {
            return ref;
        }
        public void setRef(String ref) {
            this.ref = ref;
        }
        public String getSrc() {
            return src;
        }
        public void setSrc(String src) {
            this.src = src;
        }
    }
}

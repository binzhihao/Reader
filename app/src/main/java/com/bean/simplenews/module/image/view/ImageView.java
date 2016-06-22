package com.bean.simplenews.module.image.view;

import com.bean.simplenews.bean.ImageBean;

import java.util.List;

public interface ImageView {
    void addImages(List<ImageBean> list);
    void showProgress();
    void hideProgress();
    void showLoadFailMsg();
}

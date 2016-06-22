package com.bean.simplenews.module.news.view;

import com.bean.simplenews.bean.NewsBean;

import java.util.List;

public interface NewsView {

    void showProgress();

    void addNews(List<NewsBean> newsList);

    void hideProgress();

    void showLoadFailMsg();

    void LoadSuccess();
}

package com.bean.simplenews.module.news.view;

import com.bean.simplenews.bean.NewsBean;
import com.bean.simplenews.common.mvp.MVPView;

import java.util.List;

public interface NewsListView extends MVPView{

    void addNews(List<NewsBean> newsList);

    void showProgress();

    void hideProgress();

    void showLoadFailure();

    void showLoadSuccess();
}
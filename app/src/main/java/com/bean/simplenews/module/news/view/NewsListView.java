package com.bean.simplenews.module.news.view;

import com.bean.simplenews.module.news.model.bean.NewsBean;
import com.bean.simplenews.common.mvp.MVPView;

import java.util.List;

public interface NewsListView extends MVPView{

    void setNewsData(List<NewsBean> newsList, boolean isUp);

    void showLoadFailure();

    void showLoadSuccess();

}

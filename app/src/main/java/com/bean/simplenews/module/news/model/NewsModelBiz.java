package com.bean.simplenews.module.news.model;

public interface NewsModelBiz {

    void loadNews(String url, int type, NewsModel.OnLoadNewsListListener listener);

    void loadNewsDetail(String docid, NewsModel.OnLoadNewsDetailListener listener);

}

package com.bean.simplenews.module.news.model;

/**
 * Description :
 * Author : bean
 * Email  : bean.liuling@gmail.com
 * Blog   : http://www.liuling123.com
 * Date   : 15/12/19
 */
public interface NewsModel {

    void loadNews(String url, int type, NewsModelImpl.OnLoadNewsListListener listener);

    void loadNewsDetail(String docid, NewsModelImpl.OnLoadNewsDetailListener listener);

}

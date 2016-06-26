package com.bean.simplenews.module.news.presenter;

import com.bean.simplenews.bean.NewsBean;
import com.bean.simplenews.api.Urls;
import com.bean.simplenews.common.base.BasePresenter;
import com.bean.simplenews.common.Constants;
import com.bean.simplenews.module.news.model.INewsModel;
import com.bean.simplenews.module.news.model.NewsModel;
import com.bean.simplenews.module.news.view.NewsListView;

import java.util.List;

public class NewsListPresenter extends BasePresenter<NewsListView> implements INewsListPresenter, NewsModel.OnLoadNewsListListener {

    private INewsModel mNewsModel;

    public NewsListPresenter(NewsListView newsListView) {
        onInitial(newsListView);
    }

    @Override
    public void onInitial(NewsListView view) {
        super.onInitial(view);
        mNewsModel = new NewsModel();
    }

    @Override
    public void loadNews(int type, int pageIndex) {
        String url = getUrl(type, pageIndex);
        //只有第一页的或者刷新的时候才显示刷新进度条
        if(pageIndex == 0) {
            obtainView().showProgress();
        }
        mNewsModel.loadNews(url, type, this);
    }

    @Override
    public void onSuccess(List<NewsBean> list) {
        obtainView().hideProgress();
        obtainView().addNews(list);
        obtainView().showLoadSuccess();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        obtainView().hideProgress();
        obtainView().showLoadFailure();
    }

    /**
     * 根据类别和页面索引创建url
     * @param type
     * @param pageIndex
     * @return
     */
    private String getUrl(int type, int pageIndex) {
        StringBuffer sb = new StringBuffer();
        switch (type) {
            case Constants.NEWS_TYPE_TOP:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            case Constants.NEWS_TYPE_NBA:
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            case Constants.NEWS_TYPE_CARS:
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            case Constants.NEWS_TYPE_JOKES:
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
        }
        sb.append("/").append(pageIndex).append(Urls.END_URL);
        return sb.toString();
    }

}

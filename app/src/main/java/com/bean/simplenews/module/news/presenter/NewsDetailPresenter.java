package com.bean.simplenews.module.news.presenter;

import com.bean.simplenews.bean.NewsDetailBean;
import com.bean.simplenews.common.base.BasePresenter;
import com.bean.simplenews.module.news.model.NewsModelBiz;
import com.bean.simplenews.module.news.model.NewsModel;
import com.bean.simplenews.module.news.view.NewsDetailView;

public class NewsDetailPresenter extends BasePresenter<NewsDetailView> implements NewsDetailPresenterBiz, NewsModel.OnLoadNewsDetailListener {

    private NewsModelBiz mNewsModel;

    public NewsDetailPresenter(NewsDetailView NewsDetailView) {
        onInitial(NewsDetailView);
    }

    @Override
    public void onInitial(NewsDetailView view) {
        super.onInitial(view);
        mNewsModel = new NewsModel();
    }

    @Override
    public void loadNewsDetail(final String docId) {
        obtainView().showProgress();
        mNewsModel.loadNewsDetail(docId, this);
    }

    @Override
    public void onSuccess(NewsDetailBean newsDetailBean) {
        if(newsDetailBean != null) {
            obtainView().showNewsDetailContent(newsDetailBean.getBody());
        }
        obtainView().hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        obtainView().hideProgress();
    }
}

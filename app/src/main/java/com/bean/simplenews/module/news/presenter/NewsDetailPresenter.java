package com.bean.simplenews.module.news.presenter;

import com.bean.simplenews.module.news.model.bean.NewsDetailBean;
import com.bean.simplenews.common.base.BasePresenter;
import com.bean.simplenews.module.news.model.NewsDetailHelper;
import com.bean.simplenews.module.news.view.NewsDetailView;

public class NewsDetailPresenter extends BasePresenter<NewsDetailView> implements NewsDetailPresenterBiz, NewsDetailHelper.OnLoadNewsDetailListener {

    public NewsDetailPresenter(NewsDetailView NewsDetailView) {
        onInitial(NewsDetailView);
    }

    @Override
    public void onInitial(NewsDetailView view) {
        super.onInitial(view);
    }

    @Override
    public void loadNewsDetail(final String docId) {
        obtainView().showProgress();
        NewsDetailHelper.getInstance().loadNewsDetail(docId,this);
    }

    @Override
    public void onSuccess(NewsDetailBean newsDetailBean) {
        if(newsDetailBean != null) {
            obtainView().showNewsDetailContent("<h3>"+newsDetailBean.getTitle()+newsDetailBean.getBody());
        }
        obtainView().hideProgress();
    }

    @Override
    public void onFailure(Throwable t) {
        obtainView().hideProgress();
    }
}

package com.bean.simplenews.module.news.presenter;

import android.os.Handler;
import android.util.Log;

import com.bean.simplenews.common.Constants;
import com.bean.simplenews.module.news.model.bean.NewsDetailBean;
import com.bean.simplenews.common.base.BasePresenter;
import com.bean.simplenews.module.news.model.NewsDetailHelper;
import com.bean.simplenews.module.news.view.NewsDetailView;
import com.bean.simplenews.util.ToastUtils;

import java.util.Iterator;

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (obtainView() != null){
                    obtainView().hideProgress();
                }
            }
        },2000);
        if(newsDetailBean != null) {
            obtainView().showNewsDetailContent(parse(newsDetailBean));
        }
    }

    @Override
    public void onFailure(Throwable t) {
        obtainView().hideProgress();
        obtainView().showFailContent();
    }

    private String parse(NewsDetailBean newsDetailBean) {
        String body = newsDetailBean.getBody();
        Iterator iterator = newsDetailBean.getImg().iterator();
        while ( iterator.hasNext()) {
            NewsDetailBean.Image img = (NewsDetailBean.Image) iterator.next();
            String ref = img.getRef();
            String src = img.getSrc();
            body = body.replace(ref,"<br/><img width=100% src=\"" + src + "\"/><br/>");
        }
        return Constants.prefix + "<h3>" + newsDetailBean.getTitle() + "<br/>" + body + Constants.suffix;
    }
}

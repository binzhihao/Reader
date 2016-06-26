package com.bean.simplenews.module.news.view;

import com.bean.simplenews.common.mvp.MVPView;

public interface NewsDetailView extends MVPView{

    void showNewsDetailContent(String newsDetailContent);

    void showProgress();

    void hideProgress();

}

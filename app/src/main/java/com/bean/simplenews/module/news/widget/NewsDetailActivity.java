package com.bean.simplenews.module.news.widget;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bean.simplenews.R;
import com.bean.simplenews.bean.NewsBean;
import com.bean.simplenews.common.Constants;
import com.bean.simplenews.common.base.BaseActivity;
import com.bean.simplenews.module.news.presenter.NewsDetailPresenter;
import com.bean.simplenews.module.news.view.NewsDetailView;
import com.bean.simplenews.util.ImageLoaderUtils;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailActivity extends BaseActivity<NewsDetailPresenter> implements NewsDetailView {

    @BindView(R.id.progress)
    ProgressBar mProgressBar;
    @BindView(R.id.htNewsContent)
    HtmlTextView mNewsContent;
    @BindView(R.id.ivImage)
    ImageView mImage;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        initPresenter(new NewsDetailPresenter(this));
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        setOptionalIconsVisible(menu);
        return true;
    }

    @Override
    public void showNewsDetailContent(String newsDetailContent) {
        mNewsContent.setHtmlFromString(newsDetailContent, new HtmlTextView.LocalImageGetter());
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void initView(){
        initToolbar(mToolbar,true);
        setTitle("");
        NewsBean newsBean = (NewsBean) getIntent().getSerializableExtra(Constants.NEWS);
        ImageLoaderUtils.display(getApplicationContext(), mImage, newsBean.getImgsrc());
        obtainPresenter().loadNewsDetail(newsBean.getDocid());
    }
}

package com.bean.simplenews.module.news.widget;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bean.simplenews.R;
import com.bean.simplenews.common.Constants;
import com.bean.simplenews.common.base.BaseActivity;
import com.bean.simplenews.module.news.model.bean.NewsBean;
import com.bean.simplenews.module.news.presenter.NewsDetailPresenter;
import com.bean.simplenews.module.news.view.NewsDetailView;
import com.bean.simplenews.util.ImageLoaderUtils;
import com.bean.simplenews.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailActivity extends BaseActivity<NewsDetailPresenter> implements NewsDetailView {

    @BindView(R.id.progress)
    ProgressBar mProgressBar;
    @BindView(R.id.htNewsContent)
    WebView mNewsContent;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        initPresenter(new NewsDetailPresenter(this));
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNewsContent.onPause();
    }

    @Override
    protected void onResume() {
        mNewsContent.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mNewsContent != null) {
            mNewsContent.destroy();
            mNewsContent = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        setOptionalIconsVisible(menu);
        return true;
    }

    @Override
    public void showNewsDetailContent(String newsDetailContent) {
        mNewsContent.loadDataWithBaseURL("", newsDetailContent,"text/html","utf-8","");
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
        mNewsContent.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                ToastUtils.makeToast(NewsDetailActivity.this,getString(R.string.load_fail));
                //super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        mNewsContent.setBackgroundColor(getResources().getColor(R.color.colorDayGray));
        WebSettings webSettings = mNewsContent.getSettings();
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        obtainPresenter().loadNewsDetail(getIntent().getStringExtra(Constants.NEWS));
    }

}

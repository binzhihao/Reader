package com.bean.simplenews.module.news.widget;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bean.simplenews.R;
import com.bean.simplenews.bean.NewsBean;
import com.bean.simplenews.common.Constants;
import com.bean.simplenews.module.news.presenter.NewsDetailPresenter;
import com.bean.simplenews.module.news.view.NewsDetailView;
import com.bean.simplenews.util.ImageLoaderUtils;
import com.bean.simplenews.util.ToolsUtil;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class NewsDetailActivity extends SwipeBackActivity implements NewsDetailView {

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

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeSize(ToolsUtil.getWidthInPx(this));
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        NewsBean newsBean = (NewsBean) getIntent().getSerializableExtra(Constants.NEWS);
        mCollapsingToolbar.setTitle(newsBean.getTitle());
        ImageLoaderUtils.display(getApplicationContext(), mImage, newsBean.getImgsrc());

        NewsDetailPresenter mNewsDetailPresenter = new NewsDetailPresenter(this);
        mNewsDetailPresenter.loadNewsDetail(newsBean.getDocid());
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
}

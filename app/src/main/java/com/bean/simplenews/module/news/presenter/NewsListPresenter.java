package com.bean.simplenews.module.news.presenter;

import android.content.Context;

import com.bean.simplenews.api.Urls;
import com.bean.simplenews.module.news.model.bean.NewsBean;
import com.bean.simplenews.common.Constants;
import com.bean.simplenews.common.base.BasePresenter;
import com.bean.simplenews.module.news.model.NewsListHelper;
import com.bean.simplenews.module.news.view.NewsListView;
import com.bean.simplenews.util.CacheUtils;
import com.bean.simplenews.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class NewsListPresenter extends BasePresenter<NewsListView> implements NewsListPresenterBiz, NewsListHelper.OnLoadNewsListListener {

    private String mCategory, mId;
    private Context mContext;
    private int mLastPageIndex = 0;
    private boolean mIsUp = false, mIsLoading = false, mLocalLock = false;

    public NewsListPresenter(NewsListView newsListView, int type, Context context) {
        onInitial(newsListView);
        mContext = context;
        switch (type) {
            case Constants.NEWS_TYPE_TOP:
                mCategory = Urls.CATEGORY_TOP;
                mId = Urls.TOP_ID;
                break;
            case Constants.NEWS_TYPE_NBA:
                mCategory = Urls.CATEGORY_COMMON;
                mId = Urls.NBA_ID;
                break;
            case Constants.NEWS_TYPE_TEC:
                mCategory = Urls.CATEGORY_COMMON;
                mId = Urls.TEC_ID;
                break;
            case Constants.NEWS_TYPE_FIN:
                mCategory = Urls.CATEGORY_COMMON;
                mId = Urls.FIN_ID;
                break;
            default:
                mCategory = Urls.CATEGORY_TOP;
                mId = Urls.TOP_ID;
        }
    }

    @Override
    public void onInitial(NewsListView view) {
        super.onInitial(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void loadNews(int pageIndex, boolean isUp) {
        //与上次请求相同且还在loading状态则跳过避免重复请求，或者正在本地加载也跳过
        if ((mLastPageIndex == pageIndex && mIsLoading) || mLocalLock) {
            return;
        }
        //取消正在进行的该类别的所有网络请求避免缓存窗口滑动冲突
        NewsListHelper.getInstance().cancel(mId);
        mIsLoading = true;
        Object list;
        if ((list = CacheUtils.get(mContext.getApplicationContext()).getAsObject(mId + pageIndex)) != null) {
            onLocalSuccess((ArrayList<NewsBean>) list, isUp);
        } else {
            NewsListHelper.getInstance().loadNews(mCategory, mId, pageIndex, this, isUp);
        }
        mLastPageIndex = pageIndex;
    }

    @Override
    public void onSuccess(List<NewsBean> list, int pageIndex, boolean isUp) {
        if (isViewAttached()) {
            obtainView().setNewsData(list, isUp);
            obtainView().showLoadSuccess();
            CacheUtils.get(mContext.getApplicationContext()).
                    put(mId + pageIndex, (ArrayList<NewsBean>) list);
        }
        mIsLoading = false;
    }

    @Override
    public void onFailure(Throwable t) {
        if (isViewAttached()) {
            obtainView().showLoadFailure();
        }
        mIsLoading = false;
    }

    public void clearCache() {
        CacheUtils.get(mContext.getApplicationContext()).clear();
    }

    private void onLocalSuccess(ArrayList<NewsBean> list, boolean isUp) {
        mLocalLock = true;
        if (isViewAttached()) {
            obtainView().setNewsData(list, isUp);
        }
        mLocalLock = false;
        mIsLoading = false;
    }

}

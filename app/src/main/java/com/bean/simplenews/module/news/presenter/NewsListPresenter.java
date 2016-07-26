package com.bean.simplenews.module.news.presenter;

import com.bean.simplenews.api.Urls;
import com.bean.simplenews.module.news.model.bean.NewsBean;
import com.bean.simplenews.common.Constants;
import com.bean.simplenews.common.base.BasePresenter;
import com.bean.simplenews.module.news.model.NewsListHelper;
import com.bean.simplenews.module.news.view.NewsListView;
import com.bean.simplenews.util.LogUtils;

import java.util.List;

public class NewsListPresenter extends BasePresenter<NewsListView> implements NewsListPresenterBiz, NewsListHelper.OnLoadNewsListListener {

    private int mType;
    private String mCategory,mId;

    public NewsListPresenter(NewsListView newsListView,int type) {
        onInitial(newsListView);
        mType=type;
        switch (mType){
            case Constants.NEWS_TYPE_TOP:
                mCategory=Urls.CATEGORY_TOP;
                mId=Urls.TOP_ID;
                break;
            case Constants.NEWS_TYPE_NBA:
                mCategory=Urls.CATEGORY_COMMON;
                mId=Urls.NBA_ID;
                break;
            case Constants.NEWS_TYPE_TEC:
                mCategory=Urls.CATEGORY_COMMON;
                mId=Urls.TEC_ID;
                break;
            case Constants.NEWS_TYPE_FIN:
                mCategory=Urls.CATEGORY_COMMON;
                mId=Urls.FIN_ID;
                break;
            default:
                mCategory=Urls.CATEGORY_TOP;
                mId=Urls.TOP_ID;
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
    public void loadNews(int pageIndex) {
        if(pageIndex == 0) {  //首次加载或手动刷新无条件加载
            cancelAll();
            NewsListHelper.getInstance().loadNews(mCategory,mId,pageIndex,this);
        }else{
            if(NewsListHelper.getInstance().isCallExist(mId+pageIndex)){
                LogUtils.e("fuck","http call is already exist");
                return;
            }
            obtainView().showFooterProgress();
            NewsListHelper.getInstance().loadNews(mCategory,mId,pageIndex,this);
        }
    }

    @Override
    public void onSuccess(List<NewsBean> list) {
        if(isViewAttached()) {
            obtainView().addNews(list);
            obtainView().showLoadSuccess();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if(isViewAttached()) {
            obtainView().showLoadFailure();
        }
    }

    //清除某一个请求
    public void cancel(int pageIndex){
        NewsListHelper.getInstance().cancel(mId+pageIndex);
    }

    //清除某一个类别的所有请求
    public void cancelAll(){
        NewsListHelper.getInstance().cancelAll(mId);
    }
}

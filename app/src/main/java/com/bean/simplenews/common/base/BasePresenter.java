package com.bean.simplenews.common.base;

import com.bean.simplenews.common.mvp.MVPPresenter;
import com.bean.simplenews.common.mvp.MVPView;
import com.bean.simplenews.util.LogUtils;

public class BasePresenter<V extends MVPView> implements MVPPresenter<V> {

    private V MVPView;

    public void onInitial(V view) {
        attachView(view);
    }

    public void onDestroy() {
        detachView();
    }

    public boolean isViewAttached(){
        return MVPView!=null;
    }

    @Override
    public void attachView(V view) {
        MVPView = view;
    }

    @Override
    public void detachView() {
        MVPView = null;
    }

    @Override
    public V obtainView() {
        return MVPView;
    }

}


package com.bean.simplenews.common.base;

import com.bean.simplenews.common.mvp.MVPPresenter;
import com.bean.simplenews.common.mvp.MVPView;

public class BasePresenter<V extends MVPView> implements MVPPresenter<V> {

    private V MVPView;

    public void onInitial(V view) {
        attachView(view);
        // TODO: other initialization
    }

    public void onDestroy() {
        detachView();
        // TODO: other actions
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


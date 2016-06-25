package com.bean.simplenews.common;

public class BasePresenter<V extends MVPView> implements MVPPresenter<V> {

    private V MVPView;

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


package com.bean.simplenews.common;

public interface MVPPresenter<V extends MVPView>{
	
	void attachView(V MVPView);
	
	void detachView();

	V obtainView();

}
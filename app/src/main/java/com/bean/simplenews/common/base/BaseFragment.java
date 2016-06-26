package com.bean.simplenews.common.base;

import android.support.v4.app.Fragment;

import com.bean.simplenews.util.LogUtils;

public class BaseFragment<P extends BasePresenter> extends Fragment{

    private P Presenter;

    protected boolean initPresenter(P presenter){
        if(presenter==null){
            LogUtils.e("BaseFragment","presenter is null");
            return false;
        }
        Presenter=presenter;
        return true;
    }

    protected P obtainPresenter(){
        return Presenter;
    }

    @Override
    public void onDestroy() {
        if(Presenter!=null){
            Presenter.onDestroy();
            Presenter=null;
        }
        super.onDestroy();
    }
}

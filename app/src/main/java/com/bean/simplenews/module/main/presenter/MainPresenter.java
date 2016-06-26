package com.bean.simplenews.module.main.presenter;

import com.bean.simplenews.R;
import com.bean.simplenews.common.base.BasePresenter;
import com.bean.simplenews.module.main.view.MainView;

public class MainPresenter extends BasePresenter<MainView> implements IMainPresenter{

    public MainPresenter(MainView MainView) {
        onInitial(MainView);
    }

    @Override
    public void switchNavigation(int id){
        switch (id) {
            case R.id.navigation_item_news:
                obtainView().switch2News();
                break;
            case R.id.navigation_item_about:
                obtainView().switch2About();
                break;
            default:
                obtainView().switch2News();
                break;
        }
    }
}

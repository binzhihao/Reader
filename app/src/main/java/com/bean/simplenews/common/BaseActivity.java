package com.bean.simplenews.common;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import java.lang.reflect.Method;

public class BaseActivity<P extends BasePresenter> extends AppCompatActivity{

    protected P mPresenter;

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            // 通过反射获取方法令icon显示出来
            if (menu.getClass().getSimpleName().endsWith("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {}
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    protected  void initToolbar(Toolbar toolbar){
        setSupportActionBar(toolbar);
    }

    protected void initToolbar(Toolbar toolbar, DrawerLayout drawerLayout, final View main,
                               final NavigationView navigationView, int openStrRes, int closeStrRes){
        initToolbar(toolbar);
        // drawer
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                openStrRes, closeStrRes) {
            @Override
            public void onDrawerSlide(View view, float offset) {
                super.onDrawerSlide(view, offset);
                main.scrollTo(-(int) (navigationView.getWidth() * offset), 0);
            }
        };
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
    }

}

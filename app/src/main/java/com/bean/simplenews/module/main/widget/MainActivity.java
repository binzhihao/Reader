package com.bean.simplenews.module.main.widget;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.bean.simplenews.R;
import com.bean.simplenews.common.Constants;
import com.bean.simplenews.common.base.BaseActivity;
import com.bean.simplenews.module.about.AboutFragment;
import com.bean.simplenews.module.main.presenter.MainPresenter;
import com.bean.simplenews.module.main.view.MainView;
import com.bean.simplenews.module.news.widget.NewsFragment;
import com.bean.simplenews.module.image.ImageFragment;
import com.bean.simplenews.module.video.VideoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frame_content)
    FrameLayout mFrameContent;
    @BindView(R.id.main_content)
    CoordinatorLayout mMainContent;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initPresenter(new MainPresenter(this));
        initView();
        setDoubleToExit(true);
        switch2News();  // 初始页面
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        setOptionalIconsVisible(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_mode:
                if(!Constants.NIGHT) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Constants.NIGHT=true;
                    recreate();
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Constants.NIGHT = false;
                    recreate();
                }
                break;
            case R.id.action_settings:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void switch2News() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new NewsFragment()).commit();
        mToolbar.setTitle(R.string.navigation_news);
    }

    @Override
    public void switch2About() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new AboutFragment()).commit();
        mToolbar.setTitle(R.string.navigation_about);
    }

    @Override
    public void switch2Pic() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new ImageFragment()).commit();
        mToolbar.setTitle(R.string.navigation_images);
    }

    @Override
    public void switch2video() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new VideoFragment()).commit();
        mToolbar.setTitle(R.string.navigation_video);
    }

    private void initView(){
        initToolbar(mToolbar,mDrawerLayout,mMainContent,mNavigationView,R.string.drawer_open,R.string.drawer_close);
        //setTitle(R.string.navigation_news);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        obtainPresenter().switchNavigation(menuItem.getItemId());
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
}

package com.bean.simplenews.module.main.widget;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bean.simplenews.R;
import com.bean.simplenews.module.about.AboutFragment;
import com.bean.simplenews.module.image.widget.ImageFragment;
import com.bean.simplenews.module.main.presenter.MainPresenter;
import com.bean.simplenews.module.main.presenter.MainPresenterImpl;
import com.bean.simplenews.module.main.view.MainView;
import com.bean.simplenews.module.news.widget.NewsFragment;

public class MainActivity extends AppCompatActivity implements MainView {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainPresenter = new MainPresenterImpl(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(R.string.navigation_news);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close){
            int width;
            View content = (View)findViewById(R.id.main_content);
            @Override
            public void onDrawerSlide(View view, float offset){
                super.onDrawerSlide(view, offset);
                width = mNavigationView.getWidth();
                content.scrollTo(-(int)(mNavigationView.getWidth()*offset), 0);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mMainPresenter.switchNavigation(menuItem.getItemId());
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        switch2News();  // 初始页面
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // TODO
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* implement MainView */

    @Override
    public void switch2News() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new NewsFragment()).commit();
        mToolbar.setTitle(R.string.navigation_news);
    }

    @Override
    public void switch2Images() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new ImageFragment()).commit();
        mToolbar.setTitle(R.string.navigation_images);
    }

    @Override
    public void switch2About() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new AboutFragment()).commit();
        mToolbar.setTitle(R.string.navigation_about);
    }
}

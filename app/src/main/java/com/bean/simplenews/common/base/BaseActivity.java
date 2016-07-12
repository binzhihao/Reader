package com.bean.simplenews.common.base;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.bean.simplenews.R;
import com.bean.simplenews.common.Constants;
import com.bean.simplenews.common.mvp.MVPView;
import com.bean.simplenews.util.LogUtils;
import com.bean.simplenews.util.ToastUtils;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

public class BaseActivity<P extends BasePresenter> extends AppCompatActivity{

    private P Presenter;
    private boolean isExit=false;
    private boolean isDoubleToExit=false;

    @Override
    protected void onDestroy() {
        if(Presenter!=null){
            Presenter.onDestroy();
            Presenter=null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(!isDoubleToExit){
            super.onBackPressed();
        }else if (!isExit) {
            isExit=true;
            ToastUtils.makeToast(this,getString(R.string.exit_message));
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                } }, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    protected void setDoubleToExit(boolean bool){
        isDoubleToExit=bool;
    }

    // this method must be called in every activity
    protected boolean initPresenter(P presenter){
        if(presenter==null){
            LogUtils.e("BaseActivity","presenter is null");
            return false;
        }
        Presenter=presenter;
        return true;
    }

    protected P obtainPresenter(){
        return Presenter;
    }

    protected void setOptionalIconsVisible(Menu menu) {
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
    }

    protected  void initToolbar(Toolbar toolbar){
        setSupportActionBar(toolbar);
    }

    protected void initToolbar(Toolbar toolbar,boolean upAsHome){
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(upAsHome);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
                main.scrollTo(-(int) (navigationView.getWidth() * offset), 0); //轻微影响性能
            }
        };
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
    }

}

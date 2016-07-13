package com.bean.simplenews.common.base;

import android.app.Application;
import android.content.Context;

public class BaseApp extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=super.getApplicationContext();
    }

    public static Context getGlobalContext(){
        return mContext;
    }
}

package com.bean.simplenews.common.base;

import android.app.Application;
import android.graphics.Typeface;

public class BaseApp extends Application{

    private static Typeface typeFace;

    @Override
    public void onCreate() {
        super.onCreate();
        typeFace = Typeface.createFromAsset(getAssets(),"fonts/1mRegular.ttf");
    }

    public static Typeface getTypeface(){
        return typeFace;
    }
}

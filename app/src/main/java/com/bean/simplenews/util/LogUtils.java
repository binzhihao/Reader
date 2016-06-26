package com.bean.simplenews.util;

import android.util.Log;

import com.bean.simplenews.common.Constants;

public class LogUtils {

    public static void v(String tag, String message) {
        if(Constants.DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if(Constants.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if(Constants.DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if(Constants.DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if(Constants.DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Exception e) {
        if(Constants.DEBUG) {
            Log.e(tag, message, e);
        }
    }
}

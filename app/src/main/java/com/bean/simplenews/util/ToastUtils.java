package com.bean.simplenews.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    public static void makeToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}

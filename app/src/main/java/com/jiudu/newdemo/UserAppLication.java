package com.jiudu.newdemo;

import android.app.Application;
import android.content.Context;

public class UserAppLication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}

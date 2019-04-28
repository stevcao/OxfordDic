package com.clz.oxforddic.app;

import android.app.Application;
import android.content.Context;

/**
 * Create by stevcao on 2019/4/28
 */
public class BaseApplication extends Application {

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}

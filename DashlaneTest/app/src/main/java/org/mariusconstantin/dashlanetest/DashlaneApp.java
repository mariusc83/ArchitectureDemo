package org.mariusconstantin.dashlanetest;

import android.app.Application;

/**
 * Created by MConstantin on 1/4/2016.
 */
public class DashlaneApp extends Application {
    private static DashlaneApp mInstance;
    private final AppInjector mAppInjector=new AppInjector();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static DashlaneApp getInstance() {
        return mInstance;
    }

    public AppInjector getAppInjector() {
        return mAppInjector;
    }
}

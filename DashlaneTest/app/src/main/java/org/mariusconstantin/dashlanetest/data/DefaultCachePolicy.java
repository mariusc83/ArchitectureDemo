package org.mariusconstantin.dashlanetest.data;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Marius on 1/3/2016.
 */
public class DefaultCachePolicy implements ICachePolicy {

    private final int mLruCacheSize;

    public DefaultCachePolicy(@NonNull Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        int maxKb = am.getMemoryClass() * 1024;
        mLruCacheSize = maxKb / 8; // 1/8th of total ram
    }


    @Override
    public boolean isLruCacheEnabled() {
        return true;
    }

    @Override
    public boolean isDiskCacheEnabled() {
        return false;
    }

    @Override
    public long cacheExpireTime() {
        return 0;
    }

    @Override
    public int maxLruCacheSize() {
        return mLruCacheSize;
    }
}

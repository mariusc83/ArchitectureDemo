package org.mariusconstantin.dashlanetest;

import android.content.Context;

import org.mariusconstantin.dashlanetest.data.DataProvider;
import org.mariusconstantin.dashlanetest.data.IDataProvider;
import org.mariusconstantin.dashlanetest.helpers.IPathHelper;
import org.mariusconstantin.dashlanetest.helpers.PathHelper;

/**
 * Created by MConstantin on 1/4/2016.
 */
// TODO: 1/5/2016 We could use Dagger2 instead of this Injector
public class AppInjector {

    private IDataProvider mDataProvider;
    private IAppConfig mAppConfig;
    private IPathHelper mPathHelper;

    public IAppConfig getAppConfig() {
        if (mAppConfig == null)
            mAppConfig = new AppConfig();
        return mAppConfig;
    }

    public IDataProvider inject(Context context) {
        if (mDataProvider == null)
            mDataProvider = new MockDataProdiver();
        return mDataProvider; // keep only a single reference to this to persist the Cached Data between activities
    }

    public IPathHelper getPathHelper() {
        if (mPathHelper == null)
            mPathHelper = new PathHelper(getAppConfig());
        return mPathHelper;
    }
}

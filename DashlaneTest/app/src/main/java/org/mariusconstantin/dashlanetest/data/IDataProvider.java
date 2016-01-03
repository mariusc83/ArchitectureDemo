package org.mariusconstantin.dashlanetest.data;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import org.mariusconstantin.dashlanetest.data.models.IWebsiteModel;
import org.mariusconstantin.dashlanetest.network.IApiEndpoints;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * IDataProvider interface. This will be used to retrieve cached/network data as needed
 * Created by Marius on 1/1/2016.
 */
public interface IDataProvider extends ComponentCallbacks {

    void getWebsites(long id, IDataProviderListCallback<IWebsiteModel> callback);

    void clearCache();

    @NonNull
    ICachePolicy getDefaultCachePolicy(@NonNull Context context);

    @NonNull
    ExecutorService getDefaultExecutor();

    interface IDataProviderListCallback<T> {
        @MainThread
        void onSuccess(List<T> response);

        @MainThread
        void onError(Exception e);
    }
}

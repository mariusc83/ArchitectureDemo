package org.mariusconstantin.dashlanetest.data;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.util.LruCache;

import org.mariusconstantin.dashlanetest.IAppConfig;
import org.mariusconstantin.dashlanetest.data.deserizalizers.IModelDeserializer;
import org.mariusconstantin.dashlanetest.data.deserizalizers.UnsupportedDataFormatException;
import org.mariusconstantin.dashlanetest.data.deserizalizers.WebsiteModelDeserializer;
import org.mariusconstantin.dashlanetest.data.models.ICachedModel;
import org.mariusconstantin.dashlanetest.data.models.IWebsiteModel;
import org.mariusconstantin.dashlanetest.network.IApiEndpoints;
import org.mariusconstantin.dashlanetest.network.ToStringConverterFactory;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Marius on 1/3/2016.
 */
public class DataProvider implements IDataProvider {

    private static final String TAG = DataProvider.class.getSimpleName();
    private static final String WEBSITE_DATA_KEY_PREFIX = "WB_";

    private final Handler mUIHandler = new Handler(Looper.getMainLooper());

    @NonNull
    private final ExecutorService mExecutorService;
    @NonNull
    private final Map<Class<?>, IModelDeserializer<?>> mCustomDeserializers;
    @NonNull
    private final ICachePolicy mCachePolicy;

    @NonNull
    private final IApiEndpoints mApiEndpoints;

    private DataLruCache mLruCache;
    private final Object mLruCacheLocker = new Object();

    private DataProvider(Builder builder) {
        mCachePolicy = builder.mCachePolicy != null ? builder.mCachePolicy : getDefaultCachePolicy(builder.mContext);
        mExecutorService = builder.mExecutorService != null ? builder.mExecutorService : getDefaultExecutor();
        mCustomDeserializers = builder.mCustomDeserializers;
        mApiEndpoints = new Retrofit.Builder().baseUrl(builder.mApiEndpointUrl).addConverterFactory(new ToStringConverterFactory()).build().create(IApiEndpoints.class);
        init();
    }


    @SuppressWarnings("unchecked")
    @VisibleForTesting
    @Override
    public void getWebsites(final long id, final IDataProviderListCallback<IWebsiteModel> callback) {
        // first we check the internal cache
        final String cacheId = generateId(IWebsiteModel.class, id);
        if (mCachePolicy.isLruCacheEnabled()) {
            final List<? extends ICachedModel> cachedData = getFromCache(cacheId);
            if (cachedData != null) {
                callback.onSuccess((List<IWebsiteModel>) cachedData);
                return;
            }
        }

        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                // check deserializer
                if (!mCustomDeserializers.containsKey(IWebsiteModel.class))
                    mCustomDeserializers.put(IWebsiteModel.class, new WebsiteModelDeserializer());

                try {
                    final Call<String> call = mApiEndpoints.getWebsites(id);
                    final Response<String> response = call.execute();
                    if (response.isSuccess()) {
                        final String data = response.body();
                        final String[] dataArray = data.split("[\\r\\n]+");
                        if (dataArray.length > 0) {
                            final IModelDeserializer<IWebsiteModel> deserializer = (IModelDeserializer<IWebsiteModel>) mCustomDeserializers.get(IWebsiteModel.class);
                            final List<IWebsiteModel> toReturn = new ArrayList<>();
                            for (int i = 0; i < dataArray.length; i = i + 2) {
                                try {
                                    toReturn.add(deserializer.fetchModel(dataArray[i]));
                                } catch (UnsupportedDataFormatException e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            }

                            // cache it if available
                            if (mCachePolicy.isLruCacheEnabled()) {
                                addToCache(cacheId, toReturn);
                            }
                            mUIHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(toReturn);
                                }
                            });
                            return;
                        }
                    } else {
                        throw new ConnectException(response.errorBody().string());
                    }
                    dispatchSuccess(callback, null);
                } catch (final IOException e) {
                    dispatchError(callback, e);
                }

            }
        });
    }

    @NonNull
    @Override
    public ICachePolicy getDefaultCachePolicy(@NonNull Context context) {
        return new DefaultCachePolicy(context);
    }

    @NonNull
    @Override
    public ExecutorService getDefaultExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Override
    public void clearCache() {
        synchronized (mLruCacheLocker) {
            mLruCache.evictAll();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {
        // clear the LRU cache here
        mLruCache.evictAll();
    }

    // private
    private void init() {
        if (mCachePolicy.isLruCacheEnabled()) {
            mLruCache = new DataLruCache(mCachePolicy.maxLruCacheSize());
        }
    }

    private List<? extends ICachedModel> getFromCache(String id) {
        synchronized (mLruCacheLocker) {
            return mLruCache.get(id);
        }
    }

    private void addToCache(String id, List<? extends ICachedModel> data) {
        synchronized (mLruCacheLocker) {
            mLruCache.put(id, data);
        }
    }

    private void dispatchError(final IDataProviderListCallback<?> callback, final Exception e) {
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(e);
            }
        });
    }

    private <T> void dispatchSuccess(final IDataProviderListCallback<T> callback, final List<T> response) {
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response);
            }
        });
    }

    private String generateId(Class<?> modelClass, long id) {
        if (modelClass.equals(IWebsiteModel.class)) {
            return WEBSITE_DATA_KEY_PREFIX + id;
        }
        return String.valueOf(id);
    }

    private class DataLruCache extends LruCache<String, List<? extends ICachedModel>> {

        public DataLruCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(String key, List<? extends ICachedModel> value) {
            int size = 0;
            for (ICachedModel model : value) {
                size += model.memorySize();
            }
            return size;
        }
    }

    public static final class Builder {
        private final Context mContext;
        private final Map<Class<?>, IModelDeserializer<?>> mCustomDeserializers = new HashMap<>();
        private final String mApiEndpointUrl;
        private ICachePolicy mCachePolicy;
        private ExecutorService mExecutorService;

        public Builder(IAppConfig config, Context activity) {
            this.mApiEndpointUrl = config.getEndpointsRootUrl();
            mContext = activity;
        }

        public Builder cachePolicy(ICachePolicy mCachePolicy) {
            this.mCachePolicy = mCachePolicy;
            return this;
        }

        public <T> Builder setDeserializer(Class<T> tClass, IModelDeserializer<T> deserializer) {
            if (!mCustomDeserializers.containsKey(tClass))
                mCustomDeserializers.put(tClass, deserializer);
            return this;
        }

        public Builder setExecutorService(ExecutorService mExecutorService) {
            this.mExecutorService = mExecutorService;
            return this;
        }

        public DataProvider build() {
            return new DataProvider(this);
        }
    }
}

package org.mariusconstantin.dashlanetest;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import org.mariusconstantin.dashlanetest.data.ICachePolicy;
import org.mariusconstantin.dashlanetest.data.IDataProvider;
import org.mariusconstantin.dashlanetest.data.models.IWebsiteModel;
import org.mariusconstantin.dashlanetest.data.models.WebsiteModel;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by MConstantin on 1/7/2016.
 */
public class MockDataProdiver implements IDataProvider {
    public static final List<IWebsiteModel> MOCKED_DATA = Arrays.asList(
            getWebsiteModel("000webhost_com.png", "000webhost.com"),
            getWebsiteModel("1and1_fr.png", "1and1.fr"),
            getWebsiteModel("1euro_com.png", "1euro.com"),
            getWebsiteModel("1und1_de.png", "1und1.de")
    );

    @Override
    public void getWebsites(long id, IDataProviderListCallback<IWebsiteModel> callback) {
        callback.onSuccess(MOCKED_DATA);
    }

    @Override
    public void clearCache() {

    }

    @NonNull
    @Override
    public ICachePolicy getDefaultCachePolicy(@NonNull Context context) {
        return null;
    }

    @NonNull
    @Override
    public ExecutorService getDefaultExecutor() {
        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    private static IWebsiteModel getWebsiteModel(String logoId, String title) {
        return new WebsiteModel(logoId, title);
    }
}

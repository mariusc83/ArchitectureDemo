package org.mariusconstantin.dashlanetest.activities.mainactivity;

import android.support.annotation.NonNull;

import org.mariusconstantin.dashlanetest.data.IDataProvider;
import org.mariusconstantin.dashlanetest.data.models.IWebsiteModel;
import org.mariusconstantin.dashlanetest.presenter.IContract;

/**
 * Created by Marius on 1/3/2016.
 */
public interface IMainActivityContract {
    interface IPresenter extends IContract.IContainerPresenter {
        @NonNull
        IDataProvider getDataProvider();

        void filterWebAddresses(String inputValue);

        void navigateToAddressDetails(@NonNull IWebsiteModel model, boolean isTablet);
    }
}

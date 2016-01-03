package org.mariusconstantin.dashlanetest.fragments.addresseslist;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import org.mariusconstantin.dashlanetest.activities.mainactivity.IMainActivityContract;
import org.mariusconstantin.dashlanetest.data.IDataProvider;
import org.mariusconstantin.dashlanetest.data.models.IWebsiteModel;

import java.util.List;

/**
 * Created by Marius on 1/3/2016.
 */
public class WebAddressListPresenter implements IWebAddressListContract.IPresenter<IMainActivityContract.IPresenter, IWebAddressListContract.IView> {
    private final IMainActivityContract.IPresenter mMainActivityPresenter;
    private IWebAddressListContract.IView mView;

    public WebAddressListPresenter(IMainActivityContract.IPresenter mainActivityPresenter) {
        this.mMainActivityPresenter = mainActivityPresenter;
    }

    @NonNull
    @MainThread
    @Override
    public IMainActivityContract.IPresenter getParent() {
        return mMainActivityPresenter;
    }

    @Override
    public void onAttach(@NonNull IWebAddressListContract.IView childView) {
        mView = childView;
        getParent().addChild(childView);
    }

    @Override
    public void onDetach(@NonNull IWebAddressListContract.IView childView) {
        mView = null;
        getParent().removeChild(childView);
    }

    @Override
    public void requestAdapterData(long pageId) {
        getParent().getDataProvider().getWebsites(pageId, new IDataProvider.IDataProviderListCallback<IWebsiteModel>() {
            @Override
            public void onSuccess(List<IWebsiteModel> response) {
                mView.refreshAdapterData(response);
            }

            @Override
            public void onError(Exception e) {
                // TODO: 1/4/2016 Display a toast message here
            }
        });
    }
}

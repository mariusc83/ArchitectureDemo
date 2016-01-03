package org.mariusconstantin.dashlanetest.activities.mainactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.mariusconstantin.dashlanetest.DashlaneApp;
import org.mariusconstantin.dashlanetest.data.IDataProvider;
import org.mariusconstantin.dashlanetest.data.models.IWebsiteModel;
import org.mariusconstantin.dashlanetest.fragments.addressdetail.AddressDetailFragment;
import org.mariusconstantin.dashlanetest.fragments.addressdetail.AddressDialogDetailFragment;
import org.mariusconstantin.dashlanetest.fragments.addresseslist.IWebAddressListContract;
import org.mariusconstantin.dashlanetest.fragments.addresseslist.WebAddressListFragment;
import org.mariusconstantin.dashlanetest.presenter.IContract;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marius on 1/3/2016.
 */
public class MainActivityPresenter implements IMainActivityContract.IPresenter {
    private final Set<IContract.IChildView> mChildren = new HashSet<>();
    private
    @NonNull
    final IContract.IView mView;

    private
    @NonNull
    final IDataProvider mDataProvider;

    public MainActivityPresenter(@NonNull Context context, @NonNull IContract.IView view) {
        mView = view;
        mDataProvider = DashlaneApp.getInstance().getAppInjector().inject(context.getApplicationContext());
    }

    @NonNull
    @Override
    public IDataProvider getDataProvider() {
        return mDataProvider;
    }

    @Override
    public void filterWebAddresses(String inputValue) {
        if (mChildren != null) {
            for (IContract.IChildView childView : mChildren) {
                if (childView instanceof IWebAddressListContract.IView) {
                    ((IWebAddressListContract.IView) childView).performFilter(inputValue);
                }
            }
        }
    }

    @Override
    public void navigateToAddressDetails(@NonNull IWebsiteModel model, boolean isTablet) {
        if (isTablet)
            mView.showDialogFragment(AddressDialogDetailFragment.newInstance(model));
        else
            mView.switchToFragment(AddressDetailFragment.newInstance(model));
    }

    @Override
    public <T extends IContract.IChildView> void addChild(T childView) {
        mChildren.add(childView);
    }

    @Override
    public <T extends IContract.IChildView> boolean removeChild(T childView) {
        return mChildren.remove(childView);
    }

    @Override
    public void onCreate(Context context, Bundle bundle) {
        if (bundle == null)
            mView.switchToFragment(WebAddressListFragment.newInstance(2532281));
        context.registerComponentCallbacks(mDataProvider);
    }

    @Override
    public void onDestroy(Context context) {
        context.unregisterComponentCallbacks(mDataProvider);
    }
}

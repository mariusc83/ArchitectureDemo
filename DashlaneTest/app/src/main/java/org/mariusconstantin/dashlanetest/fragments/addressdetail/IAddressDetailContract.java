package org.mariusconstantin.dashlanetest.fragments.addressdetail;

import android.content.Context;
import android.support.annotation.NonNull;

import org.mariusconstantin.dashlanetest.presenter.IContract;

/**
 * Created by MConstantin on 1/5/2016.
 */
public interface IAddressDetailContract {
    interface IView extends IContract.IChildView {
        void showTitle(@NonNull String title);

        void showLogo(@NonNull String logo);

        Context getContext();
    }

    interface IPresenter<T extends IContract.IContainerPresenter, D extends IContract.IChildView> extends IContract.IChildPresenter<T, D> {
        void refreshView(IView view);

        D getView();
    }
}

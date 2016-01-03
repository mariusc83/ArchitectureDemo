package org.mariusconstantin.dashlanetest.fragments.addresseslist;

import org.mariusconstantin.dashlanetest.data.models.IWebsiteModel;
import org.mariusconstantin.dashlanetest.presenter.IContract;

import java.util.List;

/**
 * Created by Marius on 1/3/2016.
 */
public interface IWebAddressListContract {
    interface IView extends IContract.IChildView {
        void refreshAdapterData(List<IWebsiteModel> data);

        void performFilter(String inputValue);

    }

    interface IPresenter<T extends IContract.IContainerPresenter, D extends IContract.IChildView> extends IContract.IChildPresenter<T, D> {
        void requestAdapterData(long pageId);
    }
}

package org.mariusconstantin.dashlanetest.fragments.addressdetail;

import android.support.annotation.NonNull;

import org.mariusconstantin.dashlanetest.DashlaneApp;
import org.mariusconstantin.dashlanetest.activities.mainactivity.IMainActivityContract;
import org.mariusconstantin.dashlanetest.data.models.IWebsiteModel;
import org.mariusconstantin.dashlanetest.helpers.IPathHelper;

/**
 * Created by MConstantin on 1/5/2016.
 */
public class AddressDetailPresenter implements IAddressDetailContract.IPresenter<IMainActivityContract.IPresenter, IAddressDetailContract.IView> {
    IAddressDetailContract.IView mView;
    private final IMainActivityContract.IPresenter mMainActivityPresenter;
    private final IWebsiteModel mModel;
    private final IPathHelper mPathHelper;

    public AddressDetailPresenter(IMainActivityContract.IPresenter mMainActivityPresenter, IWebsiteModel mModel) {
        this.mMainActivityPresenter = mMainActivityPresenter;
        this.mModel = mModel;
        mPathHelper = DashlaneApp.getInstance().getAppInjector().getPathHelper();
    }

    @Override
    public void refreshView(IAddressDetailContract.IView view) {
        view.showLogo(mPathHelper.getLogoPath(mModel.getLogoId()));
        view.showTitle(mModel.getTitle());
    }


    @Override
    public IAddressDetailContract.IView getView() {
        return mView;
    }

    @NonNull
    @Override
    public IMainActivityContract.IPresenter getParent() {
        return mMainActivityPresenter;
    }

    @Override
    public void onAttach(@NonNull IAddressDetailContract.IView childView) {
        mView = childView;
        refreshView(childView);
    }

    @Override
    public void onDetach(@NonNull IAddressDetailContract.IView childView) {
        mView = childView;
    }
}

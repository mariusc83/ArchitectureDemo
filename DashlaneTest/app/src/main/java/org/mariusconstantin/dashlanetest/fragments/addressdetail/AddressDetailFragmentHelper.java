package org.mariusconstantin.dashlanetest.fragments.addressdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.mariusconstantin.dashlanetest.AppConfig;
import org.mariusconstantin.dashlanetest.DashlaneApp;
import org.mariusconstantin.dashlanetest.IAppConfig;
import org.mariusconstantin.dashlanetest.R;
import org.mariusconstantin.dashlanetest.activities.mainactivity.IMainActivityContract;
import org.mariusconstantin.dashlanetest.data.models.IWebsiteModel;
import org.mariusconstantin.dashlanetest.presenter.IContract;

/**
 * Created by MConstantin on 1/5/2016.
 */
public class AddressDetailFragmentHelper implements IAddressDetailContract.IView {
    public static final String MODEL_KEY = "m_key";
    private TextView mTitleView;
    private ImageView mLogoView;
    private AddressDetailPresenter mPresenter;
    private IAppConfig mAppConfig;


    public void onActivityCreated(Fragment fragment) {
        mAppConfig = DashlaneApp.getInstance().getAppInjector().getAppConfig();
        mPresenter = new AddressDetailPresenter((IMainActivityContract.IPresenter) ((IContract.IView) fragment.getActivity()).getPresenter(), (IWebsiteModel) fragment.getArguments().getParcelable(MODEL_KEY));
        mPresenter.onAttach((IAddressDetailContract.IView) fragment);
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTitleView = (TextView) view.findViewById(android.R.id.text1);
        mLogoView = (ImageView) view.findViewById(R.id.image);
    }

    public void onDestroyView() {
        mLogoView = null;
        mTitleView = null;
    }

    public void onDetach(IAddressDetailContract.IView fragment) {
        mPresenter.onDetach(fragment);
    }

    @Override
    public void showTitle(@NonNull String title) {
        if (mTitleView != null)
            mTitleView.setText(title);
    }


    @Override
    public void showLogo(@NonNull String logo) {
        if (!mAppConfig.isMock()) {
            if (mLogoView != null)
                Picasso.with(mPresenter.getView().getContext()).load(logo).into(mLogoView);
        } else {
            mLogoView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public Context getContext() {
        return null;
    }
}

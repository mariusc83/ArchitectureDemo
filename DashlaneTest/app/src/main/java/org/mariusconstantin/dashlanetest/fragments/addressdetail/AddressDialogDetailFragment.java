package org.mariusconstantin.dashlanetest.fragments.addressdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.mariusconstantin.dashlanetest.R;
import org.mariusconstantin.dashlanetest.data.models.IWebsiteModel;

/**
 * Created by MConstantin on 1/5/2016.
 */
public class AddressDialogDetailFragment extends DialogFragment  implements IAddressDetailContract.IView {
    private AddressDetailFragmentHelper mFragmentHelper = new AddressDetailFragmentHelper();

    public static AddressDialogDetailFragment newInstance(@NonNull IWebsiteModel model) {
        final AddressDialogDetailFragment fragment = new AddressDialogDetailFragment();
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AddressDetailFragmentHelper.MODEL_KEY, model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.address_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentHelper.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentHelper.onActivityCreated(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mFragmentHelper.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentHelper.onDetach(this);
    }

    @Override
    public void showTitle(@NonNull String title) {
        mFragmentHelper.showTitle(title);
    }


    @Override
    public void showLogo(@NonNull String logo) {
        mFragmentHelper.showLogo(logo);
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }
}

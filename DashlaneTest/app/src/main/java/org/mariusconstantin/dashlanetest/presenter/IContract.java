package org.mariusconstantin.dashlanetest.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

/**
 * Created by Marius on 1/3/2016.
 */
public interface IContract {

    interface IContainerPresenter {
        @MainThread
        <T extends IChildView> void addChild(T childView);

        @MainThread
        <T extends IChildView> boolean removeChild(T childView);

        void onCreate(Context context, Bundle bundle);

        void onDestroy(Context context);
    }

    interface IChildPresenter<T extends IContainerPresenter, D extends IChildView> {
        @NonNull
        @MainThread
        T getParent();


        void onAttach(@NonNull D childView);

        void onDetach(@NonNull D childView);
    }

    interface IView {
        <T extends Fragment> T switchToFragment(T fragment);

        <T extends DialogFragment> T showDialogFragment(T fragment);

        IContainerPresenter getPresenter();
    }

    interface IChildView {
    }
}

package org.mariusconstantin.dashlanetest.data.models;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Website Model class which will be used to render the Websites List
 * Created by Marius on 1/1/2016.
 */
public interface IWebsiteModel extends ICachedModel, Parcelable {

    @NonNull
    String getLogoId();

    @NonNull
    String getTitle();

    @Nullable
    String getSubtitle();
}

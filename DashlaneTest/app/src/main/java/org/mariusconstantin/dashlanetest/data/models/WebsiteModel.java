package org.mariusconstantin.dashlanetest.data.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by Marius on 1/1/2016.
 */
public class WebsiteModel implements IWebsiteModel {
    @NonNull
    private final String mLogoId;
    @NonNull
    private final String mTitle;
    @Nullable
    private final String mSubtitle;

    public WebsiteModel(Parcel in) {
        mLogoId = in.readString();
        mTitle = in.readString();
        mSubtitle = in.readString();
    }

    public WebsiteModel(@NonNull String logoId, @NonNull String title, @Nullable String subtitle) {
        this.mLogoId = logoId;
        this.mTitle = title;
        this.mSubtitle = subtitle;
    }

    public WebsiteModel(@NonNull String logoId, @NonNull String title) {
        this(logoId, title, null);
    }

    @NonNull
    @Override
    public String getLogoId() {
        return mLogoId;
    }

    @NonNull
    @Override
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    @Override
    public String getSubtitle() {
        return mSubtitle;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLogoId);
        dest.writeString(mTitle);
        dest.writeString(mSubtitle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int memorySize() {
        int size = 0;
        size = size + mLogoId.getBytes().length / 1024;
        size = size + mTitle.getBytes().length / 1024;
        size = size + (mSubtitle != null ? mSubtitle.getBytes().length / 1024 : 0);
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WebsiteModel))
            return false;

        final WebsiteModel toCompareWith = (WebsiteModel) o;
        if (!TextUtils.equals(mTitle, toCompareWith.getTitle()))
            return false;
        if (!TextUtils.equals(mLogoId, toCompareWith.getLogoId()))
            return false;
        if (!TextUtils.equals(mSubtitle, toCompareWith.getSubtitle()))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hashcode = 17;
        hashcode = 31 * hashcode + mTitle.hashCode();
        hashcode = hashcode + 31 * hashcode + mLogoId.hashCode();
        hashcode = hashcode + 31 * hashcode + (mSubtitle != null ? mSubtitle.hashCode() : 0);
        return hashcode;
    }

    public static final Parcelable.Creator<WebsiteModel> CREATOR = new Parcelable.Creator<WebsiteModel>() {
        @Override
        public WebsiteModel createFromParcel(Parcel source) {
            return new WebsiteModel(source);
        }

        @Override
        public WebsiteModel[] newArray(int size) {
            return new WebsiteModel[size];
        }
    };
}

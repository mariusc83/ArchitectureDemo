package org.mariusconstantin.dashlanetest.data.deserizalizers;

import android.support.annotation.NonNull;

/**
 * Created by Marius on 1/3/2016.
 */
public interface IModelDeserializer<T> {

    T fetchModel(@NonNull String data) throws UnsupportedDataFormatException;
}

package org.mariusconstantin.dashlanetest.data;

/**
 * Created by Marius on 1/1/2016.
 */
public interface ICachePolicy {

    boolean isLruCacheEnabled();

    boolean isDiskCacheEnabled(); // TODO: 1/3/2016 This should be added later and is related with the Retrofit Webservice 

    long cacheExpireTime();

    int maxLruCacheSize();
}

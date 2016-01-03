package org.mariusconstantin.dashlanetest;

/**
 * Created by Marius on 1/3/2016.
 */
public interface IAppConfig {
    String getEndpointsRootUrl();

    String getImagesRootPathUrl();

    boolean isMock();
}

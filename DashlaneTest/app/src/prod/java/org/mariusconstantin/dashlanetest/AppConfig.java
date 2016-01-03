package org.mariusconstantin.dashlanetest;

/**
 * Created by Marius on 1/3/2016.
 */
public class AppConfig implements IAppConfig {
    private static final String ENDPOINTS_URL = "https://dl.dropboxusercontent.com";
    private static final String IMAGES_PATH_ROOT_URL = "http://s3-eu-west-1.amazonaws.com/static-icons/_android48/";

    @Override
    public String getEndpointsRootUrl() {
        return ENDPOINTS_URL;
    }

    @Override
    public String getImagesRootPathUrl() {
        return IMAGES_PATH_ROOT_URL;
    }

    @Override
    public boolean isMock() {
        return false;
    }
}

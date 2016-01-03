package org.mariusconstantin.dashlanetest.helpers;

import org.mariusconstantin.dashlanetest.IAppConfig;

/**
 * Created by MConstantin on 1/5/2016.
 */
public class PathHelper implements IPathHelper {
    private final String mLogoRootPath;

    public PathHelper(IAppConfig appConfig) {
        mLogoRootPath = appConfig.getImagesRootPathUrl();
    }

    @Override
    public String getLogoPath(String logoId) {
        return mLogoRootPath + logoId;
    }
}

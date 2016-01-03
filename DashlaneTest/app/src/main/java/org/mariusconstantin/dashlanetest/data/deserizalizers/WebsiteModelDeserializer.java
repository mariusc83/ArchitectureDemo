package org.mariusconstantin.dashlanetest.data.deserizalizers;

import android.support.annotation.NonNull;

import org.mariusconstantin.dashlanetest.data.models.IWebsiteModel;
import org.mariusconstantin.dashlanetest.data.models.WebsiteModel;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Marius on 1/3/2016.
 */
public class WebsiteModelDeserializer implements IModelDeserializer<IWebsiteModel> {
    private static final String DOMAIN_REGEXP = "(.*)\\.(.*)";
    private static final String UNSUPPORTED_FORMAT_EXCEPTION_MESSAGE = "You are not using a correct format for the website model: %s";
    private static Pattern domainRegExpPattern = Pattern.compile(DOMAIN_REGEXP);

    @Override
    public IWebsiteModel fetchModel(@NonNull String data) throws UnsupportedDataFormatException {
        final Pattern pattern = Pattern.compile(DOMAIN_REGEXP);
        final Matcher matcher = pattern.matcher(data);
        if (matcher.matches() && matcher.groupCount() > 1) {
            final String domain = matcher.group(1);
            return new WebsiteModel(data, domain.replace("_", "."));

        } else {
            throw new UnsupportedDataFormatException(String.format(Locale.ENGLISH, UNSUPPORTED_FORMAT_EXCEPTION_MESSAGE, data));
        }
    }
}

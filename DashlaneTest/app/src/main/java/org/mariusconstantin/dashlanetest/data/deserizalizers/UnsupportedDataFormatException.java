package org.mariusconstantin.dashlanetest.data.deserizalizers;

/**
 * Created by MConstantin on 1/4/2016.
 */
public class UnsupportedDataFormatException extends Exception {

    private static final long serialVersionUID = 1997753363232807009L;

    public UnsupportedDataFormatException(String detailMessage) {
        super(detailMessage);
    }
}

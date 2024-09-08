package com.autofocus.pms.security.oauth2.config.security.util;

import java.util.Date;

public class TimestampUtil {
    public static Date getPayloadTimestamp(){
        return new Date();
    }
}

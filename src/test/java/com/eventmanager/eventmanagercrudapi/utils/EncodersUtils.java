package com.eventmanager.eventmanagercrudapi.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EncodersUtils {
    public String getBase64Credentials(String username, String password) {
        String credentials = username + ":" + password;
        return new String(java.util.Base64.getEncoder().encode(credentials.getBytes()));
    }
}

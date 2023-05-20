package com.teamfalcon.login.utils;

import java.time.LocalDateTime;

public class ExpiryDateGeneration {
    private static int EXPIRY_DATE_IN_HOURS = 24;
    public static LocalDateTime generateExpiryDate() {
        return LocalDateTime.now().plusHours(EXPIRY_DATE_IN_HOURS);
    }
}

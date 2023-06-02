package com.teamfalcon.login.utils;

import java.util.UUID;

public class TokenGeneration {

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}

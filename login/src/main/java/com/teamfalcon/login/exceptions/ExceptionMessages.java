package com.teamfalcon.login.exceptions;

public class ExceptionMessages {
    public static final String USER_NOT_FOUND_MESSAGE = "Error 404 Not Found: No user found";
    public static String INCORRECT_PASSWORD_MESSAGE = "Error 401 Unauthorised: Incorrect Credentials";
    public static String FAILED_LOGIN_ATTEMPTS_EXCEEDED_MESSAGE = "Error 403 Forbidden: Failed login attempts exceeded please contact support";
    public static final int UNAUTHORISED_STATUS_CODE = 401;
    public static final int FORBIDDEN_STATUS_CODE = 403;
}

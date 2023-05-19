package com.teamfalcon.login.exceptions;

public class ExceptionMessages {
    public static final String USER_NOT_FOUND_MESSAGE = "Error 404 Not Found: No user found";
    public static String INCORRECT_PASSWORD_MESSAGE = "Error 401 Unauthorised: Incorrect Credentials";
    public static String FAILED_LOGIN_ATTEMPTS_EXCEEDED_MESSAGE = "Error 403 Forbidden: Failed login attempts exceeded please contact support";
    public static final int UNAUTHORISED_STATUS_CODE = 401;
    public static final int FORBIDDEN_STATUS_CODE = 403;

    public static final String ENTITY_NOT_FOUND_ERROR_MESSAGE = "Entity Not Found";
    public static final String INCORRECT_PASSWORD_HASH_ERROR_MESSAGE = "Incorrect Password";

    public static final String FAILED_LOGIN_ATTEMPTS_ERROR_MESSAGE = "Failed login limit exceeded";

    public static final String FAILED_TO_LOGIN_RESPONSE_BODY_MESSAGE = "Failed to Login";
}

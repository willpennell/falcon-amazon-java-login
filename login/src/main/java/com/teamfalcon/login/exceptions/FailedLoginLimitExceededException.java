package com.teamfalcon.login.exceptions;

public class FailedLoginLimitExceededException extends RuntimeException{
    private static final String FAILED_LOGIN_ATTEMPTS_EXCEEDED_MESSAGE = "Error 403 Forbidden: Failed login attempts exceeded please contact support";

    public FailedLoginLimitExceededException() {
        super(FAILED_LOGIN_ATTEMPTS_EXCEEDED_MESSAGE);
    }
}

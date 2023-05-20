package com.teamfalcon.login.exceptions;

public class FailedLoginLimitExceededException extends RuntimeException{
    public FailedLoginLimitExceededException(String message) {
        super(message);
    }
}

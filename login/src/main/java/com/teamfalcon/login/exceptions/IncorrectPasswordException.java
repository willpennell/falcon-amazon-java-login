package com.teamfalcon.login.exceptions;

public class IncorrectPasswordException extends RuntimeException {
    private static final String INCORRECT_PASSWORD_MESSAGE = "Error 401 Unauthorised: Incorrect Credentials";
    public IncorrectPasswordException() {
        super(INCORRECT_PASSWORD_MESSAGE);
    }
}

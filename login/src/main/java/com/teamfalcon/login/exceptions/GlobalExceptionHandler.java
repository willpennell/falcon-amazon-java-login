package com.teamfalcon.login.exceptions;

import com.teamfalcon.login.model.LoginResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    private static final int UNAUTHORISED_STATUS_CODE = 401;
    private static final int FORBIDDEN_STATUS_CODE = 403;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<LoginResponseDTO> handleEntityNotFoundException(
            EntityNotFoundException ex) {
        return new ResponseEntity<>(
                loginRequestBodyDTOSetUp(ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<LoginResponseDTO> handleIncorrectPasswordException(
            IncorrectPasswordException ex) {
        return new ResponseEntity<>(
                loginRequestBodyDTOSetUp(ex.getMessage()),
                HttpStatus.valueOf(UNAUTHORISED_STATUS_CODE));
    }

    @ExceptionHandler(FailedLoginLimitExceededException.class)
    public ResponseEntity<LoginResponseDTO> handleFailedLoginAttemptsExceededException(
            FailedLoginLimitExceededException ex) {
        return new ResponseEntity<>(
                loginRequestBodyDTOSetUp(ex.getMessage()),
                HttpStatusCode.valueOf(FORBIDDEN_STATUS_CODE));
    }

    private LoginResponseDTO loginRequestBodyDTOSetUp(String message) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setSuccess(false);
        loginResponseDTO.setToken(null);
        loginResponseDTO.setMessage(message);
        return loginResponseDTO;
    }
}

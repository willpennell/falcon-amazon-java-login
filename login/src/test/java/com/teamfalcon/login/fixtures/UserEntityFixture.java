package com.teamfalcon.login.fixtures;

import com.teamfalcon.login.model.LoginRequestBodyDTO;
import com.teamfalcon.login.model.UserEntity;
import jakarta.xml.bind.DatatypeConverter;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class UserEntityFixture {

    @SneakyThrows
    private static String hashPassword(String password) {
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        byte [] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

        return DatatypeConverter.printHexBinary(hashBytes).toUpperCase();
    }

    public static UserEntity makeValidUser() {
        return UserEntity.builder()
                .id(25)
                .username("johndoe@example.com")
                .passwordHash(hashPassword("valid"))
                .failedLoginAttempts(0)
                .deleted(Boolean.FALSE).build();

    }

    public static UserEntity makeInvalidUser() {
        return UserEntity.builder()
                .id(25)
                .username("johndoe@example.com")
                .passwordHash(hashPassword("invalid"))
                .failedLoginAttempts(6)
                .deleted(Boolean.FALSE).build();
    }

    public static UserEntity makeDeletedUser() {
        return UserEntity.builder()
                .id(25)
                .username("johndoe@example.com")
                .passwordHash(hashPassword("valid"))
                .failedLoginAttempts(0)
                .deleted(Boolean.TRUE).build();
    }

    public static LoginRequestBodyDTO makeValidLoginRequestBodyDTO() {
        return LoginRequestBodyDTO
                .builder()
                .username("johndoe@example.com")
                .password("valid")
                .build();

    }

    public static LoginRequestBodyDTO makeInvalidPasswordLoginRequestBodyDTO() {
        return LoginRequestBodyDTO
                .builder()
                .username("johndoe@example.com")
                .password("invalid")
                .build();

    }

    public static LoginRequestBodyDTO makeInvalidUsernameLoginRequestBodyDTO() {
        return LoginRequestBodyDTO
                .builder()
                .username("")
                .password("invalid")
                .build();

    }
}

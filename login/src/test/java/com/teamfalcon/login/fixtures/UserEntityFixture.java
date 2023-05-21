package com.teamfalcon.login.fixtures;

import com.teamfalcon.login.model.LoginRequestBodyDTO;
import com.teamfalcon.login.model.UserEntity;

public class UserEntityFixture {

    public static UserEntity makeValidUser() {
        return UserEntity.builder()
                .id(25)
                .username("johndoe@example.com")
                .passwordHash("h7B3q9fX8eR2dP5s")
                .failedLoginAttempts(0)
                .deleted(Boolean.FALSE).build();

    }

    public static UserEntity makeInvalidUser() {
        return UserEntity.builder()
                .id(25)
                .username("johndoe@example.com")
                .passwordHash("h7B3q9fX8eR2dP5s")
                .failedLoginAttempts(6)
                .deleted(Boolean.FALSE).build();
    }

    public static UserEntity makeDeletedUser() {
        return UserEntity.builder()
                .id(25)
                .username("johndoe@example.com")
                .passwordHash("h7B3q9fX8eR2dP5s")
                .failedLoginAttempts(0)
                .deleted(Boolean.TRUE).build();
    }

    public static LoginRequestBodyDTO makeValidLoginRequestBodyDTO() {
        return LoginRequestBodyDTO
                .builder()
                .username("johndoe@example.com")
                .passwordHash("h7B3q9fX8eR2dP5s")
                .build();

    }

    public static LoginRequestBodyDTO makeInvalidPasswordLoginRequestBodyDTO() {
        return LoginRequestBodyDTO
                .builder()
                .username("johndoe@example.com")
                .passwordHash("123")
                .build();

    }

    public static LoginRequestBodyDTO makeInvalidUsernameLoginRequestBodyDTO() {
        return LoginRequestBodyDTO
                .builder()
                .username("")
                .passwordHash("123")
                .build();

    }
}

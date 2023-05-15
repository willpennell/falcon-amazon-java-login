package com.teamfalcon.login.model;

public interface UserDTO {
    int getId();
    String getUsername();
    String getPasswordHash();
    int getFailedLoginAttempts();
    Boolean getIsDeleted();
}

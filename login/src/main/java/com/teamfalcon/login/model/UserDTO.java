package com.teamfalcon.login.model;

public interface UserDTO {

    Integer getId();
    String getUsername();
    String getPasswordHash();
    Integer getFailedLoginAttempts();
    Boolean getIsDeleted();

    void setId(Integer id);
    void setUsername(String username);
    void setPasswordHash(String passwordHash);
    void setFailedLoginAttempts(Integer failedLoginAttempts);
    void setIsDeleted(Boolean isDeleted);
}

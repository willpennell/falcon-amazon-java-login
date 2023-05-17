package com.teamfalcon.login.persistence;

import com.teamfalcon.login.model.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDTO, Integer> {
    @Query("SELECT u.id AS id, u.username AS username, u.failedLoginAttempts AS failed_login_attempts, u.isDeleted AS is_deleted, u.passwordHash AS password_hash FROM User u WHERE u.username = :username")
    UserDTO findLoginDTOByUsername(@Param("username") String username);

    @Query("UPDATE UserDTO u SET u.failedLoginAttempts = :newAttempts WHERE u.username = :username")
    @Modifying
    void updateFailedLoginAttempts(@Param("username") String username ,@Param("newAttempt") int newAttempt);
}

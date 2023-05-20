package com.teamfalcon.login.persistence;

import com.teamfalcon.login.model.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.failedLoginAttempts = :newAttempt WHERE u.username = :username")
    void updateFailedLoginAttempts(@Param("username") String username, @Param("newAttempt") int newAttempt);
}

package com.teamfalcon.login.persistence;

import com.teamfalcon.login.model.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.teamfalcon.login.utils.ParameterNames.USER_ID;

public interface UserTokenRepository extends JpaRepository<TokenEntity, Integer> {
    Optional<TokenEntity> findByUserId(@Param(USER_ID) Integer userId);
}

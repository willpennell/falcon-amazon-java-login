package com.teamfalcon.login.persistence;

import com.teamfalcon.login.model.TokenEntity;
import com.teamfalcon.login.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.teamfalcon.login.fixtures.UserEntityFixture.makeValidUser;
import static com.teamfalcon.login.utils.ExpiryDateGeneration.generateExpiryDate;
import static com.teamfalcon.login.utils.TokenGeneration.generateToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserTokenRepositoryTest {

    private final UserTokenRepository userTokenRepository;

    @Autowired
    public UserTokenRepositoryTest(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }

    @Test
    public void findUserTokenByIdTest() {
        UserEntity userEntity = makeValidUser();
        TokenEntity newTokenEntity = TokenEntity
                .builder()
                .userId(userEntity.getId())
                .token(generateToken())
                .expiryDate(generateExpiryDate())
                .build();
        userTokenRepository.save(newTokenEntity);

        Optional<TokenEntity> userToken = userTokenRepository.findByUserId(userEntity.getId());

        assertTrue(userToken.isPresent());
        assertEquals(newTokenEntity.getToken(), userToken.get().getToken());
    }

    @Test
    public void userIdDoesNotExist() {
        Integer invalidId = 100002;

        Optional<TokenEntity> userToken = userTokenRepository.findByUserId(invalidId);

        assertTrue(userToken.isEmpty());
    }
}

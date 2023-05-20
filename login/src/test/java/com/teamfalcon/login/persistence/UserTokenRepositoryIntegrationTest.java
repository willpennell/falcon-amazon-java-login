package com.teamfalcon.login.persistence;

import com.teamfalcon.login.model.UserEntity;
import com.teamfalcon.login.model.TokenEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static com.teamfalcon.login.testmodels.TestUserEntityFactory.*;
import static com.teamfalcon.login.utils.ExpiryDateGeneration.generateExpiryDate;
import static com.teamfalcon.login.utils.TokenGeneration.generateToken;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class UserTokenRepositoryIntegrationTest {

    private final UserTokenRepository userTokenRepository;

    @Autowired
    public UserTokenRepositoryIntegrationTest(UserTokenRepository userTokenRepository) {
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

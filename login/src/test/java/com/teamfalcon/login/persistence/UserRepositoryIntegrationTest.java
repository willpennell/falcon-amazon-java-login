package com.teamfalcon.login.persistence;


import com.teamfalcon.login.model.UserEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class UserRepositoryIntegrationTest {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryIntegrationTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void findLoginDTOByUsernameTest() {
        String username = "johndoe@example.com";
        String passwordHash = "h7B3q9fX8eR2dP5s";

        Optional<UserEntity> userDTO = userRepository.findByUsername(username);

        assertNotNull(userDTO);
        assertEquals(passwordHash, userDTO.get().getPasswordHash());
    }


    @Test
    public void updateFailedLoginAttemptsTest() {

        String username = "johndoe@example.com";

        userRepository.updateFailedLoginAttempts(username, 2);
        Optional<UserEntity> updatedUser = userRepository.findByUsername(username);

        assertEquals(2, updatedUser.get().getFailedLoginAttempts());

    }
}

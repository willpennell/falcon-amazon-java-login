package com.teamfalcon.login.persistence;


import com.teamfalcon.login.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
public class UserRepositoryTest {

    private final UserRepository userRepository;
    private static UserEntity testUser;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    public void setUp() {
        testUser = UserEntity
                .builder()
                .id(1)
                .username("johndoe@example.com")
                .passwordHash("h7B3q9fX8eR2dP5s")
                .failedLoginAttempts(0)
                .deleted(Boolean.FALSE)
                .build();
        userRepository.save(testUser);
    }

    @Test
    public void findLoginDTOByUsernameTest() {
        String username = "johndoe@example.com";
        String passwordHash = "h7B3q9fX8eR2dP5s";

        Optional<UserEntity> userDTO = userRepository.findByUsername(username);

        assertNotNull(userDTO);
        assertEquals(passwordHash, userDTO.get().getPasswordHash());
    }


}

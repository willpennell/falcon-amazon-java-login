package com.teamfalcon.login.service;


import com.teamfalcon.login.exceptions.FailedLoginLimitExceededException;
import com.teamfalcon.login.exceptions.IncorrectPasswordException;
import com.teamfalcon.login.model.LoginRequestBodyDTO;
import com.teamfalcon.login.model.LoginResponseDTO;
import com.teamfalcon.login.model.UserEntity;
import com.teamfalcon.login.model.TokenEntity;
import com.teamfalcon.login.persistence.UserRepository;
import com.teamfalcon.login.persistence.UserTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.teamfalcon.login.testmodels.TestUserEntityFactory.*;
import static com.teamfalcon.login.utils.ExpiryDateGeneration.generateExpiryDate;
import static com.teamfalcon.login.utils.TokenGeneration.generateToken;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTokenRepository userTokenRepository;




    @Test
    public void shouldThrowEntityNotFoundExceptionTest() {

        UserEntity deletedUser = makeDeletedUser();
        LoginRequestBodyDTO loginRequestBodyDTO = makeValidLoginRequestBodyDTO();


        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(deletedUser));

        assertThrows(EntityNotFoundException.class, () -> {
            loginService.authoriseLogin(loginRequestBodyDTO);
        });


    }

    @Test
    public void shouldThrowNoEntityFoundExceptionTest() {

        LoginRequestBodyDTO loginRequestBodyDTO = makeInvalidUsernameLoginRequestBodyDTO();

        assertThrows(EntityNotFoundException.class, () -> {
            loginService.authoriseLogin(loginRequestBodyDTO);
        });
    }

    @Test
    public void shouldThrowIncorrectPasswordHashExceptionTest() {

        UserEntity validUser = makeValidUser();
        LoginRequestBodyDTO loginRequestBodyDTO = makeInvalidPasswordLoginRequestBodyDTO();

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(validUser));

        assertThrows(IncorrectPasswordException.class, () -> {
            loginService.authoriseLogin(loginRequestBodyDTO);
        });


    }

    @Test
    public void shouldIncrementFailedLoginAttemptTest() {

        UserEntity validUser = makeValidUser();
        LoginRequestBodyDTO loginRequestBodyDTO = makeInvalidPasswordLoginRequestBodyDTO();

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(validUser));
        doNothing().when(userRepository).updateFailedLoginAttempts(any(String.class), any(int.class));

        assertThrows(IncorrectPasswordException.class, () -> {
            loginService.authoriseLogin(loginRequestBodyDTO);
        });

        verify(userRepository, times(1)).updateFailedLoginAttempts(
                loginRequestBodyDTO.getUsername(),
                (validUser.getFailedLoginAttempts() + 1));


    }

    @Test
    public void shouldThrowFailedLoginAttemptsExceptionTest() {

        UserEntity invalidUserTooManyFailedLoginAttempts = makeInvalidUser();
        LoginRequestBodyDTO loginRequestBodyDTO = makeValidLoginRequestBodyDTO();

        when(userRepository.findByUsername(
                any(String.class)))
                .thenReturn(Optional.of(invalidUserTooManyFailedLoginAttempts));

        assertThrows(FailedLoginLimitExceededException.class, () -> {
            loginService.authoriseLogin(loginRequestBodyDTO);
        });
    }

    @Test
    public void createNewTokenAsEmptyUserTokenDTOReturnedFromRepoTest() {

        UserEntity validUser = makeValidUser();
        LoginRequestBodyDTO loginRequestBodyDTO = makeValidLoginRequestBodyDTO();

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(validUser));
        when(userTokenRepository.findByUserId(any(Integer.class))).thenReturn(Optional.empty());
        when(userTokenRepository.save(any(TokenEntity.class))).thenReturn(any(TokenEntity.class));

        LoginResponseDTO actualResponse = loginService.authoriseLogin(loginRequestBodyDTO);
        Boolean expectedSuccess = true;

        assertEquals(expectedSuccess, actualResponse.getSuccess());
        assertNotNull(actualResponse.getToken());
        assertNull(actualResponse.getMessage());
    }

    @Test
    public void updateExistingTokenDTOReturnedFromRepoTest() {

        UserEntity validUser = makeValidUser();
        LoginRequestBodyDTO loginRequestBodyDTO = makeValidLoginRequestBodyDTO();

        TokenEntity returnedUserToken = TokenEntity.builder()
                .id(1)
                .userId(1)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().minusHours(6))
                .build();

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(validUser));
        when(userTokenRepository.findByUserId(any(Integer.class))).thenReturn(Optional.of(returnedUserToken));
        when(userTokenRepository.save(any(TokenEntity.class))).thenReturn(any(TokenEntity.class));

        LoginResponseDTO actualResponse = loginService.authoriseLogin(loginRequestBodyDTO);
        Boolean expectedSuccess = true;

        assertNotEquals(returnedUserToken.getToken(), actualResponse.getToken());
        assertEquals(expectedSuccess, actualResponse.getSuccess());
        assertNotNull(actualResponse.getToken());
        assertNull(actualResponse.getMessage());
    }

    @Test
    public void useValidUserTokenDTOReturnedFromRepoTest() {

        UserEntity validUser = makeValidUser();
        LoginRequestBodyDTO loginRequestBodyDTO = makeValidLoginRequestBodyDTO();

        TokenEntity returnedUserToken = TokenEntity.builder()
                .id(1)
                .userId(1)
                .token(generateToken())
                .expiryDate(generateExpiryDate())
                .build();

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(validUser));
        when(userTokenRepository.findByUserId(any(Integer.class))).thenReturn(Optional.of(returnedUserToken));


        LoginResponseDTO actualResponse = loginService.authoriseLogin(loginRequestBodyDTO);
        Boolean expectedSuccess = true;

        assertNotNull(actualResponse.getToken());
        assertEquals(returnedUserToken.getToken(), actualResponse.getToken());
        assertEquals(expectedSuccess, actualResponse.getSuccess());
        assertNull(actualResponse.getMessage());
    }


}

package com.teamfalcon.login.service;


import com.teamfalcon.login.exceptions.FailedLoginLimitExceededException;
import com.teamfalcon.login.exceptions.IncorrectPasswordException;
import com.teamfalcon.login.model.LoginRequestBodyDTO;
import com.teamfalcon.login.model.UserDTO;
import com.teamfalcon.login.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    @InjectMocks
    private static LoginServiceImpl loginService;

    @Mock
    private UserRepository userRepository;



    @Test
    public void IsDeletedThrowsEntityNotFoundExceptionTest() {
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        UserDTO deletedUser = makeDeletedUser(factory);
        LoginRequestBodyDTO loginRequestBodyDTO = makeValidLoginRequestBodyDTO();

        when(userRepository.findLoginDTOByUsername(any(String.class))).thenReturn(deletedUser);

        assertThrows(EntityNotFoundException.class, () -> {
            loginService.authoriseLogin(loginRequestBodyDTO);
        });

        Mockito.reset(userRepository);
    }

    @Test
    public void IncorrectPasswordHashThrowsIncorrectPasswordHashExceptionTest() {
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        UserDTO validUser = makeValidUser(factory);
        LoginRequestBodyDTO loginRequestBodyDTO = makeInvalidLoginRequestBodyDTO();

        when(userRepository.findLoginDTOByUsername(any(String.class))).thenReturn(validUser);

        assertThrows(IncorrectPasswordException.class, () -> {
            loginService.authoriseLogin(loginRequestBodyDTO);
        });

        Mockito.reset(userRepository);
    }

    @Test
    public void IncorrectPasswordHashIncrementsFailedLoginAttemptTest() {
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        UserDTO validUser = makeValidUser(factory);
        LoginRequestBodyDTO loginRequestBodyDTO = makeInvalidLoginRequestBodyDTO();

        when(userRepository.findLoginDTOByUsername(any(String.class))).thenReturn(validUser);
        doNothing().when(userRepository).updateFailedLoginAttempts(any(String.class), any(int.class));

        assertThrows(IncorrectPasswordException.class, () -> {
            loginService.authoriseLogin(loginRequestBodyDTO);
        });

        verify(userRepository, times(1)).updateFailedLoginAttempts(
                loginRequestBodyDTO.getUsername(),
                (validUser.getFailedLoginAttempts() + 1));

        Mockito.reset(userRepository);
    }

    @Test
    public void FailedLoginAttemptsExceedsLimitThrowsFailedLoginAttemptsExceptionTest() {
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        UserDTO invalidUserTooManyFailedLoginAttempts = makeInvalidUser(factory);
        LoginRequestBodyDTO loginRequestBodyDTO = makeValidLoginRequestBodyDTO();

        System.out.println(invalidUserTooManyFailedLoginAttempts);
        when(userRepository.findLoginDTOByUsername(
                any(String.class)))
                .thenReturn(invalidUserTooManyFailedLoginAttempts);

        assertThrows(FailedLoginLimitExceededException.class, () -> {
            loginService.authoriseLogin(loginRequestBodyDTO);
        });

        Mockito.reset(userRepository);
    }


    private UserDTO makeValidUser(ProjectionFactory factory) {
        UserDTO userDTOProjection = factory.createProjection(UserDTO.class);
        userDTOProjection.setId(1);
        userDTOProjection.setUsername("will123");
        userDTOProjection.setPasswordHash("b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86");
        userDTOProjection.setFailedLoginAttempts(0);
        userDTOProjection.setIsDeleted(false);
        return userDTOProjection;
    }

    private UserDTO makeInvalidUser(ProjectionFactory factory) {
        UserDTO userDTOProjection = factory.createProjection(UserDTO.class);
        userDTOProjection.setId(1);
        userDTOProjection.setUsername("will123");
        userDTOProjection.setPasswordHash("b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86");
        userDTOProjection.setFailedLoginAttempts(5);
        userDTOProjection.setIsDeleted(false);
        return userDTOProjection;
    }

    private UserDTO makeDeletedUser(ProjectionFactory factory) {
        UserDTO userDTOProjection = factory.createProjection(UserDTO.class);
        userDTOProjection.setId(1);
        userDTOProjection.setUsername("will123");
        userDTOProjection.setPasswordHash("b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86");
        userDTOProjection.setFailedLoginAttempts(0);
        userDTOProjection.setIsDeleted(true);
        return userDTOProjection;
    }

    private LoginRequestBodyDTO makeValidLoginRequestBodyDTO() {
        return LoginRequestBodyDTO
                .builder()
                .username("will123")
                .passwordHash("b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86")
                .build();

    }

    private LoginRequestBodyDTO makeInvalidLoginRequestBodyDTO() {
        return LoginRequestBodyDTO
                .builder()
                .username("will123")
                .passwordHash("123")
                .build();

    }
}

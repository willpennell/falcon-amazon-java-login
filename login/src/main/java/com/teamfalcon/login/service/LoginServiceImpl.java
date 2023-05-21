package com.teamfalcon.login.service;

import com.teamfalcon.login.exceptions.FailedLoginLimitExceededException;
import com.teamfalcon.login.exceptions.IncorrectPasswordException;
import com.teamfalcon.login.model.LoginRequestBodyDTO;
import com.teamfalcon.login.model.LoginResponseDTO;
import com.teamfalcon.login.model.TokenEntity;
import com.teamfalcon.login.model.UserEntity;
import com.teamfalcon.login.persistence.UserRepository;
import com.teamfalcon.login.persistence.UserTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

import static com.teamfalcon.login.utils.ExpiryDateGeneration.generateExpiryDate;
import static com.teamfalcon.login.utils.TokenGeneration.generateToken;

@Validated
@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;

    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int INCREMENTAL_AMOUNT = 1;
    public static final String ENTITY_NOT_FOUND_ERROR_MESSAGE = "Entity Not Found";
    private static final String USER_NOT_FOUND_MESSAGE = "Error 404 Not Found: No user found";

    @Override
    public LoginResponseDTO authoriseLogin(LoginRequestBodyDTO loginRequestBodyDTO) {
        validateLoginRequestUsername(loginRequestBodyDTO);

        UserEntity userEntity = userRepository.findByUsername(loginRequestBodyDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_ERROR_MESSAGE + "\n" +
                        UserEntity.class)));


        validateUserEntity(loginRequestBodyDTO, userEntity);

        TokenEntity tokenEntity = userTokenRepository.findByUserId(userEntity.getId())
                .orElseGet(() -> createTokenEntity(userEntity));


        if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenEntity = updateTokenEntity(tokenEntity);
        }

        return getLoginResponseDTO(tokenEntity);
    }

    private void validateLoginRequestUsername(LoginRequestBodyDTO loginRequestBodyDTO) {
        if (loginRequestBodyDTO.getUsername().isEmpty() || loginRequestBodyDTO.getUsername().isBlank()) {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND_MESSAGE + "\n" +
                    UserEntity.class));
        }
    }

    public void validateUserEntity(LoginRequestBodyDTO loginRequestBodyDTO, UserEntity userEntity) {

        if (userEntity.getDeleted()) {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND_MESSAGE + "\n" +
                    UserEntity.class));
        }
        if (userEntity.getFailedLoginAttempts() > MAX_LOGIN_ATTEMPTS) {
            throw new FailedLoginLimitExceededException();
        }
        if (!loginRequestBodyDTO.getPasswordHash().equals(userEntity.getPasswordHash())) {
            incrementFailedLoginAttempts(userEntity);
            throw new IncorrectPasswordException();
        }

    }
    private void incrementFailedLoginAttempts(UserEntity userEntity) {
        int currentAttempts = userEntity.getFailedLoginAttempts();
        userRepository.updateFailedLoginAttempts(
                userEntity.getUsername(),
                currentAttempts + INCREMENTAL_AMOUNT);
    }

    private LoginResponseDTO getLoginResponseDTO(TokenEntity newTokenEntity) {
        return LoginResponseDTO
                .builder()
                .success(true)
                .token(newTokenEntity.getToken())
                .build();
    }

    private TokenEntity createTokenEntity(UserEntity userEntity) {
        TokenEntity newTokenEntity = TokenEntity
                .builder()
                .userId(userEntity.getId())
                .token(generateToken())
                .expiryDate(generateExpiryDate())
                .build();
        userTokenRepository.save(newTokenEntity);
        return newTokenEntity;
    }

    private TokenEntity updateTokenEntity(TokenEntity tokenEntity) {
        TokenEntity updateTokenEntity = TokenEntity
                .builder()
                .id(tokenEntity.getId())
                .userId(tokenEntity.getUserId())
                .token(generateToken())
                .expiryDate(generateExpiryDate())
                .build();
        userTokenRepository.save(updateTokenEntity);
        return updateTokenEntity;
    }
}



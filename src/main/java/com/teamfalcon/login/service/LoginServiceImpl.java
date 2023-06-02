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
import jakarta.xml.bind.DatatypeConverter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;

import static com.teamfalcon.login.utils.ExpiryDateGeneration.generateExpiryDate;
import static com.teamfalcon.login.utils.TokenGeneration.generateToken;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${login.maxAttempts}")
    private int maxLoginAttempts;
    @Value("${login.incrementAmount}")
    private int incrementAmount;
    @Value("${login.userNotFoundMessage}")
    private String userNotFoundMessage;


    @Override
    public LoginResponseDTO authoriseLogin(LoginRequestBodyDTO loginRequestBodyDTO) {
        validateLoginRequestUsername(loginRequestBodyDTO);

        UserEntity userEntity = userRepository.findByUsername(loginRequestBodyDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(String.format(userNotFoundMessage + " " +
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
            throw new EntityNotFoundException(String.format(userNotFoundMessage + "\n" +
                    UserEntity.class));
        }
    }

    public void validateUserEntity(LoginRequestBodyDTO loginRequestBodyDTO, UserEntity userEntity) {

        if (userEntity.getDeleted()) {
            throw new EntityNotFoundException(String.format(userNotFoundMessage + "\n" +
                    UserEntity.class));
        }
        if (userEntity.getFailedLoginAttempts() >= maxLoginAttempts) {
            throw new FailedLoginLimitExceededException();
        }
        if (!hashedPasswordMatches(loginRequestBodyDTO.getPassword(), userEntity.getPasswordHash())) {
            incrementFailedLoginAttempts(userEntity);
            throw new IncorrectPasswordException();
        }

    }

    @SneakyThrows
    private boolean hashedPasswordMatches(String loginRequestBodyDTOPassword, String userEntityHash) {
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        byte [] hashBytes = md.digest(loginRequestBodyDTOPassword.getBytes(StandardCharsets.UTF_8));

        String hashedPassword = DatatypeConverter.printHexBinary(hashBytes);

        return hashedPassword.equalsIgnoreCase(userEntityHash);
    }

    private void incrementFailedLoginAttempts(UserEntity userEntity) {
        int currentAttempts = userEntity.getFailedLoginAttempts();
        userEntity.setFailedLoginAttempts(currentAttempts + incrementAmount);
        userRepository.save(userEntity);
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



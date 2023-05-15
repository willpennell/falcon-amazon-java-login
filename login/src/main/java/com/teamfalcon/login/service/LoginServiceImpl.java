package com.teamfalcon.login.service;

import com.teamfalcon.login.exceptions.FailedLoginLimitExceededException;
import com.teamfalcon.login.exceptions.IncorrectPasswordException;
import com.teamfalcon.login.model.LoginRequestBodyDTO;
import com.teamfalcon.login.model.LoginResponseDTO;
import com.teamfalcon.login.model.UserDTO;
import com.teamfalcon.login.persistence.UserRepository;
import com.teamfalcon.login.persistence.UserTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@AllArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {
    private final int MAX_LOGIN_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;

    @Override
    public LoginResponseDTO authoriseLogin(LoginRequestBodyDTO loginRequestBodyDTO) {

        UserDTO userDTO = userRepository.findLoginDTOByUsername(
                loginRequestBodyDTO.getUsername());
        if ((userDTO == null) || userDTO.getIsDeleted()) {
            throw new EntityNotFoundException("Entity Not Found");
        }
        if (!(loginRequestBodyDTO.getPasswordHash().equals(userDTO.getPasswordHash()))) {
            throw new IncorrectPasswordException("Incorrect Password");
        }
        if (userDTO.getFailedLoginAttempts() >= MAX_LOGIN_ATTEMPTS) {
            throw new FailedLoginLimitExceededException("Failed login limit exceeded");
        }




        // TODO search tbl_user_tokens with valid user_id
        // TODO verify token exists, if so verify its expiry_date is in the future
        // TODO if tokens expiry_date is invalid, generate a new token
        return null;
    }
}



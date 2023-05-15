package com.teamfalcon.login.service;

import com.teamfalcon.login.model.LoginRequestBodyDTO;
import com.teamfalcon.login.model.LoginResponseDTO;

public interface LoginService {
    LoginResponseDTO authoriseLogin(LoginRequestBodyDTO loginRequestBodyDTO);
}

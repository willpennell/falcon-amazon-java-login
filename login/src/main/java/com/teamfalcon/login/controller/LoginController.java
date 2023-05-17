package com.teamfalcon.login.controller;

import com.teamfalcon.login.model.LoginRequestBodyDTO;
import com.teamfalcon.login.model.LoginResponseDTO;
import com.teamfalcon.login.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestBodyDTO loginRequestBody) {
            return new ResponseEntity<>(
                    loginService.authoriseLogin(
                            loginRequestBody),
                    HttpStatus.OK);
    }

}

package com.teamfalcon.login.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class LoginRequestBodyDTO {
    @NonNull
    private String username;
    @NonNull
    private String passwordHash;
}



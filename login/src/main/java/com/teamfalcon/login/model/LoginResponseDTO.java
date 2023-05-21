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
public class LoginResponseDTO {
    @NonNull
    private Boolean success;
    private String token;
    private String message;
}

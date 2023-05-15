package com.teamfalcon.login.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class LoginResponseDTO {
    @NonNull
    private Boolean success;
    private String token;
    @NonNull
    private String message;
}

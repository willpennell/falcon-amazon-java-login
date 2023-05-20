package com.teamfalcon.login.model;

import lombok.*;

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



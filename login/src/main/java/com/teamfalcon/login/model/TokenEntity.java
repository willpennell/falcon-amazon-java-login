package com.teamfalcon.login.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user_tokens")
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "user_id", nullable = false)
    int userId;

    @Column(nullable = false, unique = true)
    String token;

    @Column(name = "expiry_date", nullable = false)
    LocalDateTime expiryDate;
}

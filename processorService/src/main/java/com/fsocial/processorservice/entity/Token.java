package com.fsocial.processorservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "token")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Token extends AbstractEntity<String> {
    @Column(nullable = false, unique = true)
    String token;

    @Column(nullable = false)
    String username;

    @Column(nullable = false)
    Instant expiryDateReFreshToken;

    @Column(nullable = false, unique = true)
    String refreshToken;

    String userAgent;

    String ipAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id") // Cột khóa ngoại trong bảng token
    Account account;
}
package com.fsocial.accountservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;

import java.util.UUID;

@Entity
@Table(name = "token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "token", length = 600)
    String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    Account account;
}

package com.fsocial.accountservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "account")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account extends AbstractEntity<String> {
    @Column(name = "username", unique = true)
    String username;

    @Column(name = "password")
    String password;

    boolean isKOL = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role")
    Role role;

    String email;

    boolean status = true;
}

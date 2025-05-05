package com.fsocial.processorservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account extends AbstractEntity<String> {
    @Column(name = "username", unique = true)
    String username;

    @Column(name = "password")
    String password;

    boolean isKOL = false;

    @ManyToMany(mappedBy = "accounts", fetch = FetchType.LAZY)
    Set<Role> roles;

    String email;

    boolean status = true;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY) // mappedBy trỏ đến "account" trong Token
    List<Token> tokens; // Đổi tên thành "tokens" để phản ánh quan hệ OneToMany
}
package com.fsocial.processorservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends AbstractEntity<String> {
    String name;

    String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_accounts", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "role_id"), // Cột liên kết với Role
            inverseJoinColumns = @JoinColumn(name = "account_id") // Cột liên kết với Account
    )
    Set<Account> accounts; // Thay tên từ "users" thành "accounts" cho rõ ràng

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "permission_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    Set<Permission> permissions;
}
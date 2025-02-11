package com.fsocial.accountservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.catalina.User;

import java.util.Set;

@Entity
@Table(name = "role")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    String name;

    String description;

    @ManyToMany()
    Set<Permission> permissions;

    @OneToMany(mappedBy = "role",fetch = FetchType.LAZY)
    Set<Account> users;
}

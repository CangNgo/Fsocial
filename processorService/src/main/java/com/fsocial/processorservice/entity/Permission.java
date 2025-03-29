package com.fsocial.processorservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "permission")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends AbstractEntity<String> {
    String name;

    String description;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY) // Sửa mappedBy và xóa @JoinColumn
    Set<Role> roles;
}
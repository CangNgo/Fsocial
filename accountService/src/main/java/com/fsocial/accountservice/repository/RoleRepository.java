package com.fsocial.accountservice.repository;


import com.fsocial.accountservice.entity.ERole;
import com.fsocial.accountservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}


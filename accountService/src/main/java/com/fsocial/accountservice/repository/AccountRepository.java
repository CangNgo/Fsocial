package com.fsocial.accountservice.repository;

import com.fsocial.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
public interface AccountRepository extends JpaRepository<Account, UUID> {

}

package com.fsocial.accountservice.repository;

import com.fsocial.accountservice.dto.response.DuplicationCheckResult;
import com.fsocial.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByUsernameOrEmail(String username, String email);
    Integer countByUsernameOrEmail(String username, String email);
    Integer countByUsername(String username);
    Integer countByEmail(String email);

    @Query("SELECT new com.fsocial.accountservice.dto.response.DuplicationCheckResult(" +
            "CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END, " +
            "CASE WHEN COUNT(a.email) > 0 THEN TRUE ELSE FALSE END) " +
            "FROM Account a WHERE a.username = :username OR a.email = :email")
    DuplicationCheckResult checkDuplication(@Param("username") String username, @Param("email") String email);

    boolean existsById(String id);
}

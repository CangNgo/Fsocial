package com.fsocial.accountservice.repository;

import com.fsocial.accountservice.dto.response.DuplicationResponse;
import com.fsocial.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByUsernameOrEmail(String username, String email);
    boolean existsByUsernameOrEmail(String username, String email);
    Integer countByUsernameOrEmail(String username, String email);

    @Query("SELECT new com.fsocial.accountservice.dto.response.DuplicationResponse( " +
            "CASE WHEN COUNT(a) > 0 THEN :username ELSE NULL END, " +
            "CASE WHEN COUNT(a) > 0 THEN :email ELSE NULL END) " +
            "FROM Account a WHERE a.username = :username OR a.email = :email")
    DuplicationResponse checkDuplication(@Param("username") String username, @Param("email") String email);

}

package com.fsocial.accountservice.repository;

import com.fsocial.accountservice.dto.response.AccountStatisticRegiserDTO;
import com.fsocial.accountservice.dto.response.AccountStatisticRegiserLongDateDTO;
import com.fsocial.accountservice.dto.response.DuplicationCheckResult;
import com.fsocial.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Query("SELECT FUNCTION('HOUR', a.createdAt), count(a) " +
            "FROM Account a " +
            "WHERE a.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY HOUR(a.createdAt)")
    List<Object[]> countByCreatedAtByHours(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT FUNCTION('DATE', a.createdAt) AS date, count(a) AS count " +
            "FROM Account a " +
            "WHERE a.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', a.createdAt) ")
    List<Object[]> countByCreatedAtByDate(LocalDateTime startDate, LocalDateTime endDate);
}

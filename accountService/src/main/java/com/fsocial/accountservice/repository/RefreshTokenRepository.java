package com.fsocial.accountservice.repository;

import com.fsocial.accountservice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
    long countByUsername(String username);
    void deleteOldestTokenByUsername(String username);

    Optional<RefreshToken> findByUsername(String username);
    @Query("SELECT re FROM RefreshToken re WHERE re.username = :username ORDER BY re.expiryDate DESC LIMIT 1")
    Optional<RefreshToken> findByUsernameAndExpiryDate_Max(String username);
}

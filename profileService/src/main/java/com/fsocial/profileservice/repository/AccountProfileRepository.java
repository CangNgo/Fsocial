package com.fsocial.profileservice.repository;

import com.fsocial.profileservice.entity.AccountProfile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountProfileRepository extends Neo4jRepository<AccountProfile, String> {
    Optional<AccountProfile> findByUserId(String userId);
}

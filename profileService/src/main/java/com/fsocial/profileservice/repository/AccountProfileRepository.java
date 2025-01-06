package com.fsocial.profileservice.repository;

import com.fsocial.profileservice.entity.AccountProfile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountProfileRepository extends Neo4jRepository<AccountProfile, String> {
}

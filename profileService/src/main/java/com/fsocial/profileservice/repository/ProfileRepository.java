package com.fsocial.profileservice.repository;

import com.fsocial.profileservice.dto.response.FindProfileDTO;
import com.fsocial.profileservice.dto.response.ProfileAdminResponse;
import com.fsocial.profileservice.entity.AccountProfile;
import org.springframework.context.annotation.Profile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends Neo4jRepository<AccountProfile, String> {
    List<FindProfileDTO> findByFirstName(String firstName);
}

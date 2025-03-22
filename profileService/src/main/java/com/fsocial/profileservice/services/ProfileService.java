package com.fsocial.profileservice.services;

import com.fsocial.profileservice.dto.response.FindProfileDTO;
import com.fsocial.profileservice.dto.response.ProfileAdminResponse;
import com.fsocial.profileservice.entity.AccountProfile;
import com.fsocial.profileservice.exception.AppCheckedException;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface ProfileService {
    List<FindProfileDTO> findByCombinedName(String combinedName);
    ProfileAdminResponse getProfileAdmin (String profileId) throws AppCheckedException;
}

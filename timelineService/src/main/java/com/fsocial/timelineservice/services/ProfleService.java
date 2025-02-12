package com.fsocial.timelineservice.services;

import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.exception.AppCheckedException;

public interface ProfleService {
    ProfileResponse getAccountProfile(String userId) throws AppCheckedException;

}

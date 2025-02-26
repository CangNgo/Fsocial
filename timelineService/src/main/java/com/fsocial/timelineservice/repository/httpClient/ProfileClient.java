package com.fsocial.timelineservice.repository.httpClient;

import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileResponse getProfile(@PathVariable("userId") String userId);
}

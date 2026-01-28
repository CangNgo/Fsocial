package com.fsocial.postservice.repository.httpClient;

import com.fsocial.postservice.dto.profile.ProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "profile-service", url = "${app.services.profile}",path = "/profile")
public interface ProfileClient {
    @GetMapping(value = "/userId",produces = MediaType.APPLICATION_JSON_VALUE)
    Optional<ProfileDTO> getProfile(@PathVariable String userId);

    // Methods from timelineService
    @GetMapping(value = "/external/{userIdByPost}", produces = MediaType.APPLICATION_JSON_VALUE)
    com.fsocial.postservice.dto.profile.ProfileResponse getProfileResponseByUserId(@PathVariable("userIdByPost") String userIdByPost);

    @GetMapping(value = "/follow/list_following", produces = MediaType.APPLICATION_JSON_VALUE)
    com.fsocial.postservice.dto.ApiResponse<java.util.Map<String, java.util.List<String>>> listFollowing();
}

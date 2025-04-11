package com.fsocial.timelineservice.repository.httpClient;

import com.fsocial.timelineservice.dto.ApiResponse;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {
    @GetMapping(value = "/external/{userIdByPost}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileResponse getProfileResponseByUserId(@PathVariable("userIdByPost") String userIdByPost);

    @GetMapping(value = "/follow/list_following", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Map<String, List<String>>> listFollowing();
}

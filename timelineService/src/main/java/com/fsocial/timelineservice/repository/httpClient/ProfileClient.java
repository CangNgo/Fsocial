package com.fsocial.timelineservice.repository.httpClient;

import com.fsocial.timelineservice.dto.ApiResponse;
import com.fsocial.timelineservice.dto.profile.ProfileResponse;
import com.fsocial.timelineservice.dto.profile.ProfileServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {
    @GetMapping(value = "/{useridByPost}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileResponse getProfileResponseByUserId(@PathVariable("useridByPost") String useridByPost);
}

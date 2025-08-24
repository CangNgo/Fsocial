package com.fsocial.notificationService.repository.httpClient;

import com.fsocial.notificationService.dto.response.ProfileNameResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${app.services.profile}", path = "/profile")
public interface ProfileClient {
    @GetMapping(value = "/internal/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileNameResponse getProfileByUserId(@PathVariable String userId);
}

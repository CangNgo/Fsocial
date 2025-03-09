package com.fsocial.messageservice.repository.httpClient;

import com.fsocial.messageservice.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profileService", url = "${app.services.profile}")
public interface ProfileClient {
    @GetMapping(value = "/internal/message/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileResponse getAccountProfileFromAnotherService(@PathVariable String userId);
}

package com.fsocial.postservice.repository.httpClient;

import com.fsocial.postservice.dto.profile.ProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {
    @GetMapping(value = "/userId",produces = MediaType.APPLICATION_JSON_VALUE)
    Optional<ProfileDTO> getProfile(@PathVariable String userId);
}

package com.fsocial.accountservice.repository.httpclient;

import com.fsocial.accountservice.config.AuthenticationConfig;
import com.fsocial.accountservice.dto.request.ProfileRegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service",
        url = "${app.services.profile}",
        configuration = { AuthenticationConfig.class }
)
public interface ProfileClient {
    @PostMapping(value = "/internal/create", produces = MediaType.APPLICATION_JSON_VALUE)
    void createProfile(@RequestBody ProfileRegisterRequest request);
}

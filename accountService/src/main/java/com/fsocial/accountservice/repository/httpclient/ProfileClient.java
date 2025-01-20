package com.fsocial.accountservice.repository.httpclient;

import com.fsocial.accountservice.dto.request.ProfileRegisterRequest;
import com.fsocial.accountservice.dto.response.ProfileRegisterResponse;
import com.fsocial.accountservice.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {

    @PostMapping(value = "/internal/create", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileRegisterResponse createProfile(@RequestBody ProfileRegisterRequest request);

    @GetMapping(value = "/internal/{userId}")
    ProfileResponse getProfileByUserId(@PathVariable("userId") String userId);
}

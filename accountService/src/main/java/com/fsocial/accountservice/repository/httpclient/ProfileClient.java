package com.fsocial.accountservice.repository.httpclient;

import com.fsocial.accountservice.dto.request.ProfileRegisterRequest;
import com.fsocial.accountservice.dto.response.ProfileRegisterResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileRegisterResponse createProfile(@RequestBody ProfileRegisterRequest request);

}

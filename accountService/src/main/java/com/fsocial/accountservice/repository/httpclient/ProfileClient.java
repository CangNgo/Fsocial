package com.fsocial.accountservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {
}

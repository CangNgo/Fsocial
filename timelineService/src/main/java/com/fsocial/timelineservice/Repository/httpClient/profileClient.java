package com.fsocial.timelineservice.Repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface profileClient {
    @GetMapping(value = "/userId",produces = MediaType.APPLICATION_JSON_VALUE)
    Object getProfile(@PathVariable String userId);
}

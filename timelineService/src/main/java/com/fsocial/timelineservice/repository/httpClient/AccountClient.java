package com.fsocial.timelineservice.repository.httpClient;

import com.fsocial.timelineservice.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "accountService", url = "${app.services.account}")
public interface AccountClient {
    @GetMapping(value = "/exists", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Map<String, Boolean>> existsAccountByUserId(@RequestParam String userId);
}

package com.fsocial.messageservice.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "accountService", url = "${app.services.account}")
public interface AccountClient {
    @GetMapping(value = "/internal/valid-id/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean validUserId(@PathVariable String userId);
}

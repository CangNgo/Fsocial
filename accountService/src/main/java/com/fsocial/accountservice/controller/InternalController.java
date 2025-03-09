package com.fsocial.accountservice.controller;

import com.fsocial.accountservice.services.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal")
public class InternalController {

    AccountService accountService;

    @GetMapping("/valid-id/{userId}")
    public boolean validUserId(@PathVariable String userId) {
        return accountService.existsById(userId);
    }
}

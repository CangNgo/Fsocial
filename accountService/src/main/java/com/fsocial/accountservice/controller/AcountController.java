package com.fsocial.accountservice.controller;

import com.fsocial.accountservice.dto.AccountDTO;
import com.fsocial.accountservice.dto.Response;
import com.fsocial.accountservice.exception.AppCheckedException;
import com.fsocial.accountservice.exception.StatusCode;
import com.fsocial.accountservice.services.AccountServices;
import jakarta.validation.Valid;
import jdk.jshell.Snippet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class AcountController {

    AccountServices accountServices;

    @GetMapping
    public ResponseEntity<Response> login() {

        return ResponseEntity.ok().body(
                Response.builder()
                        .statusCode(StatusCode.OK.getCode())
                        .message(StatusCode.OK.getMessage())
                        .data(null)
                        .build());

    }


    @PostMapping
    public ResponseEntity<Response> addAccount(@RequestBody @Valid AccountDTO accountDTO) {
        try {
            AccountDTO result = accountServices.registerUser(accountDTO);
            return ResponseEntity.ok().body(Response.builder()
                    .statusCode(StatusCode.OK.getCode())
                    .message(StatusCode.OK.getMessage())
                    .data(result)
                    .build());
        } catch (AppCheckedException e) {
            return ResponseEntity.status(e.getStatus().getCode()).body(Response.builder()
                    .statusCode(StatusCode.OK.getCode())
                    .message(StatusCode.OK.getMessage())
                    .data(null)
                    .build());
        }
    }
}

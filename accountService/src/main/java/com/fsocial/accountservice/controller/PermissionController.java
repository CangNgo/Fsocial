package com.fsocial.accountservice.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.role.PermissionRequest;
import com.fsocial.accountservice.dto.response.role.PermissionResponse;
import com.fsocial.accountservice.services.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/permission")
public class PermissionController {

        PermissionService permissionService;

        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping
        public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
                return ApiResponse.<PermissionResponse>builder()
                                .message("Create new Permission success.")
                                .data(permissionService.createPermission(request))
                                .build();
        }

        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping
        public ApiResponse<List<PermissionResponse>> getAllPermission() {
                return ApiResponse.<List<PermissionResponse>>builder()
                                .message("Get all permission success.")
                                .data(permissionService.getAllPermissions())
                                .build();
        }

        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping("/{permissionId}")
        public ApiResponse<Void> deletePermission(@PathVariable String permissionId) {
                permissionService.deletePermission(permissionId);
                return ApiResponse.<Void>builder()
                                .message("Delete permission success.")
                                .build();
        }
}

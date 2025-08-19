package com.fsocial.accountservice.controller;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.role.RoleCreationRequest;
import com.fsocial.accountservice.dto.response.role.RoleResponse;
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.services.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/role")
public class RoleController {

        RoleService roleService;

        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping
        public ApiResponse<RoleResponse> createRole(@RequestBody RoleCreationRequest request) {
                return ApiResponse.<RoleResponse>builder()
                                .statusCode(ErrorCode.OK.getCode())
                                .message("Create new Role success.")
                                .data(roleService.createRole(request))
                                .build();
        }

        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping
        public ApiResponse<List<RoleResponse>> getAllRole() {
                return ApiResponse.<List<RoleResponse>>builder()
                                .statusCode(ErrorCode.OK.getCode())
                                .message("Get all Role success.")
                                .data(roleService.getAllRoles())
                                .build();
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/{roleId}")
        public ApiResponse<RoleResponse> updateRole(
                        @PathVariable String roleId,
                        @RequestBody Set<String> permissions) {

                return ApiResponse.<RoleResponse>builder()
                                .statusCode(ErrorCode.OK.getCode())
                                .message("Update role success.")
                                .data(roleService.updateRole(roleId, permissions))
                                .build();
        }

        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping("/{roleId}")
        public ApiResponse<Void> deleteRole(@PathVariable String roleId) {
                roleService.deleteRole(roleId);

                return ApiResponse.<Void>builder()
                                .statusCode(ErrorCode.OK.getCode())
                                .message("Delete role success.")
                                .build();
        }
}

package com.fsocial.accountservice.controller;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.RoleCreationRequest;
import com.fsocial.accountservice.dto.response.RoleResponse;
import com.fsocial.accountservice.exception.StatusCode;
import com.fsocial.accountservice.services.impl.RoleServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/role")
public class RoleController {

    RoleServiceImpl roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleCreationRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .statusCode(StatusCode.OK.getCode())
                .message("Create new Role success.")
                .data(roleService.createRole(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRole() {
        return ApiResponse.<List<RoleResponse>>builder()
                .statusCode(StatusCode.OK.getCode())
                .message("Get all Role success.")
                .data(
                        roleService.getAllRoles()
                )
                .build();
    }

    @PutMapping("/{roleId}")
    public ApiResponse<RoleResponse> updateRole(
            @PathVariable String roleId,
            @RequestBody Set<String> permissions
            ) {

        return ApiResponse.<RoleResponse>builder()
                .statusCode(StatusCode.OK.getCode())
                .message("Update role success.")
                .data(
                        roleService.updateRole(roleId, permissions)
                )
                .build();
    }

    @DeleteMapping("/{roleId}")
    public ApiResponse<Void> deleteRole(@PathVariable String roleId) {
        roleService.deleteRole(roleId);

        return ApiResponse.<Void>builder()
                .statusCode(StatusCode.OK.getCode())
                .message("Delete role success.")
                .build();
    }
}

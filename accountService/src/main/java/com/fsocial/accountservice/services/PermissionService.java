package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.request.role.PermissionRequest;
import com.fsocial.accountservice.dto.response.role.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest request);
    List<PermissionResponse> getAllPermissions();
    void deletePermission(String name);
}

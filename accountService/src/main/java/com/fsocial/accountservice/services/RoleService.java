package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.request.RoleCreationRequest;
import com.fsocial.accountservice.dto.response.RoleResponse;

import java.util.List;
import java.util.Set;

public interface RoleService {
    RoleResponse createRole(RoleCreationRequest request);
    List<RoleResponse> getAllRoles();
    RoleResponse updateRole(String roleId, Set<String> newPermissions);
    void deleteRole(String name);
}

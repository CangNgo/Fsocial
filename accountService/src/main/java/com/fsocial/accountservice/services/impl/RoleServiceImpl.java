package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.request.role.RoleCreationRequest;
import com.fsocial.accountservice.dto.response.role.RoleResponse;
import com.fsocial.accountservice.entity.Permission;
import com.fsocial.accountservice.entity.Role;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.enums.StatusCode;
import com.fsocial.accountservice.mapper.RoleMapper;
import com.fsocial.accountservice.repository.PermissionRepository;
import com.fsocial.accountservice.repository.RoleRepository;
import com.fsocial.accountservice.services.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleCreationRequest request) {
        Role role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleResponse(
                roleRepository.save(role)
        );
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    @Override
    public RoleResponse updateRole(String roleId, Set<String> newPermissions) {
        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new AppException(StatusCode.NOT_FOUND)
        );

        var permissions = permissionRepository.findAllById(newPermissions);

        Set<Permission> permissionRole = role.getPermissions();

        permissions.stream()
                .peek(permissionRole::add)
                .toList();

        role.setPermissions(permissionRole);

        return roleMapper.toRoleResponse(
                roleRepository.save(role)
        );
    }

    @Override
    public void deleteRole(String name) {
        roleRepository.deleteById(name);
    }
}

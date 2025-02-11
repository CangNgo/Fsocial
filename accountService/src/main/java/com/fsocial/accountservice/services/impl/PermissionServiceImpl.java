package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.request.role.PermissionRequest;
import com.fsocial.accountservice.dto.response.role.PermissionResponse;
import com.fsocial.accountservice.entity.Permission;
import com.fsocial.accountservice.mapper.PermissionMapper;
import com.fsocial.accountservice.repository.PermissionRepository;
import com.fsocial.accountservice.services.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);

        return permissionMapper.toPermissionResponse(
                permissionRepository.save(permission)
        );
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    @Override
    public void deletePermission(String name) {
        permissionRepository.deleteById(name);
    }
}

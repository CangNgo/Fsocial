package com.fsocial.accountservice.mapper;

import com.fsocial.accountservice.dto.request.role.PermissionRequest;
import com.fsocial.accountservice.dto.response.role.PermissionResponse;
import com.fsocial.accountservice.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}

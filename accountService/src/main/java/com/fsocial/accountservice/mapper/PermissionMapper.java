package com.fsocial.accountservice.mapper;

import com.fsocial.accountservice.dto.request.PermissionRequest;
import com.fsocial.accountservice.dto.response.PermissionResponse;
import com.fsocial.accountservice.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}

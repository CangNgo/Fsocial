package com.fsocial.accountservice.mapper;

import com.fsocial.accountservice.dto.request.role.RoleCreationRequest;
import com.fsocial.accountservice.dto.response.role.RoleResponse;
import com.fsocial.accountservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationRequest request);

    RoleResponse toRoleResponse(Role role);
}

package org.pragma.creditya.r2dbc.persistence.role.mapper;

import org.pragma.creditya.model.role.Role;
import org.pragma.creditya.r2dbc.helper.CustomMapper;
import org.pragma.creditya.r2dbc.persistence.role.entity.RoleEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper implements CustomMapper<Role, RoleEntity> {

    @Override
    public RoleEntity toData(Role entity) {
        return RoleEntity.builder()
                .roleId(entity.getId().id())
                .name(entity.getName().name())
                .build();
    }

    @Override
    public Role toEntity(RoleEntity data) {
        return Role.RoleBuilder.aRole()
                .id(data.getRoleId())
                .name(data.getName())
                .build();
    }
}

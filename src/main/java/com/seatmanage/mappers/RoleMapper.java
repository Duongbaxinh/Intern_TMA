package com.seatmanage.mappers;

import com.seatmanage.dto.request.RoleRequest;
import com.seatmanage.dto.response.RoleDTO;
import com.seatmanage.entities.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toRole(RoleRequest roleRequest);

    RoleDTO toRoleDTO(Role role);
}

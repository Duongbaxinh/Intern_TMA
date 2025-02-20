package com.seatmanage.mappers;

import com.seatmanage.dto.request.PermissionRequest;
import com.seatmanage.dto.response.PermissionDTO;
import com.seatmanage.entities.PermissionActive;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionActive toPermission(PermissionRequest permissionRequest);

    PermissionDTO toPermissionDTO(PermissionActive permissionActive);
}

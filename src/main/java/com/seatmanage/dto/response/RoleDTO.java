package com.seatmanage.dto.response;

import com.seatmanage.entities.PermissionActive;
import lombok.Data;

import java.util.HashSet;

@Data
public class RoleDTO {
    public String name;
    public HashSet<PermissionActive> permissionActives;
}

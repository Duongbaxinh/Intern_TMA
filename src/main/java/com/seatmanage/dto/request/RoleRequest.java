package com.seatmanage.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RoleRequest {
    public String roleName;
    public List<String> permission;
}

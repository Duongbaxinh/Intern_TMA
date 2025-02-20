package com.seatmanage.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RoleUpdateRequest {
    List<String> permissions;
}

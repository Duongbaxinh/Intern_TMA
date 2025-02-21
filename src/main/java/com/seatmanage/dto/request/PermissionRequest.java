package com.seatmanage.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PermissionRequest {
    @NotNull(message = "not null")
    public String name;
    public String description;
}

package com.seatmanage.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class HallRequest {
    String name;
    String description;
    @NotNull(message = "FLOOR_NOT_NULL")
    String floorId;
}

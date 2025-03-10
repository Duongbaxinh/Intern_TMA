package com.seatmanage.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PUBLIC)
public class RoomRequest {
    String name;
    String description;
    String userId;
    int capacity;
    @NotNull(message = "HALL_NOT_NULL")
    String hallId;
}

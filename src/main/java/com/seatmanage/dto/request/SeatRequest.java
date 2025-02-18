package com.seatmanage.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PUBLIC)
public class SeatRequest {
    String name;
    String description;
    double posX;
    double posY;
    String typeSeat;
    @NotNull(message = "ROOM_NOT_NULL")
    String roomId;
    String userId;
}

package com.seatmanage.dto.request;

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
    String roomId;
    String userId;
}

package com.seatmanage.dto.request;

import com.seatmanage.dto.response.UserDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PUBLIC)
public class SeatDiagramUpdate {
    String id;
    String name;
    UserDTO user;
    double posX;
    double posY;
}

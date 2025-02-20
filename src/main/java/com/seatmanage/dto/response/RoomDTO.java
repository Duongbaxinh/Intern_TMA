package com.seatmanage.dto.response;


import com.seatmanage.entities.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PUBLIC)
public class RoomDTO {
    String id;
    String name;
    String description;
    String hallId;
    User chief;
    List<SeatDTO> seats;
}

package com.seatmanage.dto.response;


import com.seatmanage.entities.Seat;
import lombok.*;
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
    List<SeatDTO> seats;
}

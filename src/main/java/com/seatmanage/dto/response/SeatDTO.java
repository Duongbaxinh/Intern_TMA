package com.seatmanage.dto.response;


import com.seatmanage.entities.Seat;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PUBLIC)
public class SeatDTO {
     String id;
     String name;
     String description;
     double posX;
     double posY;
     String typeSeat;
     String roomId;
     String userId;
     String color;
}

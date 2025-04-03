package com.seatmanage.dto.response;


import com.seatmanage.entities.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PUBLIC)
public class RoomDTO {
    String id;
    String name;
    String description;
    String hall;
    String floor;
    UserDTO chief;
    String object;
    String image;
    int seatAvailable;
    int capacity;
    int usersCount;
    List<SeatDTO> seats;
}

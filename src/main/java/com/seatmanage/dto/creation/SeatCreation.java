package com.seatmanage.dto.creation;

import com.seatmanage.entities.Room;
import com.seatmanage.entities.Seat;
import com.seatmanage.entities.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PUBLIC)
public class SeatCreation {
     String id;
     String name;
     String description;
    Seat.TypeSeat seatType;
    Room room;
    User user;
}

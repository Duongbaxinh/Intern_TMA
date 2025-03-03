package com.seatmanage.dto.request;

import com.seatmanage.entities.Seat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PUBLIC)
public class SetupSeatRequest {
    Seat.TypeSeat typeSeat;
    LocalDateTime expiration;
}

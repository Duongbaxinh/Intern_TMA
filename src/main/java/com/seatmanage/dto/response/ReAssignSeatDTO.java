package com.seatmanage.dto.response;

import lombok.Data;

@Data
public class ReAssignSeatDTO {
    SeatDTO newSeat;
    SeatDTO oldSeat;
}
    
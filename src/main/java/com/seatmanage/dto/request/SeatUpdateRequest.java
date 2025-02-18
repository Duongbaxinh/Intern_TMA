package com.seatmanage.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PUBLIC)
public class SeatUpdateRequest extends  SeatRequest{
   String roomId;
}

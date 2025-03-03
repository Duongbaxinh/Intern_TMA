package com.seatmanage.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PUBLIC)
public class ReassignSeatRequest {
     String oldSeatId;
     String newSeatId;
     String userId;
}


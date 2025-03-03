package com.seatmanage.dto.request;

import com.seatmanage.entities.Floor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FloorResponse {
    String name;
    String description;
    Floor floor;
}

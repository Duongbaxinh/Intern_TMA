package com.seatmanage.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FloorCreationRequest {
    String name;
    String description;
}

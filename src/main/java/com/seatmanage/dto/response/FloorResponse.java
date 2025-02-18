package com.seatmanage.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatmanage.entities.Floor;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class FloorResponse {
    String name;
    String description;
    Floor floor;
}

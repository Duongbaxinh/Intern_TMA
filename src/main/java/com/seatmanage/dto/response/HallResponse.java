package com.seatmanage.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatmanage.entities.Floor;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class HallResponse {
    String id;
    String name;
    String description;
}

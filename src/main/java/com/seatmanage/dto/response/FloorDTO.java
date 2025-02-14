package com.seatmanage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class FloorDTO {

String id;
    String name;
    String description;
    List<HallResponse> halls;

}

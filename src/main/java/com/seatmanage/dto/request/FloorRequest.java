package com.seatmanage.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class FloorRequest {
    String name;
    String description;
//    Map<String,String> map;
    public Map<String,Object> toMap(FloorRequest floorRequest){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(floorRequest, Map.class);
    }

}

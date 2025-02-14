package com.seatmanage.mappers;

import com.seatmanage.dto.request.FloorRequest;
import com.seatmanage.dto.response.FloorDTO;
import com.seatmanage.entities.Floor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {HallMapper.class})
public interface FloorMapper {
    Floor toFloor(FloorRequest floorRequest);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "halls", source = "hallList")
    FloorDTO toFloorDTO(Floor floor);
}

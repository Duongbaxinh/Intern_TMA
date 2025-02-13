package com.seatmanage.mappers;

import com.seatmanage.dto.request.FloorCreationRequest;
import com.seatmanage.entities.Floor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FloorMapper {
    Floor toFloor(FloorCreationRequest floorCreationRequest);
}

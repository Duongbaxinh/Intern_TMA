package com.seatmanage.mappers;

import com.seatmanage.dto.request.HallRequest;
import com.seatmanage.dto.response.HallDTO;
import com.seatmanage.dto.response.HallResponse;
import com.seatmanage.entities.Hall;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HallMapper {

    @Mapping(target = "hallId",source = "id")
    @Mapping(target = "name",source = "name")
    @Mapping(target = "description",source = "description")
    @Mapping(target = "floorId",source = "floor.id")
    @Mapping(target = "floorName",source = "floor.name")
    HallDTO toHallDTO(Hall hall);

    @Mapping(target = "id",source = "id")
    @Mapping(target = "name",source = "name")
    @Mapping(target = "description",source = "description")
    HallResponse toHallRepo(Hall hall);

    Hall toHall(HallRequest hallRequest);
}

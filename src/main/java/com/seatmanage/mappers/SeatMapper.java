package com.seatmanage.mappers;

import com.seatmanage.dto.request.SeatRequest;
import com.seatmanage.dto.response.SeatDTO;
import com.seatmanage.entities.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    @Mapping(target = "id",source = "id")
    @Mapping(target = "name",source = "name")
    @Mapping(target = "description",source = "description")
    @Mapping(target = "posX",source = "posX")
    @Mapping(target = "posY",source = "posY")
    @Mapping(target = "typeSeat",source = "typeSeat")
    @Mapping(target = "roomId",source = "room.id")
    @Mapping(target = "userId",source = "user.id")
    SeatDTO toSeatDTO(Seat seat);

    Seat toSeat(SeatRequest seatRequest);
}

package com.seatmanage.mappers;

import com.seatmanage.dto.request.SeatRequest;
import com.seatmanage.dto.response.SeatDTO;
import com.seatmanage.entities.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface SeatMapper {
    @Mapping(target = "roomId",source = "room.id")
    @Mapping(target = "user",source = "user")
    @Mapping(target = "color",source = "user.team.code")
    SeatDTO toSeatDTO(Seat seat);

    Seat toSeat(SeatRequest seatRequest);
}

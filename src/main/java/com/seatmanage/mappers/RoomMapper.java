package com.seatmanage.mappers;

import com.seatmanage.dto.request.HallRequest;
import com.seatmanage.dto.response.*;
import com.seatmanage.entities.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {SeatDTO.class})
public interface RoomMapper {

    @Mapping(target = "hallId",source = "hall.id")
    @Mapping(target = "seats",source = "seatList")
    RoomDTO toRoomDTO(Room room);



    Room toHall(HallRequest hallRequest);
}

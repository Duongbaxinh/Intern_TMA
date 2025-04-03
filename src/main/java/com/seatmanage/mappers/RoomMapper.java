package com.seatmanage.mappers;

import com.seatmanage.dto.response.RoomDTO;
import com.seatmanage.dto.response.SeatDTO;
import com.seatmanage.entities.Hall;
import com.seatmanage.entities.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {SeatDTO.class, UserMapper.class, Hall.class})
public interface RoomMapper {

    @Mapping(target = "hall",source = "hall.name")
    @Mapping(target = "seats",source = "seatList")
    @Mapping(target = "image",source = "image")
    @Mapping(target = "floor",source = "hall.floor.name")
    @Mapping(target = "object",source = "object")
    RoomDTO toRoomDTO(Room room);

}

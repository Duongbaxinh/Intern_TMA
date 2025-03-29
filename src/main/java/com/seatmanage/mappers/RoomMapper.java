package com.seatmanage.mappers;

import com.seatmanage.dto.request.HallRequest;
import com.seatmanage.dto.response.*;
import com.seatmanage.entities.Hall;
import com.seatmanage.entities.Room;
import com.seatmanage.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",uses = {SeatDTO.class, UserMapper.class, Hall.class})
public interface RoomMapper {

    @Mapping(target = "hall",source = "hall.name")
    @Mapping(target = "seats",source = "seatList")
    @Mapping(target = "image",source = "image")
    @Mapping(target = "floor",source = "hall.floor.name")
    @Mapping(target = "object",source = "object")
    RoomDTO toRoomDTO(Room room);

    Room toHall(HallRequest hallRequest);
}

package com.seatmanage.mappers;

import com.seatmanage.dto.request.HallRequest;
import com.seatmanage.dto.response.*;
import com.seatmanage.entities.Room;
import com.seatmanage.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",uses = {SeatDTO.class, UserMapper.class})
public interface RoomMapper {

    @Mapping(target = "hallId",source = "hall.id")
    @Mapping(target = "chief" ,source = "chief" , qualifiedByName = "mapChiefToUserDTO")
    @Mapping(target = "seats",source = "seatList")
    @Mapping(target = "object",source = "object")
    RoomDTO toRoomDTO(Room room);

    @Named("mapChiefToUserDTO")
    default UserDTO mapChiefToUserDTO(User chief) {
        if (chief == null) return null;

        UserDTO userDTO = new UserDTO();
        userDTO.setId(chief.getId());
        userDTO.setUsername(chief.getUsername());
        userDTO.setFirstName(chief.getFirstName());
        userDTO.setLastName(chief.getLastName());
        userDTO.setRole("");
        return userDTO;
    }

    Room toHall(HallRequest hallRequest);
}

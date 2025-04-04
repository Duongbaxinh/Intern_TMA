package com.seatmanage.mappers;


import com.seatmanage.dto.request.UserRequest;
import com.seatmanage.dto.response.UserDTO;
import com.seatmanage.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",uses = {TeamMapper.class})
public interface UserMapper {
    User toUser(UserRequest userRequest);

    @Mapping(target = "role",source = "role.name")
    @Mapping(target = "team",source = "team")
    @Mapping(target = "roomId",source = "roomId")
    UserDTO toUserDTO(User user);
}



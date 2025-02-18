package com.seatmanage.mappers;


import com.seatmanage.dto.request.UserRequest;
import com.seatmanage.dto.response.UserDTO;
import com.seatmanage.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);

    @Mapping(target = "roleId",source = "role.id")
    UserDTO toUserDTO(User user);
}



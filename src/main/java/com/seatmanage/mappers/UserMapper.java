package com.seatmanage.mappers;


import com.seatmanage.dto.request.UserRequest;
import com.seatmanage.entities.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);
}



package com.seatmanage.mappers;


import com.seatmanage.dto.request.UserCreationRequest;
import com.seatmanage.entities.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest userCreationRequest);
}



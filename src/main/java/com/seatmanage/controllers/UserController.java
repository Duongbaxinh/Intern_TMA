package com.seatmanage.controllers;

import com.seatmanage.dto.request.ApiResponse;
import com.seatmanage.dto.request.UserCreationRequest;
import com.seatmanage.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/add")
    ApiResponse<Object> addUser(@RequestBody @Valid UserCreationRequest userCreationRequest) {
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(200)
                .msg("add user successfully")
                .result(userService.addUser(userCreationRequest))
                .build();
        return apiResponse;
    }
}

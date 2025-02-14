package com.seatmanage.controllers;

import com.seatmanage.dto.request.UserRequest;
import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping
    ApiResponse<Object> addUser(@RequestBody @Valid UserRequest userRequest) {
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(200)
                .msg("add user successfully")
                .result(userService.addUser(userRequest))
                .build();
        return apiResponse;
    }
    @GetMapping
    ApiResponse<Object> getUser() {
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(200)
                .msg("get all user")
                .result(userService.getAllUsers())
                .build();
        return apiResponse;
    }

}

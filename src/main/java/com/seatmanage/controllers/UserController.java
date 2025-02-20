package com.seatmanage.controllers;

import com.seatmanage.dto.request.UserRequest;
import com.seatmanage.dto.request.UserUpdateRequest;
import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")

public class UserController {
    final
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    ApiResponse<Object> addUser(@RequestBody @Valid UserRequest userRequest) {
        return ApiResponse.builder()
                .code(200)
                .msg("add user successfully")
                .result(userService.addUser(userRequest))
                .build();

    }

    @PutMapping("{id}")
    ApiResponse<Object> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest userRequest) {

       return  ApiResponse.builder().code(200).msg("update user successfully")
                .result(userService.updateUser(id,userRequest)).build();
    }

    @GetMapping
        @PreAuthorize("hasAnyAuthority('ROLE_SUPERUSER','ROLE_LANDLORD')")
    ApiResponse<Object> getUser() {
        return  ApiResponse.builder()
                .code(200)
                .msg("get all user")
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    ApiResponse<Object> getProfile() {
        return  ApiResponse.builder()
                .code(200)
                .msg("get profile user")
                .result(userService.getProfile())
                .build();
    }

    @DeleteMapping("{id}")
    ApiResponse<Object> deleteUser( @PathVariable String id) {
        return ApiResponse.builder()
                .code(200)
                .msg("Delete user with id :" + id)
                .result(userService.deleteUser(id))
                .build();
    }

}

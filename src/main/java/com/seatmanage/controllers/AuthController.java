package com.seatmanage.controllers;

import com.nimbusds.jose.JOSEException;
import com.seatmanage.dto.request.AuthRequest;
import com.seatmanage.dto.request.UserRequest;
import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.repositories.PermissionRepository;
import com.seatmanage.services.AuthService;
import com.seatmanage.services.PermissionService;
import com.seatmanage.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;


    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ApiResponse<Object> authenticated( @RequestBody AuthRequest authRequest) throws JOSEException {
        return ApiResponse.builder().code(200).msg("login successfully").result(
                authService.authenticate(authRequest)
        ).build();
    }
    @PostMapping("/register")
    ApiResponse<Object> addUser(@RequestBody @Valid UserRequest userRequest) {
        return ApiResponse.builder()
                .code(200)
                .msg("add user successfully")
                .result(userService.addUser(userRequest))
                .build();

    }



}

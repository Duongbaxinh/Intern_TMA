package com.seatmanage.controllers;

import com.nimbusds.jose.JOSEException;
import com.seatmanage.dto.request.AuthRequest;
import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.services.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ApiResponse<Object> authenticated( @RequestBody AuthRequest authRequest) throws JOSEException {
        return ApiResponse.builder().code(200).msg("login successfully").result(
                authService.authenticate(authRequest)
        ).build();
    }

}

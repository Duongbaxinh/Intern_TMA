package com.seatmanage.controllers;

import com.seatmanage.dto.request.PermissionRequest;
import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.dto.response.PermissionDTO;
import com.seatmanage.entities.PermissionActive;
import com.seatmanage.services.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
//@PreAuthorize("hasRole('ROLE_SUPERUSER')")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public ApiResponse<Object> createPermission( @RequestBody PermissionRequest permission) {
        return ApiResponse.builder().code(200).msg("create permission success")
                .result(permissionService.createPermission(permission)).build();
    }

    @GetMapping
    public ApiResponse<Object> getAllPermission( ) {
        return  ApiResponse.builder().code(200).msg("").result(permissionService.getAllPermission()).build();
    }

    @GetMapping("{id}")
    public ApiResponse<Object> getPermission( @PathVariable String id) {
        return  ApiResponse.builder().code(200).msg("").result(permissionService.getPermissionById(id)).build();
    }

}

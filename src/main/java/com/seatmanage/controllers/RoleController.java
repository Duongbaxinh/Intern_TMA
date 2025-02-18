package com.seatmanage.controllers;

        import com.seatmanage.dto.request.RoleRequest;
import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.services.RoleService;
import org.springframework.web.bind.annotation.*;

        @RestController
@RequestMapping("/role")
 public class RoleController {
    private final RoleService roleService;

            public RoleController(RoleService roleService) {
                this.roleService = roleService;
            }

            @PostMapping
    public ApiResponse<Object> createRole( @RequestBody RoleRequest request ) {
              return ApiResponse.builder()
                              .code(200)
                              .msg("create role success")
                              .result(roleService.createRole(request))
                              .build();
            }
    @GetMapping
    public ApiResponse<Object> getAllRole() {
                return ApiResponse.builder()
                                .code(200)
                                .msg("create role success")
                                .result(roleService.getAllRole())
                                .build();
            }

      }

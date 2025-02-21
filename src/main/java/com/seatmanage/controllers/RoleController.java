package com.seatmanage.controllers;

        import com.seatmanage.dto.request.RoleRequest;
        import com.seatmanage.dto.request.RoleUpdateRequest;
        import com.seatmanage.dto.response.ApiResponse;
        import com.seatmanage.services.PermissionService;
        import com.seatmanage.services.RoleService;
        import org.springframework.security.access.prepost.PreAuthorize;
        import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/role")
//@PreAuthorize("hasRole('SUPERUSER')")
 public class RoleController {
    private final RoleService roleService;
            private final PermissionService permissionService;

            public RoleController(RoleService roleService, PermissionService permissionService) {
                this.roleService = roleService;
                this.permissionService = permissionService;
            }

            @PostMapping
    public ApiResponse<Object> createRole( @RequestBody RoleRequest request ) {
              return ApiResponse.builder()
                              .code(200)
                              .msg("create role success")
                              .result(roleService.createRole(request))
                              .build();
            }

            @PutMapping("{id}")
            public ApiResponse<Object> addNewPermission( @PathVariable  String id,
                                                         @RequestBody RoleUpdateRequest roleUpdateRequest ) {
                return  ApiResponse.builder().code(200).msg("add permission successfully")
                        .result(roleService.addPermission(id,roleUpdateRequest.getPermissions())).build();
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

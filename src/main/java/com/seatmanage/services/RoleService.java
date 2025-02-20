package com.seatmanage.services;
import com.seatmanage.config.ConfigRole;
import com.seatmanage.dto.request.RoleRequest;
import com.seatmanage.entities.PermissionActive;
import com.seatmanage.entities.Role;
import com.seatmanage.mappers.RoleMapper;
import com.seatmanage.repositories.RoleRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PUBLIC)
public class RoleService
{
    final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionService permissionService;

    public RoleService(RoleMapper roleMapper, RoleRepository roleRepository, PermissionService permissionService) {
            this.roleMapper = roleMapper;
            this.roleRepository = roleRepository;
        this.permissionService = permissionService;
    }
//    public Role getRoleById(String roleId){
//        Role role =  roleRepository.findById(roleId).orElse(null);
//        System.out.println("Check role " + role);
//        if(role == null){ throw  new RuntimeException("Role Not Found"); }
//        return role;
//    }

    public Role getRoleByName(ConfigRole roleName){
        Role role =  roleRepository.findRoleByName(roleName).orElse(null);
        System.out.println("Check role " + role);
        if(role == null){ throw  new RuntimeException("Role Not Found"); }
        return role;
    }

    public Role createRole(RoleRequest roleRequest){
        System.out.println("Create Role");
        Role roleEx = roleRepository.findRoleByName(ConfigRole.valueOf(roleRequest.getRoleName())).orElse(null);
        System.out.println("Check role 1");
        if(roleEx != null) throw new  RuntimeException("Role Exist");
        System.out.println("Check role 2");
        List<PermissionActive> permissionActives =null;
        if(roleRequest.permission != null){
            System.out.println("Check role 3");
             permissionActives = permissionService.getPermissionList(roleRequest.permission);
        }
        System.out.println("check" + permissionActives);
        Role role = Role.builder().name(ConfigRole.valueOf(roleRequest.getRoleName()))
                        .permissionActives(permissionActives).build();
        return roleRepository.save(role);
    }

    public List<Role> getAllRole(){
              return  roleRepository.findAll();
          }
          public Role addPermission(String roleId,List<String> permissionActiveList){
            Role role = roleRepository.findRoleByName(ConfigRole.valueOf(roleId)).orElseThrow(() -> new RuntimeException(
                    "Role Not " +
                    "Found"));
            permissionActiveList.forEach(permissionActive -> {
            PermissionActive permission = Optional.ofNullable(permissionActive)
                    .map(permissionService::getPermissionById)
                    .orElseThrow(()->new RuntimeException("Permission Not Found"));
            role.getPermissionActives().add(permission);
                });
            return roleRepository.save(role);
          }


}

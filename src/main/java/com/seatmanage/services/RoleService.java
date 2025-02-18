package com.seatmanage.services;
import com.seatmanage.config.ConfigRole;
import com.seatmanage.dto.request.RoleRequest;
import com.seatmanage.entities.Role;
import com.seatmanage.mappers.RoleMapper;
import com.seatmanage.repositories.RoleRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PUBLIC)
public class RoleService
{
    final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

            public RoleService(RoleMapper roleMapper, RoleRepository roleRepository) {
            this.roleMapper = roleMapper;
            this.roleRepository = roleRepository;
        }
    Role getRoleById(String roleId){
                Role role =  roleRepository.findById(roleId).orElse(null);
                if(role == null){ throw  new RuntimeException("Role Not Found"); }
                return role;
            }

            public Role createRole(RoleRequest roleRequest){
                Role roleEx = roleRepository.findRoleByName(ConfigRole.valueOf(roleRequest.getRoleName())).orElse(null);
                if(roleEx != null) throw new  RuntimeException("Role Exist");
                Role role = Role.builder().roleName(ConfigRole.valueOf(roleRequest.getRoleName())).build();
                return roleRepository.save(role);

            }
    public List<Role> getAllRole(){
              return  roleRepository.findAll();
          }


}

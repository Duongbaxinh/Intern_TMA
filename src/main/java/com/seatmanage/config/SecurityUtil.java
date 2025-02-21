package com.seatmanage.config;

import com.seatmanage.dto.response.UserPrivateDTO;
import com.seatmanage.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.attribute.UserPrincipal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SecurityUtil {

    private final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    public static boolean isPrivate(User user) {
        return isPrivate(true, user);
    }

    public static  enum RoleAuth {
        USER,LANDLORD,SUPERUSER
    }
    public static  enum PermissionAuth {
        DELETE_SEAT,GET_SEAT,CREATE_SEAT,UPDATE_SEAT,
        DELETE_ROOM,GET_ROOM,CREATE_ROOM,UPDATE_ROOM,
        DELETE_HALL,GET_HALL,CREATE_HALL,UPDATE_HALL,
        DELETE_USER,GET_USER,CREATE_USER,UPDATE_USER,
    }

    public static boolean isPrivate( boolean isOwn , User user){
        UserPrivateDTO userPrivateDTO = getUserPrincipal();

        if(userPrivateDTO ==null) throw  new RuntimeException("User must login !!!");
        boolean isSuperUser = userPrivateDTO.getPermissions().contains("ROLE_SUPERUSER");
        if (!isSuperUser ) {
            if(!isOwn)  return  false;
            System.out.println("run at here pass" + userPrivateDTO.getUsername() + "=== " + user.getUsername());
            return  userPrivateDTO.getUsername().equals(user.getUsername());
        }
        return true;
    }

    public static UserPrivateDTO getUserPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()){
            return null;
        }
        List<String> authorities =
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
        return  UserPrivateDTO.builder().username(authentication.getName())
                .permissions(authorities).build();
    }
}

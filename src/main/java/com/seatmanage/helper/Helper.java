package com.seatmanage.helper;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Data
@NoArgsConstructor
public class Helper {
    public  boolean isOwn = true;
    public String username;
    public boolean isPrivate( boolean isOwn , String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isSuperUser = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPERUSER"));
        if (!isSuperUser ) {
            if(!isOwn)  throw new RuntimeException("Forbidden");
            if(!authentication.getName().equals(username)) throw new RuntimeException("Forbidden!!!");
        }
    return true;
    }
}

package com.seatmanage.config;

import com.seatmanage.entities.PermissionActive;
import com.seatmanage.entities.Role;
import com.seatmanage.entities.User;
import com.seatmanage.repositories.UserRepository;
import org.springframework.core.SimpleAliasRegistry;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationConvert implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;

    public CustomAuthenticationConvert(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String username = jwt.getSubject();

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GrantedAuthority> listAuth = new ArrayList<>();
        if(user.getRole() != null){
        listAuth.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
            user.getRole().getPermissionActives().forEach(permissionActive -> {
                listAuth.add(new SimpleGrantedAuthority(permissionActive.getName()));
            });
        }

        System.out.println("check list authority" + listAuth);
        return new JwtAuthenticationToken(jwt,listAuth);
    }
}

package com.seatmanage.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.JOSEException;
import com.seatmanage.dto.request.AuthRequest;
import com.seatmanage.dto.response.AuthDTO;
import com.seatmanage.entities.User;
import com.seatmanage.exception.AppExceptionHandle;
import com.seatmanage.exception.ErrorCode;
import com.seatmanage.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Service
public class AuthService {
    private final UserService userService;
   private final PasswordEncoder passwordEncoder;
   private final UserRepository userRepository;
   private final JwtService jwtService;
    public AuthService(UserService userService, UserRepository userRepository, JwtService jwtService) {
               this.userService = userService;
               this.passwordEncoder = new BCryptPasswordEncoder();
               this.userRepository = userRepository;
               this.jwtService = jwtService;
           }

    public AuthDTO authenticate(AuthRequest authRequest) throws JOSEException {
               User user = userRepository.findByUserName(authRequest.getUsername())
                                   .orElseThrow(()-> new AppExceptionHandle(ErrorCode.NOT_FOUND_USER));
               boolean authenticated = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());

               if (!authenticated) throw new RuntimeException("user name or password is not correct");
               String accessToken = jwtService.generateToken(authRequest.getUsername());
               return  AuthDTO.builder().id(user.getId())
                               .username(user.getUsername())
                               .room(String.valueOf(user.getRoomId()))
                               .role(String.valueOf(user.getRole().getName()))
                               .accessToken(accessToken)
                               .build();
           }


}

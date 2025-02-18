package com.seatmanage.services;

import com.seatmanage.dto.request.UserCreationRequest;
import com.seatmanage.entities.User;
import com.seatmanage.exception.AppExceptionHandle;
import com.seatmanage.exception.ErrorCode;
import com.seatmanage.mappers.UserMapper;
import com.seatmanage.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
   private final PasswordEncoder passwordEncoder;
   private final RoleService roleService;
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
               this.passwordEncoder = passwordEncoder;
               this.roleService = roleService;
    }


    public UserDTO addUser(UserRequest user) {
                 User userEx = userRepository.findByUserName(user.getUsername()).orElse(null);
                 if(userEx != null) throw new RuntimeException("user already exist");
                Role role =  Optional.ofNullable(user.getRoleId()).map(roleService::getRoleById).orElse(null);
                user.setPassword( passwordEncoder.encode(user.getPassword()));
        User newUser = userMapper.toUser(user);
               return userRepository.save(newUser);

                        newUser.setRole(role);
               return userMapper.toUserDTO(userRepository.save(newUser));
    }

    public List<UserDTO> getAllUsers() {
               return userRepository.findAll()
                               .stream()
                               .map(userMapper::toUserDTO)
                               .collect(Collectors.toList());
           }

    public UserDTO deleteUser(String userId) {
                User user = userRepository.findById(userId).orElse(null);
                if(user == null) throw  new RuntimeException("User not found");
                userRepository.deleteById(userId);
                return userMapper.toUserDTO(user);
    }
    public User getUserByUserName(String username){
                User user =  userRepository.findByUserName(username).orElse(null);
                if(user == null) throw new AppExceptionHandle(ErrorCode.NOT_FOUND_USER);
                return user;
            }



}

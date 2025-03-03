package com.seatmanage.services;

import com.seatmanage.config.SecurityUtil;
import com.seatmanage.dto.request.UserRequest;
import com.seatmanage.dto.request.UserUpdateRequest;
import com.seatmanage.dto.response.UserDTO;
import com.seatmanage.dto.response.UserPrivateDTO;
import com.seatmanage.entities.Role;
import com.seatmanage.entities.Team;
import com.seatmanage.entities.User;
import com.seatmanage.exception.AppExceptionHandle;
import com.seatmanage.exception.ErrorCode;
import com.seatmanage.mappers.UserMapper;
import com.seatmanage.repositories.RoleRepository;
import com.seatmanage.repositories.TeamRepository;
import com.seatmanage.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
   private final PasswordEncoder passwordEncoder;
   private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final TeamService teamService;
    private final TeamRepository teamRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, RoleService roleService, RoleRepository roleRepository, TeamService teamService, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
               this.passwordEncoder = passwordEncoder;
               this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.teamService = teamService;
        this.teamRepository = teamRepository;
    }


    public UserDTO addUser(UserRequest user) {
        User userEx = userRepository.findByUserName(user.getUsername()).orElse(null);
        if(userEx != null) throw new RuntimeException("user already exist");
        System.out.println("run at 1" + user.getRoleName());
        Role role = roleRepository.findRoleByName(SecurityUtil.RoleAuth.valueOf(user.getRoleName())).orElse(null);

        Team team =
                Optional.ofNullable(user.getTeamId()).map(teamId -> teamRepository.findById(teamId)
                        .orElseThrow(() -> new RuntimeException("team not found"))).orElse(null);
        user.setPassword( passwordEncoder.encode(user.getPassword()));
        User newUser = userMapper.toUser(user);
        newUser.setRole(role);
        newUser.setTeam(team);
        return userMapper.toUserDTO(userRepository.save(newUser));
    }

    public List<UserDTO> getAllUsers() {
               return userRepository.findAllUser()
                               .stream()
                               .map(userMapper::toUserDTO)
                               .collect(Collectors.toList());
           }

    public UserDTO deleteUser(String userId) {
                User user = userRepository.findById(userId).orElse(null);
                if(user == null) throw  new RuntimeException("User not found");
                user.setDeleted(true);
                return userMapper.toUserDTO(userRepository.save(user));
    }

    public UserDTO updateUser(String userId, UserUpdateRequest userUpdateRequest) {
        User user = getUserById(userId);
        boolean isPrivate = SecurityUtil.isPrivate(user);

        if(!isPrivate) throw new RuntimeException("user is not allow");

        Optional.ofNullable(userUpdateRequest.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(userUpdateRequest.getLastName()).ifPresent(user::setLastName);

        Optional.ofNullable(userUpdateRequest.getTeamId())
                .map(teamId -> Optional.ofNullable(teamService.getTeamById(teamId)).orElseThrow(()-> new RuntimeException("team not found")))
                        .ifPresent(user::setTeam);
        Optional.ofNullable(userUpdateRequest.getRoleName())
                .map(roleName -> Optional.ofNullable(roleService.getRoleByName(SecurityUtil.RoleAuth.valueOf(roleName))).orElseThrow(()->new RuntimeException("Role not found")))
                .ifPresent(user::setRole);

        return userMapper.toUserDTO(userRepository.save(user));
    }
    public User getUserById(String id) {
        User userExisted =  userRepository.findById(id).orElse(null);
        if(userExisted == null) throw  new RuntimeException("User not found");
        return userExisted;
    }

    public User getUserByUserName(String username){
                User user =  userRepository.findByUserName(username).orElse(null);
                if(user == null) throw new AppExceptionHandle(ErrorCode.NOT_FOUND_USER);
                return user;
            }

    public UserDTO getProfile(){
        UserPrivateDTO userPrivateDTO = SecurityUtil.getUserPrincipal();
        if(userPrivateDTO == null) throw  new AppExceptionHandle(ErrorCode.NOT_FOUND_USER);
        User user = userRepository.findByUserName(userPrivateDTO.getUsername()).orElse(null);
        if(user == null) throw new AppExceptionHandle(ErrorCode.NOT_FOUND_USER);
        return userMapper.toUserDTO(user);
    }


}

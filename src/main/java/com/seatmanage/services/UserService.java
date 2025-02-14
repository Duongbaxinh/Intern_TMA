package com.seatmanage.services;

import com.seatmanage.dto.request.UserRequest;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public User addUser(UserRequest user) {
        List<User> userExited = userRepository.findByEmailAddress(user.getFirstName());
        if(!userExited.isEmpty()){
            throw new AppExceptionHandle(ErrorCode.EXISTED_USER);
        }
        User newUser = userMapper.toUser(user);
       return userRepository.save(newUser);

    }
    public User getUserById(String id) {
        User userExisted =  userRepository.findById(id).orElse(null);
        if(userExisted == null)throw  new RuntimeException("User not found");
        return userExisted;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

package com.seatmanage.repositories;

import com.seatmanage.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    @Query("select user from User user where user.deleted = false ")
    List<User> findAllUser();

    @Query("select  user from User user where user.deleted = false  and user.username = :userName")
    Optional<User> findByUserName(String userName);

}

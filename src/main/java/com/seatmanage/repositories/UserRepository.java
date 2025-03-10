package com.seatmanage.repositories;

import com.seatmanage.dto.response.UserDTO;
import com.seatmanage.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    @Query("SELECT u FROM User u WHERE u.deleted = false AND u.roomId = :roomId")
    List<User> getUserInRoom(String roomId);

    @Query("select user from User user where user.deleted = false ")
    List<User> findAllUser();

    @EntityGraph(attributePaths = {"role.permissionActives"})
    @Query("select  user from User user where user.deleted = false  and user.username = :userName")
    Optional<User> findByUserName(String userName);


}

package com.seatmanage.repositories;

import com.seatmanage.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    @Query("select u from User u where u.firstName = :firstname")
    List<User> findByFirstname(String firstname);
}

package com.seatmanage.repositories;

import com.seatmanage.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    
    List<User> findByEmailAddress(String firstName);
}

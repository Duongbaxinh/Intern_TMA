package com.seatmanage.repositories;

import com.seatmanage.config.ConfigRole;
import com.seatmanage.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    @Query
            ("select role from Role role where role.name = :roleName")
    public Optional<Role> findRoleByName(ConfigRole roleName);
}

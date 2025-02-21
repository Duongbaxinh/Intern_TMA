package com.seatmanage.repositories;

import com.seatmanage.config.SecurityUtil;
import com.seatmanage.entities.PermissionActive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionActive,String> {

    @Query("select p from PermissionActive  p where p.name = :permission ")
    public Optional<PermissionActive> findByName(SecurityUtil.PermissionAuth permission);

}

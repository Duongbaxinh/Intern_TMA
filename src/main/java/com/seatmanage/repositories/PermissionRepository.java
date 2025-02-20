package com.seatmanage.repositories;

import com.seatmanage.entities.PermissionActive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionActive,String> {


}

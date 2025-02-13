package com.seatmanage.repositories;

import com.seatmanage.entities.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FloorRepository extends JpaRepository<Floor,String> {
    @Query("select f  from  Floor f where f.name = :floorName ")
    Optional<Floor> getFloorByFloorName(String floorName);
}

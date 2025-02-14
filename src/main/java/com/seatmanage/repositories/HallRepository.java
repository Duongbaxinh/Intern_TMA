package com.seatmanage.repositories;

import com.seatmanage.dto.response.HallDTO;
import com.seatmanage.entities.Hall;
import com.seatmanage.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall,String> {
    Optional<Hall> findHallByName(String name);

    @Query("SELECT h from Hall h where  h.name = :hallName and h.floor.id = :floorId ")
    Optional<Hall> findByHallNamedAndFloorId(String hallName, String floorId);

    @Query("SELECT new com.seatmanage.dto.response.HallDTO(h.id,h.name, h.description, f.id, f.name) " +
            "FROM Hall h JOIN h.floor f")
    List<HallDTO> findHalls();


}

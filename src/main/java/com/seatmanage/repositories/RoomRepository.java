package com.seatmanage.repositories;

import com.seatmanage.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,String> {
    @Query("SELECT f from Room f where  f.name = :nameRoom and f.hall.id = :hallId")
    Optional<Room> findRoomByNamAndHallId(String nameRoom, String hallId);

}

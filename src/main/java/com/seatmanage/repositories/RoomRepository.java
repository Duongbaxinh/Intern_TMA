package com.seatmanage.repositories;

import com.seatmanage.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,String> {
    @Query("SELECT f from Room f where  f.name = :nameRoom and f.hall.id = :hallId")
    Optional<Room> findRoomByNamAndHallId(String nameRoom, String hallId);

    @Query("SELECT r FROM Room r WHERE " +
            "(:hallId IS NULL OR r.hall.id = :hallId)")
    Page<Room> findRoomsWithFilters( @Param("hallId") String hallId,
                                    Pageable pageable);
}

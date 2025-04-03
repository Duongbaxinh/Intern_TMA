package com.seatmanage.repositories;

import com.seatmanage.entities.Seat;
import com.seatmanage.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat,String> {
    @Query("SELECT s from Seat s where  s.name = :seatName and s.room.id = :roomId ")
    Optional<Seat> findSeatByNamAndRoomId(String seatName, String roomId);

    @Query("SELECT s from Seat s where  s.user.id = :userId ")
    Optional<Seat> findSeatByUserId(String userId);

    @Query("select s from Seat s where s.room.id = :romId and s.user != null ")
    List<Seat> findSeatOccupantByRoomId(String roomId);

    @Query("SELECT s.user  from Seat s where s.id = :seatId ")
    Optional<User> findUserBySeat(String seatId);

    @Query("select s from Seat s where  s.room.id = :roomId")
    List<Seat> findSeatByRoomId(String roomId);

    @Query("SELECT s FROM Seat s WHERE " +
                "(:roomId IS NULL OR s.room.id = :roomId)"
                + "AND (:typeSeat IS NULL OR s.typeSeat = :typeSeat)"
    )

    Page<Seat> findSeatsWithFilters(@Param("roomId") String roomId,
                                    @Param("typeSeat") Seat.TypeSeat typeSeat,
                                    @Param("isOccupied") Boolean isOccupied,
                                    Pageable pageable);
    @Query("select seat.user.id from  Seat  seat  where seat.room.id = :roomId")
    List<String> getUserAssignByRoomId(String roomId);

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.room.id = :roomId AND s.user IS NULL")
    int countSeatAvailableByRoomId(String roomId);


}

package com.seatmanage.services;

import com.seatmanage.dto.request.SeatRequest;
import com.seatmanage.dto.response.SeatDTO;
import com.seatmanage.entities.Room;
import com.seatmanage.entities.Seat;
import com.seatmanage.entities.User;
import com.seatmanage.mappers.RoomMapper;
import com.seatmanage.mappers.SeatMapper;
import com.seatmanage.repositories.SeatRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PUBLIC)
public class SeatService {

    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private SeatMapper seatMapper;

    public SeatDTO getSeatById(String seatId) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(()->new RuntimeException("Seat Not Found"));
        return seatMapper.toSeatDTO(seat);
    }

    public Seat getSeatByIdDefault(String seatId) {
        return seatRepository.findById(seatId).orElseThrow(()->new RuntimeException("Seat Not Found"));
    }

    public SeatDTO createSeat(SeatRequest seatRequest) {
        Seat seatExisted =
                seatRepository.findRoomByNamAndRoomId(seatRequest.name,seatRequest.roomId).orElse(null);
        if (seatExisted != null) throw new RuntimeException("Seat already exist");
        Room room = roomService.getRoomByIdDefault(seatRequest.roomId);
        User user = null;
        if(seatRequest.getUserId() != null ) {
            user = userService.getUserById(seatRequest.userId);
        }
        Seat seat =
                Seat.builder().name(seatRequest.name)
                        .description(seatRequest.description)
                        .description(seatRequest.description)
                        .typeSeat(Seat.TypeSeat.valueOf(seatRequest.typeSeat))
                        .room(room)
                        .user(user)
                        .build();
        return seatMapper.toSeatDTO(seatRepository.save(seat));
    }

    public List<SeatDTO> getAll(){
        List<Seat> seats = seatRepository.findAll();
        return seats.stream()
                .map(seat -> seatMapper.toSeatDTO(seat))
                .collect(Collectors.toList());
    }

    public SeatDTO updateSeat(String seatId,SeatRequest seatRequest) {
        Seat seat = getSeatByIdDefault(seatId);
        Optional.ofNullable(seatRequest.getName()).ifPresent(seat::setName);
        Optional.ofNullable(seatRequest.getDescription()).ifPresent(seat::setDescription);
        Optional.of(seatRequest.getPosX()).ifPresent(seat::setPosX);
        Optional.of(seatRequest.getPosY()).ifPresent(seat::setPosY);
        Optional.of(Seat.TypeSeat.valueOf(seatRequest.getTypeSeat())).ifPresent(seat::setTypeSeat);

        Optional.ofNullable(seatRequest.getRoomId())
                .map(roomService::getRoomByIdDefault)
                .ifPresent(seat::setRoom);

        Optional.ofNullable(seatRequest.getUserId())
                .map(userService::getUserById)
                .ifPresent(seat::setUser);
        return seatMapper.toSeatDTO(seatRepository.save(seat));
    }

    public SeatDTO deleteSeat(String seatId){
        SeatDTO seat = getSeatById(seatId);
        seatRepository.deleteById(seatId);
        return seat;
    }

}

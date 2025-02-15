package com.seatmanage.services;

import com.seatmanage.dto.request.SeatRequest;
import com.seatmanage.dto.response.SeatDTO;
import com.seatmanage.entities.Room;
import com.seatmanage.entities.Seat;
import com.seatmanage.entities.User;
import com.seatmanage.mappers.SeatMapper;
import com.seatmanage.repositories.SeatRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PUBLIC)
public class SeatService {

    private final SeatRepository seatRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final SeatMapper seatMapper;

    public SeatService(SeatRepository seatRepository, UserService userService, RoomService roomService, SeatMapper seatMapper) {
        this.seatRepository = seatRepository;
        this.userService = userService;
        this.roomService = roomService;
        this.seatMapper = seatMapper;
    }

    public SeatDTO getSeatById(String seatId) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(()->new RuntimeException("Seat Not Found"));
        return seatMapper.toSeatDTO(seat);
    }

    public Seat getSeatByIdDefault(String seatId) {
        return seatRepository.findById(seatId).orElseThrow(()->new RuntimeException("Seat Not Found"));
    }

    public Seat updateUserSeat(Seat seat, String userId) {
        System.out.println("run at here ");
        User user = userService.getUserById(userId);
        if (seat.getUser() != null ) {
            seatRepository.findSeatByUserId(user.getId())
                    .ifPresent(seatEx -> {
                        seatEx.setUser(null);
                        seatRepository.save(seatEx);
                    });
        }
        seat.setUser(user);
        return  seat;
    }

    public SeatDTO createSeat(SeatRequest seatRequest) {


//      Check room existed
        Room room = roomService.getRoomByIdDefault(seatRequest.roomId);

//      Check Seat have in room ?
        seatRepository.findSeatByNamAndRoomId(seatRequest.name,room.getId())
                .ifPresent(roomExisted -> {
                    throw new RuntimeException("Seat already exist");
                });

//      If request have userId -> Check user existed?
        User user = Optional.ofNullable(seatRequest.userId)
                    .map(userId -> Optional.ofNullable(userService.getUserById(userId))
                           .orElseThrow(()-> new RuntimeException("User not found!")))
                    .orElse(null);

//        Check user have seat
        Seat userHasSeat = Optional.ofNullable(user)
                .flatMap(userEx -> seatRepository.findSeatByUserId(userEx.getId()))
                .orElse(null);
        Optional.ofNullable(userHasSeat).ifPresent(seat -> {
            throw new RuntimeException("User have a seat");
        });

        Seat seat = Seat.builder().name(seatRequest.name)
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
                .map(seatMapper::toSeatDTO)
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
                .map(roomId -> Optional.ofNullable(roomService.getRoomByIdDefault(roomId)).orElseThrow(()->new RuntimeException("Room Not Found")))
                .ifPresent(seat::setRoom);
        if(seatRequest.userId != null) {
            seat = updateUserSeat(seat,seatRequest.getUserId());
        }

        return seatMapper.toSeatDTO(seatRepository.save(seat));
    }

    public SeatDTO deleteSeat(String seatId){
        SeatDTO seat = getSeatById(seatId);
        seatRepository.deleteById(seatId);
        return seat;
    }

}

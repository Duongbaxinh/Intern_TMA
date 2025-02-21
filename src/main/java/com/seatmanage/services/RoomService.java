package com.seatmanage.services;

import com.seatmanage.config.SecurityUtil;
import com.seatmanage.dto.request.HallRequest;
import com.seatmanage.dto.request.RoomRequest;
import com.seatmanage.dto.response.RoomDTO;
import com.seatmanage.entities.Hall;
import com.seatmanage.entities.Room;
import com.seatmanage.entities.User;
import com.seatmanage.mappers.RoomMapper;
import com.seatmanage.repositories.RoomRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PUBLIC)
public class RoomService {

    private final RoomRepository roomRepository;
    private final HallService hallService;
    private final RoomMapper roomMapper;
    private final UserService userService;

    public RoomService(RoomRepository roomRepository, HallService hallService, RoomMapper roomMapper, UserService userService) {
        this.roomRepository = roomRepository;
        this.hallService = hallService;
        this.roomMapper = roomMapper;
        this.userService = userService;
    }


    public RoomDTO getRoomById(String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(()-> new RuntimeException("Hall not found"));
        boolean isPrivate =  SecurityUtil.isPrivate(room.chief!= null, room.chief);
        if(!isPrivate) throw new RuntimeException("Not permission to access room");
        return roomMapper.toRoomDTO(room);
    }

    public Room getRoomByIdDefault(String roomId) {
        return roomRepository.findById(roomId).orElseThrow(()-> new RuntimeException("Room not found"));
    }

    public RoomDTO createRoom(RoomRequest roomRequest) {

        Hall hall = Optional.ofNullable(roomRequest.hallId)
                    .map(hallService::getHallByIdDefault)
                    .orElseThrow(()->new RuntimeException("Hall not found"));
        User user = Optional.ofNullable(roomRequest.userId)
                    .map(userService::getUserById)
                    .orElse(null);

        Room roomExisted = roomRepository.findRoomByNamAndHallId(roomRequest.name, roomRequest.hallId).orElse(null);

        if (roomExisted != null) throw new RuntimeException("Room already exist");

        Room room = Room.builder().name(roomRequest.name)
                    .description(roomRequest.description)
                    .hall(hall)
                    .chief(user)
                    .build();
        return roomMapper.toRoomDTO(roomRepository.save(room));
    }

    public List<RoomDTO> getAll(){
        List<Room> rooms = roomRepository.findAll();
        rooms.forEach(room -> {
            if(room.chief != null){

            System.out.println(room.chief.toString());
            }
        });
        return rooms.stream()
                .map(room -> roomMapper.toRoomDTO(room))
                .collect(Collectors.toList());
    }

    public RoomDTO getRoomSeatByChief(String roomId) {

        Room roomEx = roomRepository.findById(roomId).orElseThrow(()-> new RuntimeException("Room not found"));

        boolean isPrivate = SecurityUtil.isPrivate(roomEx.chief!=null, roomEx.chief);
        if(!isPrivate) throw  new RuntimeException("Forbidden !!!");
        return roomMapper.toRoomDTO(roomEx);


    }
    public RoomDTO updateRoom(String roomId, RoomRequest roomUpdate) {
        Room room = getRoomByIdDefault(roomId);
        User user = Optional.ofNullable(roomUpdate.userId).map(userService::getUserById).orElse(null);
        if(user == null) throw new RuntimeException("User not found");
        Optional.ofNullable(roomUpdate.getName()).ifPresent(room::setName);
        Optional.ofNullable(roomUpdate.getDescription()).ifPresent(room::setDescription);

        Optional.ofNullable(roomUpdate.hallId)
                .map(hallId -> Optional.ofNullable(hallService.getHallByIdDefault(hallId))
                        .orElseThrow(()->new RuntimeException("Hall not found")))
                .ifPresent(room::setHall);

        Optional.ofNullable(roomUpdate.userId)
                .map(userId -> Optional.ofNullable(userService.getUserById(userId))
                        .orElseThrow(()->new RuntimeException("User not found")))
                .ifPresent(room::setChief);
        return roomMapper.toRoomDTO(roomRepository.save(room));
    }

    public RoomDTO deleteRoom(String hallId){
        RoomDTO hall = getRoomById(hallId);
        if(hall == null) throw new RuntimeException("Hall Not Found");
        roomRepository.deleteById(hallId);
        return hall;
    }

}

package com.seatmanage.services;

import com.seatmanage.dto.request.HallRequest;
import com.seatmanage.dto.request.RoomRequest;
import com.seatmanage.dto.response.RoomDTO;
import com.seatmanage.entities.Hall;
import com.seatmanage.entities.Room;
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

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HallService hallService;
    @Autowired
    private RoomMapper roomMapper;


    public RoomDTO getRoomById(String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(()-> new RuntimeException("Hall not found"));
        return roomMapper.toRoomDTO(room);
    }

    public Room getRoomByIdDefault(String roomId) {
        return roomRepository.findById(roomId).orElseThrow(()-> new RuntimeException("Room not found"));
    }

    public RoomDTO createRoom(RoomRequest roomRequest) {
        Hall hall =
                Optional.ofNullable(roomRequest.hallId).map(hallService::getHallByIdDefault)
                        .orElseThrow(()->new RuntimeException("Hall not found"));
        Room roomExisted =
                roomRepository.findRoomByNamAndHallId(roomRequest.name, roomRequest.hallId).orElse(null);
        if (roomExisted != null) throw new RuntimeException("Room already exist");
        Room room = Room.builder().name(roomRequest.name).description(roomRequest.description).hall(hall).build();
        return roomMapper.toRoomDTO(roomRepository.save(room));
    }

    public List<RoomDTO> getAll(){
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(room -> roomMapper.toRoomDTO(room))
                .collect(Collectors.toList());
    }

    public RoomDTO updateRoom(String roomId, RoomRequest roomUpdate) {
        Room room = getRoomByIdDefault(roomId);
        Optional.ofNullable(roomUpdate.getName()).ifPresent(room::setName);
        Optional.ofNullable(roomUpdate.getDescription()).ifPresent(room::setDescription);

        Optional.ofNullable(roomUpdate.getHallId())
                .map(hallService::getHallByIdDefault)
                .ifPresent(room::setHall);

        return roomMapper.toRoomDTO(roomRepository.save(room));
    }

    public RoomDTO deleteRoom(String hallId){
        RoomDTO hall = getRoomById(hallId);
        if(hall == null) throw new RuntimeException("Hall Not Found");
        roomRepository.deleteById(hallId);
        return hall;
    }

}

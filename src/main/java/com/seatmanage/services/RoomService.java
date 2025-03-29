package com.seatmanage.services;

import com.cloudinary.api.exceptions.BadRequest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatmanage.config.SecurityUtil;
import com.seatmanage.dto.request.*;
import com.seatmanage.dto.response.RoomDTO;
import com.seatmanage.dto.response.UserDTO;
import com.seatmanage.dto.response.UserPrivateDTO;
import com.seatmanage.entities.Hall;
import com.seatmanage.entities.Room;
import com.seatmanage.entities.Seat;
import com.seatmanage.entities.User;
import com.seatmanage.mappers.RoomMapper;
import com.seatmanage.repositories.RoomRepository;
import com.seatmanage.repositories.SeatRepository;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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
    private final SeatRepository seatRepository;
    private final WebSocketService webSocketService;
    private final CloudinaryService cloudinaryService;
    private final ObjectMapper objectMapper;
    private final RedisService redisService;

    public RoomService(RoomRepository roomRepository, HallService hallService, RoomMapper roomMapper, UserService userService, SeatRepository seatRepository, WebSocketService webSocketService, CloudinaryService cloudinaryService, CloudinaryService cloudinaryService1, ObjectMapper objectMapper, RedisService redisService) {
        this.roomRepository = roomRepository;
        this.hallService = hallService;
        this.roomMapper = roomMapper;
        this.userService = userService;
        this.seatRepository = seatRepository;
        this.webSocketService = webSocketService;
        this.cloudinaryService = cloudinaryService1;
        this.objectMapper = objectMapper;
        this.redisService = redisService;
    }


    public RoomDTO getRoomById(String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(()-> new RuntimeException("Hall not found"));
//        boolean isPrivate =  SecurityUtil.isPrivate(room.chief!= null, room.chief);
//        if(!isPrivate) throw new RuntimeException("Not permission to access room");
        return roomMapper.toRoomDTO(room);
    }

    public Room getRoomByIdDefault(String roomId) {
        return roomRepository.findById(roomId).orElseThrow(()-> new RuntimeException("Room not found"));
    }

    public List<UserDTO> getUserInRoom(String roomId) {
                List<UserDTO> users =  userService.getUsersInRoom(roomId);
                List<String> userIds = seatRepository.getUserAssignByRoomId(roomId);
        List<UserDTO> userResponse = new ArrayList<>();
        users.forEach(user -> {
                        if(!userIds.contains(user.id)) {
                userResponse.add(user);
            }
        });
                        return userResponse;
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
                    .capacity(roomRequest.capacity)
                    .chief(user)
                    .build();
        return roomMapper.toRoomDTO(roomRepository.save(room));
    }

    public List<RoomDTO> getAll(){

        List<Room> rooms = roomRepository.findAll();

        return rooms.stream()
                .map(roomMapper::toRoomDTO)
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
        Optional.of(roomUpdate.capacity).ifPresent(room::setCapacity);

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

    public RoomDTO deleteRoom(String roomId){
        RoomDTO room = getRoomById(roomId);
        if(room == null) throw new RuntimeException("room Not Found");
        roomRepository.deleteById(roomId);
        return room;
    }

    public Page<RoomDTO> getRoomsWithPaginationAndFilter(int page, int size,String hallId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Room> rooms =  roomRepository.findRoomsWithFilters( hallId, pageable);
        return  rooms.map(roomMapper::toRoomDTO);
    }

    public String saveDiagramRoom(String roomId) throws IOException, BadRequest {
        Room room = getRoomByIdDefault(roomId);
        DiagramDraft diagramDraft = this.getDiagramById(roomId);
        List<SeatDiagramUpdate> seats = diagramDraft.getSeats();

        seats.forEach(seat -> {
            Seat seat1 = seatRepository.findById(seat.seatId).orElse(null);
            if(seat1 == null) throw new RuntimeException("Seat Not Found");
            seat1.posX = seat.posX;
            seat1.posY = seat.posY;
            seatRepository.save(seat1);
        });
        room.setImage(diagramDraft.getImage());
        room.object = diagramDraft.object;
        roomRepository.save(room);
        webSocketService.sendToRole("SUPERUSER","diagram", diagramDraft);
        webSocketService.sendNoticeToRoom(room.getId(),"Layout Room " + room.name + " has been updated");
        return  "save diagram";
    }

    @SneakyThrows
    public void saveDiagramDraft(SaveDiagram saveDiagram) {
        Room room = getRoomByIdDefault(saveDiagram.roomId);
        List<SeatDiagramUpdate> seats = objectMapper.readValue(saveDiagram.seats,
                objectMapper.getTypeFactory().constructCollectionType(List.class, SeatDiagramUpdate.class));
                        DiagramDraft object = new DiagramDraft();
        String url = Optional.ofNullable(saveDiagram.getImage())
                .map(image -> {
                    try {
                        return cloudinaryService.uploadImage(image);
                    } catch (IOException e) {
                        return null;
                    }
                })
                .orElse(null);

                object.setImage(url);
                object.setSeats(seats);
                object.setObject(saveDiagram.object);
                object.setRoomId(saveDiagram.roomId);
                object.setDraft(true);
                redisService.setValue(saveDiagram.roomId, object);

                webSocketService.sendToRole("SUPERUSER","diagram", object);
    }

    public void deleteDiagramDraft(String roomId){
        redisService.deleteValueByKey("room");
    }
    public DiagramDraft getDiagramById(String roomId){
       return objectMapper.convertValue( redisService.getValueByKey(roomId),DiagramDraft.class);
    }
}

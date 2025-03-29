package com.seatmanage.services;

import com.cloudinary.api.exceptions.BadRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatmanage.config.SecurityUtil;
import com.seatmanage.dto.request.*;
import com.seatmanage.dto.response.RoomDTO;
import com.seatmanage.dto.response.SeatDTO;
import com.seatmanage.dto.response.UserDTO;
import com.seatmanage.entities.*;
import com.seatmanage.mappers.DiagramMapper;
import com.seatmanage.mappers.RoomMapper;
import com.seatmanage.repositories.RoomRepository;
import com.seatmanage.repositories.SeatRepository;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
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
    private final DiagramMapper diagramMapper;

    public RoomService(RoomRepository roomRepository, HallService hallService, RoomMapper roomMapper,
                       UserService userService, SeatRepository seatRepository, WebSocketService webSocketService,
                       CloudinaryService cloudinaryService, CloudinaryService cloudinaryService1,
                       ObjectMapper objectMapper, RedisService redisService, DiagramMapper diagramMapper) {
        this.roomRepository = roomRepository;
        this.hallService = hallService;
        this.roomMapper = roomMapper;
        this.userService = userService;
        this.seatRepository = seatRepository;
        this.webSocketService = webSocketService;
        this.cloudinaryService = cloudinaryService1;
        this.objectMapper = objectMapper;
        this.redisService = redisService;
        this.diagramMapper = diagramMapper;
    }

    public RoomDTO getRoomById(String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        return roomMapper.toRoomDTO(room);
    }

    public Room getRoomByIdDefault(String roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public List<UserDTO> getUserInRoom(String roomId) {
        List<UserDTO> users = userService.getUsersInRoom(roomId);
        List<String> userIds = seatRepository.getUserAssignByRoomId(roomId);
        return users.stream().filter(user -> !userIds.contains(user.id)).collect(Collectors.toList());
    }

    public RoomDTO createRoom(RoomRequest roomRequest) {
        Hall hall = Optional.ofNullable(roomRequest.hallId).map(hallService::getHallByIdDefault)
                .orElseThrow(() -> new RuntimeException("Hall not found"));
        User user = Optional.ofNullable(roomRequest.userId).map(userService::getUserById).orElse(null);
        if (roomRepository.findRoomByNamAndHallId(roomRequest.name, roomRequest.hallId).isPresent())
            throw new RuntimeException("Room already exist");
        Room room = Room.builder().name(roomRequest.name).description(roomRequest.description).hall(hall)
                .capacity(roomRequest.capacity).chief(user).build();
        return roomMapper.toRoomDTO(roomRepository.save(room));
    }

    public List<RoomDTO> getAll() {
        return roomRepository.findAll().stream().map(roomMapper::toRoomDTO).collect(Collectors.toList());
    }

    public RoomDTO getRoomSeatByChief(String roomId) {
        Room roomEx = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        if (!SecurityUtil.isPrivate(roomEx.chief != null, roomEx.chief)) throw new RuntimeException("Forbidden !!!");
        return roomMapper.toRoomDTO(roomEx);
    }

    public RoomDTO updateRoom(String roomId, RoomRequest roomUpdate) {
        Room room = getRoomByIdDefault(roomId);
        User user = Optional.ofNullable(roomUpdate.userId).map(userService::getUserById)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional.ofNullable(roomUpdate.getName()).ifPresent(room::setName);
        Optional.ofNullable(roomUpdate.getDescription()).ifPresent(room::setDescription);
        Optional.ofNullable(roomUpdate.hallId)
                .ifPresent(hallService::getHallByIdDefault);
        User chief = Optional.ofNullable(roomUpdate.userId).map(userService::getUserById)
                .orElse(null);
        room.setChief(chief);
        return roomMapper.toRoomDTO(roomRepository.save(room));
    }

    public RoomDTO deleteRoom(String roomId) {
        RoomDTO room = getRoomById(roomId);
        roomRepository.deleteById(roomId);
        return room;
    }

    public Page<RoomDTO> getRoomsWithPaginationAndFilter(int page, int size, String hallId) {
        Pageable pageable = PageRequest.of(page, size);
        return roomRepository.findRoomsWithFilters(hallId, pageable).map(roomMapper::toRoomDTO);
    }

    public String approvingDiagram(String roomId) throws IOException, BadRequest {
        Room room = getRoomByIdDefault(roomId);
        DiagramDraft diagramDraft = getDiagramById(roomId);
        diagramDraft.getSeats().forEach(seat -> {
            Seat seat1 = seatRepository.findById(seat.id).orElseThrow(() -> new RuntimeException("Seat Not Found"));
            seat1.posX = seat.posX;
            seat1.posY = seat.posY;
            seatRepository.save(seat1);
        });
        room.setImage(diagramDraft.getImage());
        room.object = diagramDraft.object;
        roomRepository.save(room);
        deleteDiagramDraft(roomId);
        webSocketService.sendNoticeToRoom(room.getId(), "approve","your request change layout is approved");
        return "save diagram";
    }

    public String rejectingDiagram(String roomId) throws BadRequest, IOException {
        Room room = getRoomByIdDefault(roomId);
        DiagramDraft diagramDraft = getDiagramById(roomId);
        deleteDiagramDraft(roomId);
        webSocketService.sendNoticeToRoom(roomId,"reject","your request change layout is rejected");
        return "rejecting diagram";
    }
    @SneakyThrows
    public void saveDiagramDraft(Room room, SaveDiagram saveDiagram) {
        String url = Optional.ofNullable(saveDiagram.getImage()).map(image -> {
            try {
                return cloudinaryService.uploadImage(image);
            } catch (IOException e) {
                return null;
            }
        }).orElse(room.image);
        DiagramDraft object = diagramMapper.toDiagramDraft(saveDiagram);
        object.setImage(url);
        object.setSeats(Arrays.asList(objectMapper.readValue(saveDiagram.seats, SeatDiagramUpdate[].class)));
        object.setDraft(true);
        redisService.setValue(saveDiagram.roomId, object);
        System.out.println("save diagram and send message to room");
        webSocketService.sendWithRole("SUPERUSER", "requestDiagram", saveDiagram.roomId);
    }

    @SneakyThrows
    public String saveDiagram(SaveDiagram saveDiagram) {
        Room room = getRoomByIdDefault(saveDiagram.roomId);
        if(SecurityUtil.isSupperUser()){
            System.out.println("run at here ::: superuser");
            String url = Optional.ofNullable(saveDiagram.getImage()).map(image -> {
                try {
                    return cloudinaryService.uploadImage(image);
                } catch (IOException e) {
                    return null;
                }
            }).orElse(null);
            List<SeatDiagramUpdate> seatDiagramUpdates = Arrays.asList(objectMapper.readValue(saveDiagram.seats, SeatDiagramUpdate[].class));

            seatDiagramUpdates.forEach(seat -> {
                Seat seat1 = seatRepository.findById(seat.id).orElseThrow(() -> new RuntimeException("Seat Not Found"));
                seat1.posX = seat.posX;
                seat1.posY = seat.posY;
                seatRepository.save(seat1);
            });

            System.out.println("save diagram and send message to room" + url);
            room.setImage(url);
            room.setObject(saveDiagram.object);
            roomRepository.save(room);
            webSocketService.sendNoticeToRoom(room.getId(), "saveDiagram","layout has changed");
            return  "Save diagram successfully ";
        }else {
            saveDiagramDraft(room,saveDiagram);
            return  "send request change diagram successfully ";
        }
    }

        public void deleteDiagramDraft(String roomId) {
        redisService.deleteValueByKey(roomId);
    }

    public DiagramDraft getDiagramById(String roomId) {
        return objectMapper.convertValue(redisService.getValueByKey(roomId), DiagramDraft.class);
    }
}

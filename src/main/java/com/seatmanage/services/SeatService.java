package com.seatmanage.services;
import com.cloudinary.api.exceptions.BadRequest;
import com.seatmanage.config.SecurityUtil;
import com.seatmanage.dto.request.AssignSeatRequest;
import com.seatmanage.dto.request.ReassignSeatRequest;
import com.seatmanage.dto.request.SeatRequest;
import com.seatmanage.dto.request.SetupSeatRequest;
import com.seatmanage.dto.response.ReAssignSeatDTO;
import com.seatmanage.dto.response.SeatDTO;
import com.seatmanage.dto.response.UserDTO;
import com.seatmanage.entities.Room;
import com.seatmanage.entities.Seat;
import com.seatmanage.entities.User;
import com.seatmanage.mappers.SeatMapper;
import com.seatmanage.mappers.UserMapper;
import com.seatmanage.repositories.SeatRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PUBLIC)
public class SeatService {
    private final SeatRepository seatRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final SeatMapper seatMapper;
    private final UserMapper userMapper;
    private final WebSocketService webSocketService;

    public SeatService(SeatRepository seatRepository, UserService userService, RoomService roomService,
                       SeatMapper seatMapper, UserMapper userMapper, WebSocketService webSocketService) {
        this.seatRepository = seatRepository;
        this.userService = userService;
        this.roomService = roomService;
        this.seatMapper = seatMapper;
        this.userMapper = userMapper;

        this.webSocketService = webSocketService;
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

    public SeatDTO createSeat(SeatRequest seatRequest) throws IOException, BadRequest {

//      Check room existed
        Room room = roomService.getRoomByIdDefault(seatRequest.roomId);
//      Check user is chief
        boolean isPrivate = SecurityUtil.isPrivate(room.getChief() != null,room.getChief());
        if(!isPrivate) throw  new RuntimeException("Not permission to create seat of this room ! !!!");
//      Check Seat have in room ?
        seatRepository.findSeatByNamAndRoomId(seatRequest.name,room.getId())
                .ifPresent(roomExisted -> {
                    throw new RuntimeException("Seat already exist");
                });
        System.out.println("run at here 3");
//      If request have userId -> Check user existed?
        User user = Optional.ofNullable(seatRequest.userId)
                    .map(userId -> Optional.ofNullable(userService.getUserById(userId))
                           .orElseThrow(()-> new RuntimeException("User not found!")))
                    .orElse(null);

        System.out.println("run at here 4");
//        Check user have seat
        Seat userHasSeat = Optional.ofNullable(user)
                .flatMap(userEx -> seatRepository.findSeatByUserId(userEx.getId()))
                .orElse(null);
        Optional.ofNullable(userHasSeat).ifPresent(seat -> {
            throw new RuntimeException("User have a seat");
        });
        boolean isOccupied = user != null;
        Seat seat = Seat.builder().name(seatRequest.name)
                    .description(seatRequest.description)
                    .typeSeat(seatRequest.typeSeat != null ? Seat.TypeSeat.valueOf(seatRequest.typeSeat) : null)
                    .room(room)
                    .user(user)
                .isOccupied(isOccupied)
                    .build();
        SeatDTO seatDTO =  seatMapper.toSeatDTO(seatRepository.save(seat));
        webSocketService.sendSeatAction(seat.room.getId(),"createSeat", seatDTO);
        return seatDTO;

    }
    public Page<SeatDTO> getSeatsWithPaginationAndFilter(int page, int size,  String roomId, String typeSeat, Boolean isOccupied) {
        Pageable pageable =  PageRequest.of(page, size);
        System.out.println("check room id ::: " + roomId);
        System.out.println("check type seat ::: " + typeSeat);
        Page<Seat> seatPage = seatRepository.findSeatsWithFilters(roomId, Seat.TypeSeat.valueOf(typeSeat), isOccupied, pageable);
        return seatPage.map(seatMapper::toSeatDTO);
    }
    public List<SeatDTO> getAll(){
        List<Seat> seats = seatRepository.findAll();
        return seats.stream()
                .map(seatMapper::toSeatDTO)
                .collect(Collectors.toList());
    }

    public SeatDTO updateSeat(String seatId,SeatRequest seatRequest) throws IOException, BadRequest {
        Seat seat = getSeatByIdDefault(seatId);
        boolean isPrivate = SecurityUtil.isPrivate(seat.getRoom().getChief() != null,seat.getRoom().getChief());
        if(!isPrivate) throw  new RuntimeException("Not permission to update seat of this room !");
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
        SeatDTO seatDTO = seatMapper.toSeatDTO(seatRepository.save(seat));
        Set<String> userSelect = new HashSet<>();
        webSocketService.sendSeatUpdate(seat.room.getId(),seatDTO);
        webSocketService.sendNotice("A Seat " + seat.getName() +  " updated  at room " + seat.room.getName());
        ;

        return seatDTO;
    }

    public SeatDTO deleteSeat(String seatId) throws IOException, BadRequest {
        Seat seat = getSeatByIdDefault(seatId);
        boolean isPrivate = SecurityUtil.isPrivate(seat.getRoom().getChief() != null,seat.getRoom().getChief());
        if(!isPrivate) throw  new RuntimeException("Not permission to delete seat of this room !");
        seatRepository.deleteById(seatId);
       SeatDTO seatDTO =  seatMapper.toSeatDTO(seat);
        webSocketService.sendSeatAction(seat.room.getId(),"deleteSeat", seatDTO );
        return seatDTO;
    }

    public List<SeatDTO> getSeatOccupantByRoomId(String roomId){
        return seatRepository.findSeatOccupantByRoomId(roomId).stream().map(seatMapper::toSeatDTO).collect(Collectors.toList());
    }

    public UserDTO getUserBySeat(String seatId){
        Seat seat = getSeatByIdDefault(seatId);
        return userMapper.toUserDTO(seat.getUser());
    }

    public List<SeatDTO> getSeatByRoomId(String roomId){
        Room room = roomService.getRoomByIdDefault(roomId);
        boolean isPrivate = SecurityUtil.isPrivate(room.getChief() != null,room.getChief());
        if(!isPrivate) throw  new RuntimeException("Not permission to get seat of this room !");
        return seatRepository.findSeatByRoomId(roomId).stream().map(seatMapper::toSeatDTO).collect(Collectors.toList());
    }

    public SeatDTO assignSeat(AssignSeatRequest assignSeatRequest) throws BadRequest {
        Seat seat = getSeatByIdDefault(assignSeatRequest.seatId);
        User user = userService.getUserById(assignSeatRequest.userId);
        Optional<User> getSeatByUserId = seatRepository.findUserBySeat(seat.getId());
        if(getSeatByUserId.isPresent()) throw  new RuntimeException("User already assigned to this seat !");
        if(seat.getUser() != null) throw  new RuntimeException("Seat has already been assigned to this seat !");
        seat.setUser(user);
        seat.isOccupied = true;
        seatRepository.save(seat);
        SeatDTO seatDTO = seatMapper.toSeatDTO(seat);
        webSocketService.sendToRoom(seat.room.getId(),"assignSeat", seatDTO);
        return seatDTO;
    }
    public SeatDTO reassignSeat(ReassignSeatRequest reassignSeatRequest) throws BadRequest {
        Seat seatNew = getSeatByIdDefault(reassignSeatRequest.newSeatId);
        Seat seatOld = getSeatByIdDefault(reassignSeatRequest.oldSeatId);
        User user = userService.getUserById(reassignSeatRequest.userId);
        if(!Objects.equals(seatOld.getUser().getId(), reassignSeatRequest.userId)) throw  new RuntimeException("this is not user's seat !");
        if(seatNew.getUser() != null) throw  new RuntimeException("User already assigned to this seat !");
        seatOld.setUser(null);
        seatOld.setIsOccupied(false);
        seatRepository.save(seatOld);
        seatNew.setUser(user);
        seatNew.setIsOccupied(true);
        seatRepository.save(seatNew);
        SeatDTO seatDTO = seatMapper.toSeatDTO(seatNew);
        ReAssignSeatDTO reassignSeatDTO = new ReAssignSeatDTO();
        reassignSeatDTO.setOldSeat(seatMapper.toSeatDTO(seatOld));
        reassignSeatDTO.setNewSeat(seatMapper.toSeatDTO(seatNew));
        webSocketService.sendReAssignSeat(seatNew.room.getId(),reassignSeatDTO);
        return seatDTO;
    }

    public SeatDTO unAssign(String seatId) throws IOException, BadRequest {
        Seat seat = getSeatByIdDefault(seatId);
        if(seat.user == null)  throw  new RuntimeException("Seat hasn't assigned !");
        seat.setUser(null);
        seat.isOccupied = false;
        seatRepository.save(seat);
        SeatDTO seatDTO = seatMapper.toSeatDTO(seat);
        webSocketService.sendSeatAction(seat.room.getId(),"unAssignSeat", seatDTO);
        return seatDTO;
    }

    public SeatDTO setupTypeSeat(String seatId, SetupSeatRequest typeSeat){
        if(typeSeat.getTypeSeat() == Seat.TypeSeat.TEMPORARY && typeSeat.expiration == null) throw  new RuntimeException("Type Seat Temporary required a expiration !");
        Seat seat = getSeatByIdDefault(seatId);
        boolean isPrivate = SecurityUtil.isPrivate(seat.getRoom().getChief() != null,seat.getRoom().getChief());
        LocalDateTime expiration = typeSeat.typeSeat == Seat.TypeSeat.PERMANENT ? null : typeSeat.expiration;
        if(!isPrivate) throw  new RuntimeException("Not permission to update seat of this room !");
        seat.setTypeSeat(typeSeat.getTypeSeat());
        seat.setExpiration(expiration);
        seatRepository.save(seat);
        return seatMapper.toSeatDTO(seat);
    }
}

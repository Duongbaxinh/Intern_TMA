package com.seatmanage.controllers;

import com.cloudinary.api.exceptions.BadRequest;
import com.seatmanage.dto.request.RoomRequest;
import com.seatmanage.dto.request.SaveDiagram;
import com.seatmanage.dto.request.SeatDiagramUpdate;
import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.dto.response.RoomDTO;
import com.seatmanage.dto.response.SeatDTO;
import com.seatmanage.services.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/room")

public class RoomController {
    final
    RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    ApiResponse<Object> createRoom(@RequestBody @Valid RoomRequest roomRequest) {
        return ApiResponse.builder()
                .code(200)
                .msg("create hall successfully")
                .result(roomService.createRoom(roomRequest))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    ApiResponse<Object> getRoomList() {
        return ApiResponse.builder().code(200).msg("get all room").result(roomService.getAll()).build();
    }

    @GetMapping("/users/{roomId}")
//    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    ApiResponse<Object> getUserInRoom(@PathVariable String roomId) {
        return ApiResponse.builder().code(200).msg("get user in room").result(roomService.getUserInRoom(roomId)).build();
    }

    @GetMapping("/chief/{id}")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    ApiResponse<Object> getRoomSeatByChief( @PathVariable String id) {
        return ApiResponse.builder().code(200).msg("get all room by chief")
                .result(roomService.getRoomSeatByChief(id)).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER','ROLE_LANDLORD')")
    ApiResponse<Object> getRoomById(@PathVariable String id) {
        return ApiResponse.builder().code(200)
                .msg("get room by id: " + id)
                .result(roomService.getRoomById(id)).build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    ApiResponse<Object> updateRoomById(@PathVariable String id, @RequestBody RoomRequest roomRequest){
        System.out.println("run updateHallById" + roomRequest.getName());
        return  ApiResponse.builder().code(200)
                .msg("update hall with id " + id  +" successfully")
                .result(roomService.updateRoom(id, roomRequest)).build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    ApiResponse<Object> deleteRoomById(@PathVariable String id){
        return  ApiResponse.builder().code(200)
                .msg("delete hall with id " +  id  +" successfully")
                .result(roomService.deleteRoom(id)).build();
    }

    @GetMapping("/filter")
    public ApiResponse<Object> getSeats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String hallId) {
        Page<RoomDTO> rooms = roomService.getRoomsWithPaginationAndFilter(page,size,hallId);
        return  ApiResponse.builder().code(200)
                .msg("get room successfully")
                .result(rooms).build();
    }
    @PostMapping(value = "/diagram",consumes = "multipart/form-data")
    public  ApiResponse<Object> createDiagramRoom( @ModelAttribute SaveDiagram saveDiagram) throws IOException, BadRequest {
        return ApiResponse.builder().code(200)
                .msg("save diagram successfully ")
                .result(roomService.saveDiagramRoom(saveDiagram)).build();
    }

    @GetMapping("/view/{roomId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER','ROLE_LANDLORD','USER')")
    public ApiResponse<Object> getRoomView( @PathVariable String roomId) {
        return ApiResponse.builder().code(200)
                .msg("get room view successfully ")
                .result(roomService.getRoomById(roomId)).build();
    }



}

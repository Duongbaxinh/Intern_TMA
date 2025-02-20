package com.seatmanage.controllers;

import com.seatmanage.dto.request.RoomRequest;
import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.services.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
@PreAuthorize("hasRole('ROLE_SUPERUSER')")
public class RoomController {
    final
    RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    ApiResponse<Object> createRoom(@RequestBody @Valid RoomRequest roomRequest) {
        return ApiResponse.builder()
                .code(200)
                .msg("create hall successfully")
                .result(roomService.createRoom(roomRequest))
                .build();
    }

    @GetMapping
    ApiResponse<Object> getRoomList() {
        return ApiResponse.builder().code(200).msg("get all room").result(roomService.getAll()).build();
    }

    @GetMapping("/chief/{id}")
    ApiResponse<Object> getRoomSeatByChief( @PathVariable String id) {
        return ApiResponse.builder().code(200).msg("get all room by chief")
                .result(roomService.getRoomSeatByChief(id)).build();
    }

    @GetMapping("{id}")
    ApiResponse<Object> getRoomById(@PathVariable String id) {
        return ApiResponse.builder().code(200)
                .msg("get room by id: " + id)
                .result(roomService.getRoomById(id)).build();
    }

    @PutMapping("{id}")
    ApiResponse<Object> updateRoomById(@PathVariable String id, @RequestBody RoomRequest roomRequest){
        System.out.println("run updateHallById" + roomRequest.getName());
        return  ApiResponse.builder().code(200)
                .msg("update hall with id " + id  +" successfully")
                .result(roomService.updateRoom(id, roomRequest)).build();
    }

    @DeleteMapping("{id}")
    ApiResponse<Object> deleteRoomById(@PathVariable String id){
        return  ApiResponse.builder().code(200)
                .msg("delete hall with id " +  id  +" successfully")
                .result(roomService.deleteRoom(id)).build();
    }

}

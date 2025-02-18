package com.seatmanage.controllers;

import com.seatmanage.dto.request.RoomRequest;
import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.services.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    RoomService roomService;

    @PostMapping
    ApiResponse<Object> createHall(@RequestBody @Valid RoomRequest roomRequest) {
        return ApiResponse.builder()
                .code(200)
                .msg("create hall successfully")
                .result(roomService.createRoom(roomRequest))
                .build();
    }

    @GetMapping
    ApiResponse<Object> getHallList() {
        return ApiResponse.builder().code(200).msg("get all room").result(roomService.getAll()).build();
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

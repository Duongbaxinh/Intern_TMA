package com.seatmanage.controllers;

import com.seatmanage.dto.request.SeatRequest;
import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.services.SeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seat")
public class SeatController {
    @Autowired
    private SeatService seatService;

    @PostMapping
    ApiResponse<Object> createSeat(@RequestBody @Valid SeatRequest seatRequest) {
        return ApiResponse.builder()
                .code(200)
                .msg("create hall successfully")
                .result(seatService.createSeat(seatRequest))
                .build();
    }

    @GetMapping
    ApiResponse<Object> getHallList() {
        return ApiResponse.builder().code(200).msg("get all seat").result(seatService.getAll()).build();
    }

    @GetMapping("{id}")
    ApiResponse<Object> getRoomById(@PathVariable String id) {
        return ApiResponse.builder().code(200)
                .msg("get seat by id: " + id)
                .result(seatService.getSeatById(id)).build();
    }

    @PutMapping("{id}")
    ApiResponse<Object> updateRoomById(@PathVariable String id, @RequestBody SeatRequest seatRequest){
        System.out.println("run updateHallById" + seatRequest.getName());
        return  ApiResponse.builder().code(200)
                .msg("update hall with id " + id  +" successfully")
                .result(seatService.updateSeat(id,seatRequest)).build();
    }

    @DeleteMapping("{id}")
    ApiResponse<Object> deleteRoomById(@PathVariable String id){
        return  ApiResponse.builder().code(200)
                .msg("delete hall with id " +  id  +" successfully")
                .result(seatService.deleteSeat(id)).build();
    }

}

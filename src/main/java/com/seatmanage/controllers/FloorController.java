package com.seatmanage.controllers;

import com.seatmanage.dto.request.FloorCreationRequest;
import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.services.FloorService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/floor")
public class FloorController {
    @Autowired
    FloorService floorService;

    @PostMapping
    ApiResponse<Object> createFloor(@RequestBody @Valid FloorCreationRequest floorRequestCreation) {
        return ApiResponse.builder()
                .code(200)
                .msg("create floor successfully")
                .result(floorService.createFloor(floorRequestCreation))
                .build();
    }

    @GetMapping
    ApiResponse<Object> getFloorList() {
        return ApiResponse.builder().code(200).msg("get all floors").result(floorService.getAll()).build();
    }

    @GetMapping(path = ":id")
    ApiResponse<Object> getFloorById(@PathVariable("id") String id) {
        return ApiResponse.builder().code(200).msg("get floor by id: " + id).result(floorService.getFloorById(id)).build();
    }

    @PutMapping(path = ":id")
    ApiResponse<Object> updateFloorById(@PathVariable("id") String id, FloorCreationRequest floorCreationRequest){
        return  ApiResponse.builder().code(200)
                .msg("update floor with id " + id  +" successfully")
                .result(floorService.updateFloor(id,floorCreationRequest)).build();
    }

    @DeleteMapping(path = "id")
    ApiResponse<Object> deleteFloorById(@PathVariable("id") String id){
        return  ApiResponse.builder().code(200)
                .msg("delete floor with id " + id  +" successfully")
                .result(floorService.deleteFloor(id)).build();
    }

}

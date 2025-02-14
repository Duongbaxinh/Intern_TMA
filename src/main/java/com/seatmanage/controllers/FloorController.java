package com.seatmanage.controllers;

import com.seatmanage.dto.request.FloorRequest;
import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.services.FloorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/floor")
public class FloorController {
    @Autowired
    FloorService floorService;

    @PostMapping
    ApiResponse<Object> createFloor(@RequestBody @Valid FloorRequest floorRequestCreation) {
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

    @GetMapping("{id}")
    ApiResponse<Object> getFloorById(@PathVariable String id) {
        return ApiResponse.builder().code(200).msg("get floor by id: " + id).result(floorService.getFloorById(id)).build();
    }

    @PutMapping("{id}")
    ApiResponse<Object> updateFloorById(@PathVariable String id, @RequestBody FloorRequest floorRequest){
        System.out.println("run updateFloorById" + floorRequest.getName());
        return  ApiResponse.builder().code(200)
                .msg("update floor with id " + id  +" successfully")
                .result(floorService.updateFloor(id, floorRequest)).build();
    }

    @DeleteMapping("{id}")
    ApiResponse<Object> deleteFloorById(@PathVariable String id){
        return  ApiResponse.builder().code(200)
                .msg("delete floor with id " +  id  +" successfully")
                .result(floorService.deleteFloor(id)).build();
    }

}

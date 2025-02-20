package com.seatmanage.controllers;

import com.seatmanage.dto.request.HallRequest;
import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.services.HallService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hall")
@PreAuthorize("hasRole('ROLE_SUPERUSER')")
public class HallController {
    @Autowired
    HallService hallService;

    @PostMapping
    ApiResponse<Object> createHall(@RequestBody @Valid HallRequest hallRequest) {
        return ApiResponse.builder()
                .code(200)
                .msg("create hall successfully")
                .result(hallService.createHall(hallRequest))
                .build();
    }

    @GetMapping
    ApiResponse<Object> getHallList() {
        return ApiResponse.builder().code(200).msg("get all halls").result(hallService.getAll()).build();
    }

    @GetMapping("{id}")
    ApiResponse<Object> getHallById(@PathVariable String id) {
        return ApiResponse.builder().code(200)
                .msg("get hall by id: " + id)
                .result(hallService.getHallById(id)).build();
    }

    @PutMapping("{id}")
    ApiResponse<Object> updateHallById(@PathVariable String id, @RequestBody HallRequest hallRequest){
        return  ApiResponse.builder().code(200)
                .msg("update hall with id " + id  +" successfully")
                .result(hallService.updateHall(id, hallRequest)).build();
    }

    @DeleteMapping("{id}")
    ApiResponse<Object> deleteHallById(@PathVariable String id){
        return  ApiResponse.builder().code(200)
                .msg("delete hall with id " +  id  +" successfully")
                .result(hallService.deleteHall(id)).build();
    }

}

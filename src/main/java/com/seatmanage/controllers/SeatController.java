package com.seatmanage.controllers;

import com.seatmanage.dto.request.AssignSeatRequest;
import com.seatmanage.dto.request.ReassignSeatRequest;
import com.seatmanage.dto.request.SeatRequest;
import com.seatmanage.dto.request.SetupSeatRequest;
import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.dto.response.SeatDTO;
import com.seatmanage.entities.Seat;
import com.seatmanage.services.SeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/seat")

public class SeatController {
    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }


    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_SUPERUSER','ROLE_LANDLORD')")
    ApiResponse<Object> createSeat(@RequestBody @Valid SeatRequest seatRequest) throws IOException {
        System.out.println("run at " + seatRequest.toString());
        return ApiResponse.builder()
                .code(200)
                .msg("create hall successfully")
                .result(seatService.createSeat(seatRequest))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_SUPERUSER','ROLE_LANDLORD')")
    ApiResponse<Object> getSeatList() {

        return ApiResponse.builder().code(200).msg("get all seat").result(seatService.getAll()).build();
    }

    @GetMapping("{id}")
    ApiResponse<Object> getSeatId(@PathVariable String id) {
        return ApiResponse.builder().code(200)
                .msg("get seat by id: " + id)
                .result(seatService.getSeatById(id)).build();
    }

    @GetMapping("/unassign/{seatId}")
    ApiResponse<Object> unAssignSeat(@PathVariable String seatId) throws IOException {
        return ApiResponse.builder().code(200)
                .msg("unassign seat by id: " + seatId)
                .result(seatService.unAssign(seatId)).build();
    }


    @GetMapping("/occupant/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPERUSER','ROLE_LANDLORD')")
    ApiResponse<Object> getOccupant(@PathVariable String id) {
        return  ApiResponse.builder()
                .code(200)
                .msg("seat occupant")
                .result(seatService.getSeatOccupantByRoomId(id))
                .build();
    }

    @GetMapping("/{seatId}/user")
    ApiResponse<Object> getUserSeat(@PathVariable String seatId) {
        return ApiResponse.builder()
                .code(200)
                .msg("get user with seat")
                .result(seatService.getUserBySeat(seatId))
                .build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPERUSER','ROLE_LANDLORD')")
    ApiResponse<Object> updateSeatId(@PathVariable String id, @RequestBody SeatRequest seatRequest) throws IOException {
        System.out.println("run updateHallById" + seatRequest.getName());
        return  ApiResponse.builder().code(200)
                .msg("update hall with id " + id  +" successfully")
                .result(seatService.updateSeat(id,seatRequest)).build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPERUSER','ROLE_LANDLORD')")
    ApiResponse<Object> deleteSeatById(@PathVariable String id) throws IOException {
        return  ApiResponse.builder().code(200)
                .msg("delete seat with id " +  id  +" successfully")
                .result(seatService.deleteSeat(id)).build();
    }

    @GetMapping("/{roomId}/seat")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPERUSER','ROLE_LANDLORD')")
    ApiResponse<Object> getSeatByRoomId(@PathVariable String roomId) {
     return   ApiResponse.builder().code(200)
                .msg("get seat by roomId")
                .result(seatService.getSeatByRoomId(roomId))
                .build();
    }

    @PutMapping("/{seatId}/setup-type")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPERUSER','ROLE_LANDLORD')")
    ApiResponse<Object> setupTypeSeat(@PathVariable String seatId, @RequestBody SetupSeatRequest setupSeatRequest) {
        System.out.println("run at check type seat " + Seat.TypeSeat.valueOf("TEMPORARY"));
        return   ApiResponse.builder().code(200)
                .msg("set up type seat successfully")
                .result(seatService.setupTypeSeat(seatId,setupSeatRequest))
                .build();
    }

    @PutMapping("/assign")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPERUSER','ROLE_LANDLORD')")
    ApiResponse<Object> assignSeat( @RequestBody AssignSeatRequest assignSeatRequest) {
        return   ApiResponse.builder().code(200)
                .msg("assign seat successfully")
                .result(seatService.assignSeat(assignSeatRequest))
                .build();
    }
    @PutMapping("/reassign")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPERUSER','ROLE_LANDLORD')")
    ApiResponse<Object> reassignSeat( @RequestBody ReassignSeatRequest reassignSeatRequest) {
        return ApiResponse.builder().code(200)
                .msg("reassign seat successfully")
                .result(seatService.reassignSeat(reassignSeatRequest))
                .build();
    }

    @GetMapping("/filter")
    public ApiResponse<Object> getSeats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "TEMPORARY") String typeSeat,
            @RequestParam(required = false) String roomId,
            @RequestParam(required = false) Boolean isOccupied) {
        Page<SeatDTO> seats = seatService.getSeatsWithPaginationAndFilter(page, size,  roomId, typeSeat, isOccupied);
        return  ApiResponse.builder().code(200)
                .msg("get seat successfully")
                .result(seats).build();
    }


}

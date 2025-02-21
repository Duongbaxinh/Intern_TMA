package com.seatmanage.controllers;


import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.entities.Team;
import com.seatmanage.services.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ApiResponse<Object> createTeam(@RequestBody Team team) {
        return  ApiResponse.builder()
                .code(200)
                .msg("create team successfully")
                .result(teamService.createTeam(team))
                .build();
    }

    @GetMapping
    public ApiResponse<Object> getAllTeam() {
        return ApiResponse.builder()
                .code(200)
                .msg("get all team successfully")
                .result(teamService.getAllTeam())
                .build();
    }
}

package com.seatmanage.services;

import com.seatmanage.entities.Team;
import com.seatmanage.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team createTeam(Team team){
    return teamRepository.save(team);

    }

    public  Team getTeamById(String id){
        return teamRepository.findById(id).orElseThrow(() -> new RuntimeException("team not found"));

    }

    public List<Team> getAllTeam(){
        return teamRepository.findAll();
    }
}

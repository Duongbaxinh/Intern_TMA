package com.seatmanage.services;

import com.seatmanage.entities.Project;
import com.seatmanage.entities.Team;
import com.seatmanage.repositories.ProjectRepository;
import com.seatmanage.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    public TeamService(TeamRepository teamRepository, ProjectRepository projectRepository, ProjectService projectService) {
        this.teamRepository = teamRepository;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
    }

    public Team createTeam(Team team){
        Project project = Optional.ofNullable(team.getProject()).map(project1 -> projectService.getProjectById(project1.getId())).orElse(null);
        team.setProject(project);
    return teamRepository.save(team);

    }

    public  Team getTeamById(String id){
        return teamRepository.findById(id).orElseThrow(() -> new RuntimeException("team not found"));

    }

    public List<Team> getAllTeam(){
        return teamRepository.findAll();
    }
}

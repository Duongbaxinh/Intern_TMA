package com.seatmanage.services;

import com.seatmanage.entities.Project;
import com.seatmanage.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project createProject(Project project){
        return projectRepository.save(project);
    }

    public  Project getProjectById(String id){
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("project not found"));

    }

    public List<Project> getAllProject(){
        return projectRepository.findAll();
    }
}

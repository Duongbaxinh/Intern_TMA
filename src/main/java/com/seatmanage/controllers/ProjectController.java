package com.seatmanage.controllers;


import com.seatmanage.dto.response.ApiResponse;
import com.seatmanage.entities.Project;
import com.seatmanage.services.ProjectService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ApiResponse<Object> createProject(@RequestBody Project project) {
        return  ApiResponse.builder()
                .code(200)
                .msg("create project successfully")
                .result(projectService.createProject(project))
                .build();
    }

    @GetMapping
    public ApiResponse<Object> getAllProject() {
        return ApiResponse.builder()
                .code(200)
                .msg("get all project successfully")
                .result(projectService.getAllProject())
                .build();
    }
}

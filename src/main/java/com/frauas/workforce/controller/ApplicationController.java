package com.frauas.workforce.controller;

import com.frauas.workforce.DTO.ApplicationResponseDTO;
import com.frauas.workforce.DTO.ApplyRequest;
import com.frauas.workforce.DTO.SuggestProjectRequest;
import com.frauas.workforce.model.Application;
import com.frauas.workforce.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    public ApplicationService applicationService;

//    public ApplicationController(ApplicationService applicationService) {
//        this.applicationService = applicationService;
//    }

    @PostMapping("/suggest")
    public ResponseEntity<ApplicationResponseDTO> suggestProjectToEmployee(
            @RequestBody SuggestProjectRequest request
    ) {
        ApplicationResponseDTO response =
                applicationService.suggestProjectToEmployee(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/suggested-projects/{employeeId}")
    public List<Application> getSuggestedProjects(
            @PathVariable Integer employeeId
    ) {
        return applicationService.getSuggestedProjectsForEmployee(employeeId);
    }

    @PostMapping("/apply/suggestedProjects")
    public Application applyToSuggestedProject(@RequestBody ApplyRequest request) {
        return applicationService.applyToSuggestedProject(
                request.getApplicationId(),
                request.getEmployeeId()
        );
    }
}

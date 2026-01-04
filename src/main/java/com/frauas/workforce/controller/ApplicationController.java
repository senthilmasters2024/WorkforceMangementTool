package com.frauas.workforce.controller;

import com.frauas.workforce.DTO.*;
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
    public ResponseEntity<?> suggestProjectToEmployee(
            @RequestBody SuggestProjectRequest request
    ) {
        ApplicationResponseDTO response =
                applicationService.suggestProjectToEmployee(request);

        if (response == null) {
            ErrorResponse error = new ErrorResponse(
                    "This employee has already been suggested or applied for this project with the same role",
                    HttpStatus.CONFLICT.value()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/suggested-projects/{employeeId}")
    public List<SuggestedProjectResponseDTO> getSuggestedProjects(
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


    @PostMapping("/apply/applyToOpenProject")
    public ResponseEntity<?> applyToOpenProjectAsNewApplication(@RequestBody Application request) {
        Application application = applicationService.applyToOpenProject(
                request.getEmployeeId(),
                request.getProjectId(),
                request.getProjectRole()
        );

        if (application == null) {
            ErrorResponse error = new ErrorResponse(
                    "You have already applied for this project with the same role",
                    HttpStatus.CONFLICT.value()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }

    @GetMapping("/all")
    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/grouped-by-project")
    public java.util.Map<String, List<Application>> getApplicationsGroupedByProject(
            @RequestParam(required = false) String status
    ) {
        return applicationService.getApplicationsGroupedByProject(status);
    }
}

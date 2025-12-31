package com.frauas.workforce.controller;

import com.frauas.workforce.DTO.ApplicationResponseDTO;
import com.frauas.workforce.DTO.ApplyRequest;
import com.frauas.workforce.DTO.ErrorResponse;
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
}

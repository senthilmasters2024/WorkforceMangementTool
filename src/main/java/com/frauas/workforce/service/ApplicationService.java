package com.frauas.workforce.service;

import com.frauas.workforce.DTO.ApplicationResponseDTO;
import com.frauas.workforce.DTO.SuggestProjectRequest;
import com.frauas.workforce.model.*;
import com.frauas.workforce.repository.ApplicationRepository;
import com.frauas.workforce.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EmployeeRepository employeeRepository; // fetch employee names

    public ApplicationResponseDTO suggestProjectToEmployee(SuggestProjectRequest request) {

        // Validate: Check if employee has already been suggested or applied for this project and role
        Optional<Application> existingApplication = applicationRepository
                .findByEmployeeIdAndProjectIdAndProjectRole(
                        request.getEmployeeId(),
                        request.getProjectId(),
                        request.getProjectRole()
                );

        if (existingApplication.isPresent()) {
            return null; // Will be handled in controller to return error response
        }

        Application application = new Application();
        // Generate unique application ID in format: App_{projectId}_{counter}
        long counter = applicationRepository.count() + 1;
        application.setApplicationId("App_" + request.getProjectId() + "_" + counter);
        application.setProjectId(request.getProjectId());
        application.setEmployeeId(request.getEmployeeId());
        application.setCurrentStatus(ApplicationStatus.SUGGESTED);

        // suggestedBy
        UserAction suggestedBy = new UserAction();
        suggestedBy.setUserId(request.getPlannerUserId());
        suggestedBy.setRole("RESOURCE_PLANNER");
        application.setSuggestedBy(suggestedBy);
        application.setProjectRole(request.getProjectRole());

        // timestamps
        Timestamps timestamps = new Timestamps();
        timestamps.setSuggestedAt(Date.from(Instant.now()));
        application.setTimestamps(timestamps);

        // Save to DB
        Application savedApp = applicationRepository.save(application);

        // Convert to ResponseDTO with employee names
        return mapToResponseDTO(savedApp);
    }

    private ApplicationResponseDTO mapToResponseDTO(Application app) {
        ApplicationResponseDTO dto = new ApplicationResponseDTO();
        dto.setId(app.getId());
        dto.setProjectId(app.getProjectId());
        dto.setEmployeeId(app.getEmployeeId());
        dto.setProjectRole(app.getProjectRole());
        dto.setCurrentStatus(app.getCurrentStatus());
        dto.setTimestamps(app.getTimestamps());

        dto.setInitiatedBy(mapUserAction(app.getInitiatedBy()));
        dto.setSuggestedBy(mapUserAction(app.getSuggestedBy()));
        dto.setApprovedBy(mapUserAction(app.getApprovedBy()));
        dto.setConfirmedBy(mapUserAction(app.getConfirmedBy()));

        return dto;
    }

    private ApplicationResponseDTO.UserActionDTO mapUserAction(UserAction action) {
        if (action == null) return null;

        String username = "Unknown User";

        try {
            int userId = Integer.parseInt(action.getUserId());
            username = employeeRepository.findByEmployeeId(userId)
                    .map(Employee::getUsername) // adjust to your getter
                    .orElse("Unknown User");
        } catch (NumberFormatException e) {
            // log warning if needed
        }
        return new ApplicationResponseDTO.UserActionDTO(action.getUserId(), username, action.getRole());
    }

    public List<Application> getSuggestedProjectsForEmployee(Integer employeeId) {
        return applicationRepository.findByEmployeeIdAndCurrentStatus(
                employeeId,
                ApplicationStatus.SUGGESTED
        );
    }

    public Application applyToSuggestedProject(String applicationId, Integer employeeId) {

        Application application = applicationRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Ownership check
        if (!application.getEmployeeId().equals(employeeId)) {
            throw new RuntimeException("You are not allowed to apply for this application");
        }

        // Status validation
        if (application.getCurrentStatus() != ApplicationStatus.SUGGESTED) {
            throw new RuntimeException("Application is not in SUGGESTED state");
        }

        // Update status
        application.setCurrentStatus(ApplicationStatus.APPLIED);

        // Set initiatedBy correctly
        UserAction initiatedBy = new UserAction(
                employeeId.toString(),
                Role.EMPLOYEE
        );
        application.setInitiatedBy(initiatedBy);

        // Set timestamp
        if (application.getTimestamps() == null) {
            application.setTimestamps(new Timestamps());
        }
        application.getTimestamps().setAppliedAt(Date.from(Instant.now()));

        return applicationRepository.save(application);
    }

    public Application applyToOpenProject(Integer employeeId, String projectId, String projectRole) {
        // Validate: Check if employee has already applied for this project and role
        Optional<Application> existingApplication = applicationRepository
                .findByEmployeeIdAndProjectIdAndProjectRole(employeeId, projectId, projectRole);

        if (existingApplication.isPresent()) {
            return null; // Will be handled in controller to return error response
        }

        // Create a new application
        Application application = new Application();

        // Generate unique application ID in format: App_{projectId}_{counter}
        long counter = applicationRepository.count() + 1;
        application.setApplicationId("App_" + projectId + "_" + counter);

        // Set basic fields
        application.setEmployeeId(employeeId);
        application.setProjectId(projectId);
        application.setProjectRole(projectRole);

        // Set status to APPLIED (employee is directly applying)
        application.setCurrentStatus(ApplicationStatus.APPLIED);

        // Set initiatedBy to employee
        UserAction initiatedBy = new UserAction(
                employeeId.toString(),
                Role.EMPLOYEE
        );
        application.setInitiatedBy(initiatedBy);

        // Set timestamps
        Timestamps timestamps = new Timestamps();
        timestamps.setAppliedAt(Date.from(Instant.now()));
        application.setTimestamps(timestamps);

        // Save to database
        return applicationRepository.save(application);
    }
}

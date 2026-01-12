package com.frauas.workforce.service;

import com.frauas.workforce.DTO.*;
import com.frauas.workforce.model.*;
import com.frauas.workforce.repository.ApplicationRepository;
import com.frauas.workforce.repository.EmployeeRepository;
import com.frauas.workforce.repository.ProjectManagerRepository;
import com.frauas.workforce.repository.ProjectRepository;
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

    @Autowired
    private ProjectManagerRepository projectRepo;

    public ApplicationResponseDTO suggestProjectToEmployee(SuggestProjectRequest request) {

        // Validate: Check if employee has already been suggested or applied for this project and role
        Optional<Application> existingApplication = applicationRepository
                .findByEmployeeIdAndProjectIdAndProjectRole(
                        request.getEmployeeId(),
                        request.getProjectId(),
                        request.getProjectRole()
                );

        Employee employee = employeeRepository.findByEmployeeId(Integer.valueOf(request.getPlannerUserId()))
                .orElseThrow(() -> new RuntimeException("Department Head not found"));

        if (existingApplication.isPresent()) {
            return null; // Will be handled in controller to return error response
        }

        Application application = new Application();
        // Generate unique application ID using UUID to prevent duplicates
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        application.setApplicationId("App_" + request.getProjectId() + "_" + uniqueId);
        application.setProjectId(request.getProjectId());
        application.setEmployeeId(request.getEmployeeId());
        application.setCurrentStatus(ApplicationStatus.SUGGESTED);

        // suggestedBy
        UserAction suggestedBy = new UserAction();
        suggestedBy.setUserId(request.getPlannerUserId());
        suggestedBy.setUserName(employee.getFirstName() + " " + employee.getLastName());
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
        dto.setApplicationId(app.getApplicationId());
        dto.setProjectId(app.getProjectId());
        dto.setEmployeeId(app.getEmployeeId());
        dto.setProjectRole(app.getProjectRole());
        dto.setCurrentStatus(app.getCurrentStatus());
        dto.setTimestamps(app.getTimestamps());

        dto.setInitiatedBy(mapUserAction(app.getInitiatedBy()));
        dto.setSuggestedBy(mapUserAction(app.getSuggestedBy()));
        dto.setApprovedByDepartmentHead(mapUserAction(app.getApprovedByDepartmentHead()));
        dto.setApprovedByProjectManager(mapUserAction(app.getApprovedByProjectManager()));
        dto.setRejectedBy(mapUserAction(app.getRejectedBy()));

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

//    public List<Application> getSuggestedProjectsForEmployee(Integer employeeId) {
//        return applicationRepository.findByEmployeeIdAndCurrentStatus(
//                employeeId,
//                ApplicationStatus.SUGGESTED
//        );
//    }

    public ApplicationResponseDTO applyToSuggestedProject(String applicationId, Integer employeeId) {

        Application application = applicationRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Employee employee = employeeRepository.findByEmployeeId(application.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

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

        // initiatedBy
        UserAction intiatedBy = new UserAction();
        intiatedBy.setUserId(String.valueOf(employeeId));
        intiatedBy.setUserName(employee.getFirstName() + " " + employee.getLastName());
        intiatedBy.setRole("EMPLOYEE");
        application.setInitiatedBy(intiatedBy);

        // timestamps
        if (application.getTimestamps() == null) {
            application.setTimestamps(new Timestamps());
        }
        application.getTimestamps().setAppliedAt(Date.from(Instant.now()));

        Application saved = applicationRepository.save(application);

        // MAP & RETURN DTO
        return mapToResponseDTO(saved);
    }


    public ApplicationResponseDTO applyToOpenProject(Integer employeeId, String projectId, String projectRole) {
        // Validate: Check if employee has already applied for this project and role
        Optional<Application> existingApplication = applicationRepository
                .findByEmployeeIdAndProjectIdAndProjectRole(employeeId, projectId, projectRole);

        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (existingApplication.isPresent()) {
            return null; // Will be handled in controller to return error response
        }

        // Create a new application
        Application application = new Application();

        // Generate unique application ID using UUID to prevent duplicates
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        application.setApplicationId("App_" + projectId + "_" + uniqueId);

        // Set basic fields
        application.setEmployeeId(employeeId);
        application.setProjectId(projectId);
        application.setProjectRole(projectRole);

        // Set status to APPLIED (employee is directly applying)
        application.setCurrentStatus(ApplicationStatus.APPLIED);

        // Set initiatedBy to employee
        UserAction initiatedBy = new UserAction(
                employeeId.toString(),
                employee.getUsername(),
                Role.EMPLOYEE
        );
        application.setInitiatedBy(initiatedBy);

        // Set timestamps
        Timestamps timestamps = new Timestamps();
        timestamps.setAppliedAt(Date.from(Instant.now()));
        application.setTimestamps(timestamps);

        // Save to database
        Application saved = applicationRepository.save(application);

        // MAP & RETURN DTO
        return mapToResponseDTO(saved);
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public java.util.Map<String, List<Application>> getApplicationsGroupedByProject(String statusFilter) {
        List<Application> applications = applicationRepository.findAll();

        // Filter by status if provided
        if (statusFilter != null && !statusFilter.isEmpty()) {
            try {
                ApplicationStatus status = ApplicationStatus.valueOf(statusFilter);
                applications = applications.stream()
                        .filter(app -> app.getCurrentStatus() == status)
                        .collect(java.util.stream.Collectors.toList());
            } catch (IllegalArgumentException e) {
                // Invalid status, return empty map
                return new java.util.HashMap<>();
            }
        }

        // Group by projectId
        return applications.stream()
                .collect(java.util.stream.Collectors.groupingBy(Application::getProjectId));
    }

    public List<SuggestedProjectResponseDTO> getSuggestedProjectsForEmployee(Integer employeeId) {

        // 1. Fetch suggested applications for employee
        List<Application> applications =
                applicationRepository.findByEmployeeId(
                        employeeId
                );

        // 2. Map each application -> project
        return applications.stream().map(application -> {

            Project project = projectRepo.findByProjectId(application.getProjectId())
                    .orElseThrow(() -> new RuntimeException(
                            "Project not found for projectId: " + application.getProjectId()
                    ));

            return new SuggestedProjectResponseDTO(
                    mapToApplicationResponse(application, project),
                    mapToProjectResponse(project)
            );

        }).toList();
    }

    // ------------------ MAPPERS ------------------

    private ApplicationResponseDTO mapToApplicationResponse(Application app, Project project) {

        ApplicationResponseDTO dto = new ApplicationResponseDTO();

        dto.setId(app.getId());
        dto.setApplicationId(app.getApplicationId());
        dto.setProjectId(app.getProjectId());
        dto.setEmployeeId(app.getEmployeeId());
        dto.setProjectRole(app.getProjectRole());
        dto.setCurrentStatus(app.getCurrentStatus());

        // Map requestedCapacity from Project.roles
        if (project.getRoles() != null && app.getProjectRole() != null) {
            for (Project.RoleRequirement roleRequirement : project.getRoles()) {
                if (app.getProjectRole().equalsIgnoreCase(roleRequirement.getRequiredRole())) {
                    dto.setRequestedCapacity(Integer.valueOf(roleRequirement.getNumberOfEmployees()));
                    break;
                }
            }
        }


        // Map UserActions → UserActionDTO
        dto.setInitiatedBy(mapUserActionToDto(app.getInitiatedBy()));
        dto.setSuggestedBy(mapUserActionToDto(app.getSuggestedBy()));
        dto.setApprovedByDepartmentHead(mapUserActionToDto(app.getApprovedByDepartmentHead()));
        dto.setApprovedByProjectManager(mapUserActionToDto(app.getApprovedByProjectManager()));
        dto.setRejectedBy(mapUserActionToDto(app.getRejectedBy()));

        dto.setTimestamps(app.getTimestamps());

        return dto;
    }

    private ApplicationResponseDTO.UserActionDTO mapUserActionToDto(UserAction action) {
        if (action == null) return null;

        return new ApplicationResponseDTO.UserActionDTO(
                action.getUserId(),
                action.getUserName(),
                action.getRole()
        );
    }



    private ProjectResponseDto mapToProjectResponse(Project project) {

        ProjectResponseDto dto = new ProjectResponseDto();
        RoleRequirementDto responseDto  = new RoleRequirementDto();

        dto.setId(project.getId());
        dto.setProjectId(project.getProjectId());
        dto.setProjectDescription(project.getProjectDescription());
        dto.setProjectStart(project.getProjectStart());
        dto.setProjectEnd(project.getProjectEnd());
        dto.setTaskDescription(project.getTaskDescription());
        dto.setRequiredEmployees(project.getRequiredEmployees());

        // If you later map managers / owners, do it here
//        dto.setProjectManager(null);

        dto.setLinks(project.getLinks());
        dto.setSelectedSkills(project.getSelectedSkills());
        dto.setSelectedLocations(project.getSelectedLocations());

        // Role mapping (custom because inner class differs)
        dto.setRoles(mapRoleRequirements(project.getRoles()));


        dto.setStatus(project.getStatus());
        dto.setIsPublished(project.getIsPublished());
        dto.setCreatedBy(project.getCreatedBy());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        dto.setUpdatedBy(project.getUpdatedBy());

        return dto;
    }

    private List<RoleRequirementDto> mapRoleRequirements(
            List<Project.RoleRequirement> roles
    ) {
        if (roles == null) return null;

        return roles.stream()
                .map(role -> {
                    RoleRequirementDto dto = new RoleRequirementDto();

                    // Core fields
                    dto.setRequiredRole(role.getRequiredRole());
                    dto.setRequiredCompetencies(role.getRequiredCompetencies());

                    // Capacity & count
                    dto.setCapacity(role.getCapacity());
                    dto.setNumberOfEmployees(role.getNumberOfEmployees());

                    // Optional UI-only fields (not persisted)
                    dto.setRoleInput(null);
                    dto.setCompetencyInput(null);

                    // UI flags (safe defaults)
                    dto.setShowRoleDropdown(false);
                    dto.setShowCompetencyDropdown(false);

                    return dto;
                })
                .toList();
    }


}

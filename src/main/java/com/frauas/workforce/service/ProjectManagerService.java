package com.frauas.workforce.service;

import com.frauas.workforce.DTO.CreateProjectRequestDto;
import com.frauas.workforce.DTO.ProjectResponseDto;
import com.frauas.workforce.DTO.StaffingRequirementDto;
import com.frauas.workforce.DTO.UpdateProjectRequestDto;
import com.frauas.workforce.ExceptionHandling.ResourceNotFoundException;
import com.frauas.workforce.model.Project;
import com.frauas.workforce.model.ProjectStatus;

import com.frauas.workforce.repository.ProjectManagerRepository;
import lombok.RequiredArgsConstructor;


import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ProjectManagerService Class
 *
 * Service layer for managing project-related business logic.
 * Handles creation, retrieval, updating, deletion, and lifecycle management
 * of projects in the workforce management system. Acts as an intermediary
 * between the controller and repository layers.
 *
 * @author Workforce Management Team
 * @version 1.0
 * @since 2025-11-16
 */
@Service
public class ProjectManagerService {
    private final ProjectManagerRepository projectRepository;
    private static final Logger log = LoggerFactory.getLogger(ProjectManagerService.class);

    /**
     * Constructor for ProjectManagerService.
     * Dependency injection of ProjectManagerRepository.
     *
     * @param projectRepository Repository for project database operations
     */
    public ProjectManagerService(ProjectManagerRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Create a new project in the system.
     *
     * Validates the project dates to ensure end date is after start date,
     * maps the request DTO to a Project entity, sets default values,
     * saves to the database, and returns the created project as a response DTO.
     *
     * @param request CreateProjectRequestDto containing all project details
     * @param currentUserId ID of the authenticated user creating the project
     * @return ProjectResponseDto containing the created project with generated ID and timestamps
     * @throws IllegalArgumentException if project end date is before start date
     */
    public ProjectResponseDto createProject(CreateProjectRequestDto request, String currentUserId) {
        log.info("Creating new project: {}", request.getProjectName());

        // Validate dates
        if (request.getProjectEnd().isBefore(request.getProjectStart())) {
            throw new IllegalArgumentException("Project end date must be after start date");
        }

        // Create new project entity
        Project project = new Project();
        project.setProjectName(request.getProjectName());
        project.setProjectDescription(request.getProjectDescription());
        project.setProjectStart(request.getProjectStart());
        project.setProjectEnd(request.getProjectEnd());
        project.setTaskDescription(request.getTaskDescription());
        project.setNumberOfRequiredEmployees(request.getNumberOfRequiredEmployees());
        project.setLocation(request.getLocation());
        project.setLinks(request.getLinks());
        project.setStatus(request.getStatus());
        project.setIsPublished(request.getIsPublished() != null ? request.getIsPublished() : false);
        project.setCreatedBy(currentUserId);

        // Map staffing requirements
        if (request.getStaffingRequirements() != null) {
            List<Project.StaffingRequirement> staffingReqs = request.getStaffingRequirements().stream()
                    .map(dto -> new Project.StaffingRequirement(
                            dto.getRole(),
                            dto.getRequiredCompetencies(),
                            dto.getCapacityHoursPerWeek(),
                            dto.getExperienceLevel()
                    ))
                    .collect(Collectors.toList());
            project.setStaffingRequirements(staffingReqs);
        }

        Project savedProject = projectRepository.save(project);
        log.info("Project created successfully with ID: {}", savedProject.getId());

        return mapToResponse(savedProject);
    }

    /**
     * Update an existing project with new information.
     *
     * Retrieves the existing project by ID, validates date constraints,
     * updates all fields with new values from the request, and saves changes.
     * Automatically updates the updatedAt timestamp and updatedBy field.
     *
     * @param projectId Unique identifier of the project to update
     * @param request UpdateProjectRequestDto containing updated project details
     * @param currentUserId ID of the authenticated user making the update
     * @return ProjectResponseDto containing the updated project information
     * @throws ResourceNotFoundException if no project exists with the given projectId
     * @throws IllegalArgumentException if project end date is before start date
     */
    public ProjectResponseDto updateProject(String projectId, UpdateProjectRequestDto request, String currentUserId) {
        log.info("Updating project with ID: {}", projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        // Validate dates
        if (request.getProjectEnd().isBefore(request.getProjectStart())) {
            throw new IllegalArgumentException("Project end date must be after start date");
        }

        // Update project fields
        project.setProjectName(request.getProjectName());
        project.setProjectDescription(request.getProjectDescription());
        project.setProjectStart(request.getProjectStart());
        project.setProjectEnd(request.getProjectEnd());
        project.setTaskDescription(request.getTaskDescription());
        project.setNumberOfRequiredEmployees(request.getNumberOfRequiredEmployees());
        project.setLocation(request.getLocation());
        project.setLinks(request.getLinks());
        project.setStatus(request.getStatus());
        project.setIsPublished(request.getIsPublished());
        project.setUpdatedBy(currentUserId);

        // Update staffing requirements
        if (request.getStaffingRequirements() != null) {
            List<Project.StaffingRequirement> staffingReqs = request.getStaffingRequirements().stream()
                    .map(dto -> new Project.StaffingRequirement(
                            dto.getRole(),
                            dto.getRequiredCompetencies(),
                            dto.getCapacityHoursPerWeek(),
                            dto.getExperienceLevel()
                    ))
                    .collect(Collectors.toList());
            project.setStaffingRequirements(staffingReqs);
        }

        Project savedProject = projectRepository.save(project);
        log.info("Project updated successfully with ID: {}", savedProject.getId());

        return mapToResponse(savedProject);
    }

    /**
     * Retrieve a single project by its unique identifier.
     *
     * @param projectId Unique identifier of the project to retrieve
     * @return ProjectResponseDto containing the project details
     * @throws ResourceNotFoundException if no project exists with the given projectId
     */
    public ProjectResponseDto getProjectById(String projectId) {
        log.info("Fetching project with ID: {}", projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        return mapToResponse(project);
    }

    /**
     * Retrieve all projects from the database.
     *
     * Fetches all projects regardless of status or publication state.
     * Useful for admin views and comprehensive project listings.
     *
     * @return List of ProjectResponseDto containing all projects in the system
     */
    public List<ProjectResponseDto> getAllProjects() {
        log.info("Fetching all projects");

        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all projects filtered by their current status.
     *
     * Useful for displaying projects in specific lifecycle stages,
     * such as showing only ACTIVE projects or COMPLETED projects.
     *
     * @param status The project status to filter by (PLANNED, OPEN, STAFFING, ACTIVE, COMPLETED)
     * @return List of ProjectResponseDto containing projects with the specified status
     */
    public List<ProjectResponseDto> getProjectsByStatus(ProjectStatus status) {
        log.info("Fetching projects with status: {}", status);

        List<Project> projects = projectRepository.findByStatus(status);
        return projects.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all projects created by a specific user.
     *
     * Used to display a project manager's portfolio or to filter
     * projects by ownership. Helpful for "My Projects" views.
     *
     * @param creatorId User ID of the project creator
     * @return List of ProjectResponseDto containing projects created by the specified user
     */
    public List<ProjectResponseDto> getProjectsByCreator(String creatorId) {
        log.info("Fetching projects created by: {}", creatorId);

        List<Project> projects = projectRepository.findByCreatedBy(creatorId);
        return projects.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Publish a project to make it visible to all employees.
     *
     * Changes the isPublished flag to true, making the project
     * discoverable and accessible to employees for assignment or viewing.
     *
     * @param projectId Unique identifier of the project to publish
     * @param currentUserId ID of the authenticated user performing the publish action
     * @return ProjectResponseDto containing the published project details
     * @throws ResourceNotFoundException if no project exists with the given projectId
     */
    public ProjectResponseDto publishProject(String projectId, String currentUserId) {
        log.info("Publishing project with ID: {}", projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        project.setIsPublished(true);
        project.setUpdatedBy(currentUserId);

        Project savedProject = projectRepository.save(project);
        log.info("Project published successfully with ID: {}", savedProject.getId());

        return mapToResponse(savedProject);
    }

    /**
     * Update the lifecycle status of a project.
     *
     * Transitions the project through its lifecycle stages
     * (PLANNED → OPEN → STAFFING → ACTIVE → COMPLETED).
     * Useful for tracking project progress and workflow management.
     *
     * @param projectId Unique identifier of the project
     * @param status New status to set for the project
     * @param currentUserId ID of the authenticated user making the status change
     * @return ProjectResponseDto containing the updated project with new status
     * @throws ResourceNotFoundException if no project exists with the given projectId
     */
    public ProjectResponseDto updateProjectStatus(String projectId, ProjectStatus status, String currentUserId) {
        log.info("Updating project status to {} for ID: {}", status, projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        project.setStatus(status);
        project.setUpdatedBy(currentUserId);

        Project savedProject = projectRepository.save(project);
        log.info("Project status updated successfully for ID: {}", savedProject.getId());

        return mapToResponse(savedProject);
    }

    /**
     * Delete a project from the system permanently.
     *
     * Removes the project from the database. This operation is irreversible.
     * Use with caution - consider archiving or soft-delete for production systems.
     *
     * @param projectId Unique identifier of the project to delete
     * @throws ResourceNotFoundException if no project exists with the given projectId
     */
    public void deleteProject(String projectId) {
        log.info("Deleting project with ID: {}", projectId);

        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }

        projectRepository.deleteById(projectId);
        log.info("Project deleted successfully with ID: {}", projectId);
    }

    /**
     * Map Project entity to ProjectResponseDto.
     *
     * Internal helper method to convert a Project entity from the database
     * into a ProjectResponseDto for API responses. Includes mapping of
     * nested staffing requirements.
     *
     * @param project Project entity to convert
     * @return ProjectResponseDto containing all project data for API response
     */
    private ProjectResponseDto mapToResponse(Project project) {
        ProjectResponseDto response = new ProjectResponseDto();
        response.setId(project.getId());
        response.setProjectName(project.getProjectName());
        response.setProjectDescription(project.getProjectDescription());
        response.setProjectStart(project.getProjectStart());
        response.setProjectEnd(project.getProjectEnd());
        response.setTaskDescription(project.getTaskDescription());
        response.setNumberOfRequiredEmployees(project.getNumberOfRequiredEmployees());
        response.setLocation(project.getLocation());
        response.setLinks(project.getLinks());
        response.setStatus(project.getStatus());
        response.setIsPublished(project.getIsPublished());
        response.setCreatedBy(project.getCreatedBy());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        response.setUpdatedBy(project.getUpdatedBy());

        // Map staffing requirements
        if (project.getStaffingRequirements() != null) {
            List<StaffingRequirementDto> staffingDtos = project.getStaffingRequirements().stream()
                    .map(req -> new StaffingRequirementDto(
                            req.getRole(),
                            req.getRequiredCompetencies(),
                            req.getCapacityHoursPerWeek(),
                            req.getExperienceLevel()
                    ))
                    .collect(Collectors.toList());
            response.setStaffingRequirements(staffingDtos);
        }

        return response;
    }
}

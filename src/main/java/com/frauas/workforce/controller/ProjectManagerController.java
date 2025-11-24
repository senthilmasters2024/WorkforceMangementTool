package com.frauas.workforce.controller;

import com.frauas.workforce.DTO.CreateProjectRequestDto;
import com.frauas.workforce.DTO.ProjectResponseDto;
import com.frauas.workforce.DTO.UpdateProjectRequestDto;
import com.frauas.workforce.model.ProjectStatus;
import com.frauas.workforce.service.ApiResponse;
import com.frauas.workforce.service.ProjectManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ProjectManagerController
 *
 * REST API controller for managing project lifecycle operations.
 * Provides endpoints for creating, retrieving, updating, publishing,
 * and deleting projects in the workforce management system.
 *
 * Base URL: /api/projects
 *
 * All endpoints require authentication except where specified.
 * The authenticated user information is obtained from the Security context.
 *
 * @author Workforce Management Team
 * @version 1.0
 * @since 2025-11-16
 */
@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectManagerController {

    private static final Logger log = LoggerFactory.getLogger(ProjectManagerController.class);
    private  final  ProjectManagerService projectService;

    /**
     * Constructor for ProjectManagerController.
     * Dependency injection of ProjectManagerService.
     *
     * @param projectService Service layer for project operations
     */
    public ProjectManagerController(ProjectManagerService projectService) {
        this.projectService = projectService;
    }

    /**
     * Create a new project.
     *
     * Endpoint: POST /api/projects
     *
     * Creates a new project with the provided details. The authenticated user
     * is automatically set as the project creator. Validates all required fields
     * and date constraints before saving.
     *
     * @param request CreateProjectRequestDto containing project details (validated)
     * @return ResponseEntity with HTTP 201 (CREATED) and created project details on success
     *         HTTP 400 (BAD_REQUEST) for validation errors
     *         HTTP 500 (INTERNAL_SERVER_ERROR) for unexpected errors
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponseDto>> createProject(
            @Valid @RequestBody CreateProjectRequestDto request) {

        try {
//            String currentUserId = authentication.getName(); // Get current user from authentication
            ProjectResponseDto response = projectService.createProject(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(response, "Project created successfully"));
        } catch (IllegalArgumentException e) {
            log.error("Validation error creating project: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Validation error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating project: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create project", e.getMessage()));
        }
    }

    /**
     * Update an existing project.
     *
     * Endpoint: PUT /api/projects/{projectId}
     *
     * Updates all fields of an existing project. The authenticated user
     * is recorded as the last updater. Automatically updates the updatedAt timestamp.
     *
     * @param projectId Path variable - unique identifier of the project to update
     * @param request UpdateProjectRequestDto containing updated project details (validated)
     * @return ResponseEntity with HTTP 200 (OK) and updated project details on success
     *         HTTP 400 (BAD_REQUEST) for validation errors
     *         HTTP 404 (NOT_FOUND) if project doesn't exist
     *         HTTP 500 (INTERNAL_SERVER_ERROR) for unexpected errors
     */
    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> updateProject(
            @PathVariable String projectId,
            @Valid @RequestBody UpdateProjectRequestDto request) {

        try {

            ProjectResponseDto response = projectService.updateProject(projectId, request);
            return ResponseEntity.ok(ApiResponse.success(response, "Project updated successfully"));
        } catch (Exception e) {
            log.error("Error updating project: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update project", e.getMessage()));
        }
    }

    /**
     * Retrieve a single project by its ID.
     *
     * Endpoint: GET /api/projects/{projectId}
     *
     * Fetches detailed information about a specific project.
     *
     * @param projectId Path variable - unique identifier of the project to retrieve
     * @return ResponseEntity with HTTP 200 (OK) and project details on success
     *         HTTP 404 (NOT_FOUND) if project doesn't exist
     *         HTTP 500 (INTERNAL_SERVER_ERROR) for unexpected errors
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> getProjectById(@PathVariable String projectId) {
        try {
            ProjectResponseDto response = projectService.getProjectById(projectId);
            return ResponseEntity.ok(ApiResponse.success(response, "Project fetched successfully"));
        } catch (Exception e) {
            log.error("Error fetching project: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Project not found", e.getMessage()));
        }
    }

    @GetMapping("/by-creator/{createdBy}")
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getProjectsByUserId(@PathVariable String createdBy) {
        try {
            List<ProjectResponseDto> projects = projectService.getProjectsByCreator(createdBy);
            return ResponseEntity.ok(ApiResponse.success(projects, "Projects fetched successfully for user: " + createdBy));
        } catch (Exception e) {
            log.error("Error fetching projects by userId: {}", createdBy, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Projects not found for user", e.getMessage()));
        }
    }

    @GetMapping("/published/{isPublished}")
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getProjectsByPublishedStatus(@PathVariable Boolean isPublished) {
        try {
            List<ProjectResponseDto> projects = projectService.getAllProjects()
                    .stream()
                    .filter(p -> p.getIsPublished().equals(isPublished))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success(projects, "Projects fetched successfully for isPublished=" + isPublished));
        } catch (Exception e) {
            log.error("Error fetching projects by isPublished: {}", isPublished, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Projects not found for isPublished=" + isPublished, e.getMessage()));
        }
    }


    /**
     * Retrieve all projects in the system.
     *
     * Endpoint: GET /api/projects
     *
     * Fetches all projects regardless of status or publication state.
     * Useful for admin dashboards and comprehensive project listings.
     *
     * @return ResponseEntity with HTTP 200 (OK) and list of all projects on success
     *         HTTP 500 (INTERNAL_SERVER_ERROR) for unexpected errors
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getAllProjects() {
        try {
            List<ProjectResponseDto> response = projectService.getAllProjects();
            return ResponseEntity.ok(ApiResponse.success(response, "Projects fetched successfully"));
        } catch (Exception e) {
            log.error("Error fetching projects: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch projects", e.getMessage()));
        }
    }

    /**
     * Retrieve projects filtered by status.
     *
     * Endpoint: GET /api/projects/status/{status}
     *
     * Fetches all projects with a specific lifecycle status.
     * Useful for filtering projects by their current stage.
     *
     * @param status Path variable - project status to filter by
     *               (Valid values: PLANNED, OPEN, STAFFING, ACTIVE, COMPLETED)
     * @return ResponseEntity with HTTP 200 (OK) and list of matching projects on success
     *         HTTP 500 (INTERNAL_SERVER_ERROR) for unexpected errors
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getProjectsByStatus(
            @PathVariable ProjectStatus status) {
        try {
            List<ProjectResponseDto> response = projectService.getProjectsByStatus(status);
            return ResponseEntity.ok(ApiResponse.success(response, "Projects fetched successfully"));
        } catch (Exception e) {
            log.error("Error fetching projects by status: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch projects", e.getMessage()));
        }
    }

    /**
     * Retrieve projects created by the current authenticated user.
     *
     * Endpoint: GET /api/projects/my-projects
     *
     * Fetches all projects where the authenticated user is the creator.
     * Useful for "My Projects" views in project manager dashboards.
     *
     * @param authentication Spring Security authentication object containing current user info
     * @return ResponseEntity with HTTP 200 (OK) and list of user's projects on success
     *         HTTP 500 (INTERNAL_SERVER_ERROR) for unexpected errors
     */
    @GetMapping("/my-projects")
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getMyProjects(Authentication authentication) {
        try {
            String currentUserId = authentication.getName();
            List<ProjectResponseDto> response = projectService.getProjectsByCreator(currentUserId);
            return ResponseEntity.ok(ApiResponse.success(response, "Your projects fetched successfully"));
        } catch (Exception e) {
            log.error("Error fetching user projects: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch projects", e.getMessage()));
        }
    }

    /**
     * Publish a project to make it visible to all employees.
     *
     * Endpoint: PATCH /api/projects/{projectId}/publish
     *
     * Changes the project's publication status to make it discoverable
     * and visible to employees for assignment or viewing.
     *
     * @param projectId Path variable - unique identifier of the project to publish
     * @param authentication Spring Security authentication object containing current user info
     * @return ResponseEntity with HTTP 200 (OK) and published project details on success
     *         HTTP 404 (NOT_FOUND) if project doesn't exist
     *         HTTP 500 (INTERNAL_SERVER_ERROR) for unexpected errors
     */
    @PatchMapping("/{projectId}/publish")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> publishProject(
            @PathVariable String projectId,
            Authentication authentication) {
        try {
            String currentUserId = authentication.getName();
            ProjectResponseDto response = projectService.publishProject(projectId, currentUserId);
            return ResponseEntity.ok(ApiResponse.success(response, "Project published successfully"));
        } catch (Exception e) {
            log.error("Error publishing project: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to publish project", e.getMessage()));
        }
    }

    /**
     * Update the lifecycle status of a project.
     *
     * Endpoint: PATCH /api/projects/{projectId}/status
     *
     * Transitions a project through its lifecycle stages
     * (PLANNED → OPEN → STAFFING → ACTIVE → COMPLETED).
     * Used for workflow management and progress tracking.
     *
     * @param projectId Path variable - unique identifier of the project
     * @param status Request parameter - new status to set
     *               (Valid values: PLANNED, OPEN, STAFFING, ACTIVE, COMPLETED)
     * @param authentication Spring Security authentication object containing current user info
     * @return ResponseEntity with HTTP 200 (OK) and updated project details on success
     *         HTTP 404 (NOT_FOUND) if project doesn't exist
     *         HTTP 500 (INTERNAL_SERVER_ERROR) for unexpected errors
     */
    @PatchMapping("/{projectId}/status")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> updateProjectStatus(
            @PathVariable String projectId,
            @RequestParam ProjectStatus status,
            Authentication authentication) {
        try {
            String currentUserId = authentication.getName();
            ProjectResponseDto response = projectService.updateProjectStatus(projectId, status, currentUserId);
            return ResponseEntity.ok(ApiResponse.success(response, "Project status updated successfully"));
        } catch (Exception e) {
//            log.error("Error updating project status: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update project status", e.getMessage()));
        }
    }

    /**
     * Delete a project permanently from the system.
     *
     * Endpoint: DELETE /api/projects/{projectId}
     *
     * Permanently removes a project from the database.
     * This operation is irreversible - use with caution.
     *
     * @param projectId Path variable - unique identifier of the project to delete
     * @return ResponseEntity with HTTP 200 (OK) and success message on completion
     *         HTTP 404 (NOT_FOUND) if project doesn't exist
     *         HTTP 500 (INTERNAL_SERVER_ERROR) for unexpected errors
     */
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable String projectId) {
        try {
            projectService.deleteProject(projectId);
            return ResponseEntity.ok(ApiResponse.success("Project deleted successfully"));
        } catch (Exception e) {
//            log.error("Error deleting project: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete project", e.getMessage()));
        }
    }
}

package com.frauas.workforce.controller;

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

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectManagerController {

    private static final Logger log = LoggerFactory.getLogger(ProjectManagerController.class);
    private  final  ProjectManagerService projectService;

    public ProjectManagerController(ProjectManagerService projectService) {
        this.projectService = projectService;
    }

    /**
     * Update existing project
     * PUT /api/projects/{projectId}
     */
    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> updateProject(
            @PathVariable String projectId,
            @Valid @RequestBody UpdateProjectRequestDto request,
            Authentication authentication) {

        try {
            String currentUserId = authentication.getName(); // Get current user from authentication
            ProjectResponseDto response = projectService.updateProject(projectId, request, currentUserId);
            return ResponseEntity.ok(ApiResponse.success(response, "Project updated successfully"));
        } catch (Exception e) {
            log.error("Error updating project: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update project", e.getMessage()));
        }
    }

    /**
     * Get project by ID
     * GET /api/projects/{projectId}
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

    /**
     * Get all projects
     * GET /api/projects
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
     * Get projects by status
     * GET /api/projects/status/{status}
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
     * Get projects by creator
     * GET /api/projects/my-projects
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
     * Publish project
     * PATCH /api/projects/{projectId}/publish
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
     * Update project status
     * PATCH /api/projects/{projectId}/status
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
     * Delete project
     * DELETE /api/projects/{projectId}
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

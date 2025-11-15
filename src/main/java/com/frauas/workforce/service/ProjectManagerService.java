package com.frauas.workforce.service;

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

@Service
public class ProjectManagerService {
    private final ProjectManagerRepository projectRepository;
    private static final Logger log = LoggerFactory.getLogger(ProjectManagerService.class);

    public ProjectManagerService(ProjectManagerRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    /**
     * Update existing project
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
     * Get project by ID
     */
    public ProjectResponseDto getProjectById(String projectId) {
        log.info("Fetching project with ID: {}", projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        return mapToResponse(project);
    }

    /**
     * Get all projects
     */
    public List<ProjectResponseDto> getAllProjects() {
        log.info("Fetching all projects");

        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get projects by status
     */
    public List<ProjectResponseDto> getProjectsByStatus(ProjectStatus status) {
        log.info("Fetching projects with status: {}", status);

        List<Project> projects = projectRepository.findByStatus(status);
        return projects.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get projects created by specific user
     */
    public List<ProjectResponseDto> getProjectsByCreator(String creatorId) {
        log.info("Fetching projects created by: {}", creatorId);

        List<Project> projects = projectRepository.findByCreatedBy(creatorId);
        return projects.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Publish project
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
     * Update project status
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
     * Delete project
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
     * Map Project entity to ProjectResponse DTO
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

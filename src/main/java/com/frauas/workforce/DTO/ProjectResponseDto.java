package com.frauas.workforce.DTO;

import com.frauas.workforce.model.Project;
import com.frauas.workforce.model.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ProjectResponseDto
 *
 * Data Transfer Object for project API responses.
 * Contains complete project information including metadata fields
 * like creation/update timestamps and user information.
 *
 * Used as response body for all project-related API endpoints.
 *
 * @author Workforce Management Team
 * @version 1.0
 * @since 2025-11-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDto {

    private String id;

    private String projectId;

    private String projectDescription;

    private LocalDate projectStart;

    private LocalDate projectEnd;

    private String taskDescription;

    // Changed from Integer to String
    private Integer requiredEmployees;

    // Changed from List<String> to single String
    private String links;

    // NEW: Selected skills
    private List<String> selectedSkills;

    // NEW: Selected locations (replaces single 'location')
    private List<String> selectedLocations;

    // UPDATED: Roles instead of staffingRequirements
    private List<RoleRequirementDto> roles;

    private ProjectStatus status;

    private Boolean isPublished;

    private String createdBy;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private String updatedBy;
}

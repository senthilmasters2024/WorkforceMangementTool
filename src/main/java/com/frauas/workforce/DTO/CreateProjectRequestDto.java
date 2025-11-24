package com.frauas.workforce.DTO;

import com.frauas.workforce.model.ProjectStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CreateProjectRequestDto
 *
 * Data Transfer Object for creating a new project.
 * Contains all required and optional fields for project creation with validation constraints.
 *
 * Used as request body for POST /api/projects endpoint.
 *
 * @author Workforce Management Team
 * @version 1.0
 * @since 2025-11-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequestDto {

    /**
     * Project name - required field.
     * Must not be blank.
     */

    private String projectId;

    @NotBlank(message = "Project name is required")
    private String projectName;

    @NotBlank(message = "Project description is required")
    private String projectDescription;

    @NotNull(message = "Project start date is required")
    private LocalDate projectStart;

    @NotNull(message = "Project end date is required")
    private LocalDate projectEnd;

    private String taskDescription;

    // Changed from numberOfRequiredEmployees to requiredEmployees (String)
    private Integer requiredEmployees;

    // Changed from 'links' (single URL) to 'links' (single URL string)
    private String links;

    // NEW: Selected skills/technologies
    private List<String> selectedSkills;

    // NEW: Selected locations (multiple locations support)
    private List<String> selectedLocations;

    // UPDATED: Roles structure to match frontend
    private List<RoleRequirementDto> roles;

    @Field("createdBy")
    private String createdBy;

    @NotNull(message = "Project status is required")
    private ProjectStatus status;

    private Boolean isPublished;
}

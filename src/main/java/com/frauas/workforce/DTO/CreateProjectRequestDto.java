package com.frauas.workforce.DTO;

import com.frauas.workforce.model.ProjectStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @NotBlank(message = "Project name is required")
    private String projectName;

    /**
     * Detailed project description - required field.
     * Must not be blank.
     */
    @NotBlank(message = "Project description is required")
    private String projectDescription;

    /**
     * Project start date and time - required field.
     * Must be a valid date-time.
     */
    @NotNull(message = "Project start date is required")
    private LocalDateTime projectStart;

    /**
     * Project end date and time - required field.
     * Must be after projectStart date.
     */
    @NotNull(message = "Project end date is required")
    private LocalDateTime projectEnd;

    /**
     * Detailed task description - optional field.
     * Can contain specific deliverables and work items.
     */
    private String taskDescription;

    /**
     * Number of employees required - optional field with validation.
     * Must be at least 1 if provided.
     */
    @Min(value = 1, message = "At least 1 employee is required")
    private Integer numberOfRequiredEmployees;

    /**
     * Project location - optional field.
     * Physical office location or "Remote".
     */
    private String location;

    /**
     * List of related resource URLs - optional field.
     * Can include documentation, repositories, project management tools, etc.
     */
    private List<String> links;

    /**
     * List of staffing requirements - optional field.
     * Defines specific roles, skills, and experience needed.
     */
    private List<StaffingRequirementDto> staffingRequirements;

    /**
     * Initial project status - required field.
     * Typically set to PLANNED for new projects.
     * Valid values: PLANNED, OPEN, STAFFING, ACTIVE, COMPLETED
     */
    @NotNull(message = "Project status is required")
    private ProjectStatus status;

    /**
     * Publication flag - optional field.
     * If not provided, defaults to false (unpublished/draft).
     * true = visible to all employees, false = draft/hidden
     */
    private Boolean isPublished;
}

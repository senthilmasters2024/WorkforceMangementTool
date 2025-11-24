package com.frauas.workforce.DTO;

import com.frauas.workforce.model.ProjectStatus;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * UpdateProjectRequestDto
 *
 * Data Transfer Object for updating an existing project.
 * Contains all project fields that can be modified with validation constraints.
 *
 * Used as request body for PUT /api/projects/{projectId} endpoint.
 *
 * @author Workforce Management Team
 * @version 1.0
 * @since 2025-11-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequestDto {

    private String projectId;

    @NotBlank(message = "Project description is required")
    private String projectDescription;

    @NotNull(message = "Project start date is required")
    private LocalDate projectStart;

    @NotNull(message = "Project end date is required")
    private LocalDate projectEnd;

    private String taskDescription;

    private Integer requiredEmployees;

    private String links;

    private List<String> selectedSkills;

    private List<String> selectedLocations;

    private List<RoleRequirementDto> roles;

    @NotNull(message = "Project status is required")
    private ProjectStatus status;

    private Boolean isPublished;

    @Field("createdBy")
    private String createdBy;
}

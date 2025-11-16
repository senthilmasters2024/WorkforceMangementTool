package com.frauas.workforce.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * StaffingRequirementDto
 *
 * Data Transfer Object representing staffing requirements for a specific role in a project.
 * Defines the skills, experience, and time commitment needed for a position.
 *
 * Used within CreateProjectRequestDto, UpdateProjectRequestDto, and ProjectResponseDto.
 *
 * @author Workforce Management Team
 * @version 1.0
 * @since 2025-11-16
 */
@Data
@NoArgsConstructor
public class StaffingRequirementDto {

    /**
     * Job role or position title - required field.
     * Examples: "Senior Backend Developer", "DevOps Engineer", "UX Designer"
     */
    @NotBlank(message = "Role is required")
    private String role;

    /**
     * List of required competencies/skills - required field.
     * Must contain at least one competency.
     * Examples: ["Java", "Spring Boot"], ["React", "TypeScript"]
     */
    @NotEmpty(message = "At least one competency is required")
    private List<String> requiredCompetencies;

    /**
     * Number of hours per week required for this role.
     * Must be at least 1 hour per week.
     * Typically ranges from 10 (part-time) to 40 (full-time).
     */
    @Min(value = 1, message = "Capacity must be at least 1 hour per week")
    private Integer capacityHoursPerWeek;

    /**
     * Required experience level for the role - optional field.
     * Common values: "Junior", "Mid-Level", "Senior", "Expert"
     */
    private String experienceLevel;

    /**
     * Parameterized constructor for StaffingRequirementDto.
     *
     * @param role The job role or position title
     * @param requiredCompetencies List of required skills and competencies
     * @param capacityHoursPerWeek Number of hours per week needed
     * @param experienceLevel Required experience level
     */
    public StaffingRequirementDto(String role,
                                  List<String> requiredCompetencies,
                                  Integer capacityHoursPerWeek,
                                  String experienceLevel) {
        this.role = role;
        this.requiredCompetencies = requiredCompetencies;
        this.capacityHoursPerWeek = capacityHoursPerWeek;
        this.experienceLevel = experienceLevel;
    }
}

package com.frauas.workforce.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class StaffingRequirementDto {
    @NotBlank(message = "Role is required")
    private String role;

    @NotEmpty(message = "At least one competency is required")
    private List<String> requiredCompetencies;

    @Min(value = 1, message = "Capacity must be at least 1 hour per week")
    private Integer capacityHoursPerWeek;

    public StaffingRequirementDto(String role,
                               List<String> requiredCompetencies,
                               Integer capacityHoursPerWeek,
                               String experienceLevel) {
        this.role = role;
        this.requiredCompetencies = requiredCompetencies;
        this.capacityHoursPerWeek = capacityHoursPerWeek;
        this.experienceLevel = experienceLevel;
    }

    public @NotBlank(message = "Role is required") String getRole() {
        return role;
    }

    public void setRole(@NotBlank(message = "Role is required") String role) {
        this.role = role;
    }

    public @NotEmpty(message = "At least one competency is required") List<String> getRequiredCompetencies() {
        return requiredCompetencies;
    }

    public void setRequiredCompetencies(@NotEmpty(message = "At least one competency is required") List<String> requiredCompetencies) {
        this.requiredCompetencies = requiredCompetencies;
    }

    public @Min(value = 1, message = "Capacity must be at least 1 hour per week") Integer getCapacityHoursPerWeek() {
        return capacityHoursPerWeek;
    }

    public void setCapacityHoursPerWeek(@Min(value = 1, message = "Capacity must be at least 1 hour per week") Integer capacityHoursPerWeek) {
        this.capacityHoursPerWeek = capacityHoursPerWeek;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    private String experienceLevel;
}

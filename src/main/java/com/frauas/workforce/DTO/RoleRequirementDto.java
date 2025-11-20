package com.frauas.workforce.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequirementDto {
    @NotBlank(message = "Required role is required")
    private String requiredRole;

    @NotEmpty(message = "At least one competency is required")
    private List<String> requiredCompetencies;

    // Capacity as string (e.g., "40hrs/week")
    private String capacity;

    // Number of employees for this role (e.g., "2", "3")
    private String numberOfEmployees;

    // Optional field for role input
    private String roleInput;

    // Optional field for competency input
    private String competencyInput;

    // UI control flags
    private Boolean showRoleDropdown;
    private Boolean showCompetencyDropdown;
}

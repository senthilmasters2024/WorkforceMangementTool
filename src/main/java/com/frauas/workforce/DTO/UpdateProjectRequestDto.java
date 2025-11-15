package com.frauas.workforce.DTO;

import com.frauas.workforce.model.ProjectStatus;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequestDto {
    @NotBlank(message = "Project name is required")
    private String projectName;

    @NotBlank(message = "Project description is required")
    private String projectDescription;

    @NotNull(message = "Project start date is required")
    private LocalDateTime projectStart;

    @NotNull(message = "Project end date is required")
    private LocalDateTime projectEnd;

    private String taskDescription;

    public @NotBlank(message = "Project name is required") String getProjectName() {
        return projectName;
    }

    public void setProjectName(@NotBlank(message = "Project name is required") String projectName) {
        this.projectName = projectName;
    }

    public @NotBlank(message = "Project description is required") String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(@NotBlank(message = "Project description is required") String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public @NotNull(message = "Project start date is required") LocalDateTime getProjectStart() {
        return projectStart;
    }

    public void setProjectStart(@NotNull(message = "Project start date is required") LocalDateTime projectStart) {
        this.projectStart = projectStart;
    }

    public @NotNull(message = "Project end date is required") LocalDateTime getProjectEnd() {
        return projectEnd;
    }

    public void setProjectEnd(@NotNull(message = "Project end date is required") LocalDateTime projectEnd) {
        this.projectEnd = projectEnd;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public @Min(value = 1, message = "At least 1 employee is required") Integer getNumberOfRequiredEmployees() {
        return numberOfRequiredEmployees;
    }

    public void setNumberOfRequiredEmployees(@Min(value = 1, message = "At least 1 employee is required") Integer numberOfRequiredEmployees) {
        this.numberOfRequiredEmployees = numberOfRequiredEmployees;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public List<StaffingRequirementDto> getStaffingRequirements() {
        return staffingRequirements;
    }

    public void setStaffingRequirements(List<StaffingRequirementDto> staffingRequirements) {
        this.staffingRequirements = staffingRequirements;
    }

    public @NotNull(message = "Project status is required") ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "Project status is required") ProjectStatus status) {
        this.status = status;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean published) {
        isPublished = published;
    }

    @Min(value = 1, message = "At least 1 employee is required")
    private Integer numberOfRequiredEmployees;

    private String location;
    private List<String> links;
    private List<StaffingRequirementDto> staffingRequirements;

    @NotNull(message = "Project status is required")
    private ProjectStatus status;

    private Boolean isPublished;

}

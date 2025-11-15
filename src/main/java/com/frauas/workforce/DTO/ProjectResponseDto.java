package com.frauas.workforce.DTO;

import com.frauas.workforce.model.Project;
import com.frauas.workforce.model.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProjectResponseDto {

        private String id;
        private String projectName;
        private String projectDescription;
        private LocalDateTime projectStart;
        private LocalDateTime projectEnd;
        private String taskDescription;
        private Integer numberOfRequiredEmployees;
        private String location;
        private List<String> links;
        private List<StaffingRequirementDto> staffingRequirements;
        private ProjectStatus status;
        private Boolean isPublished;
        private String createdBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public List<StaffingRequirementDto> getStaffingRequirements() {
        return staffingRequirements;
    }

    public void setStaffingRequirements(List<StaffingRequirementDto> staffingRequirements) {
        this.staffingRequirements = staffingRequirements;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getNumberOfRequiredEmployees() {
        return numberOfRequiredEmployees;
    }

    public void setNumberOfRequiredEmployees(Integer numberOfRequiredEmployees) {
        this.numberOfRequiredEmployees = numberOfRequiredEmployees;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public LocalDateTime getProjectEnd() {
        return projectEnd;
    }

    public void setProjectEnd(LocalDateTime projectEnd) {
        this.projectEnd = projectEnd;
    }

    public LocalDateTime getProjectStart() {
        return projectStart;
    }

    public void setProjectStart(LocalDateTime projectStart) {
        this.projectStart = projectStart;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String updatedBy;

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

}

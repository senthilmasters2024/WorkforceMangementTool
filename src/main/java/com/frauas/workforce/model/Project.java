package com.frauas.workforce.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "projects")
public class Project {

    @Id
    private String id;

    private String projectName;
    private String projectDescription;
    private LocalDateTime projectStart;
    private LocalDateTime projectEnd;
    private String taskDescription;
    private Integer numberOfRequiredEmployees;
    private String location;
    private List<String> links;

    private List<StaffingRequirement> staffingRequirements;

    private ProjectStatus status;

    private Boolean isPublished;

    private String createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String updatedBy;

    // --------------------------------------
    // Getters and Setters
    // --------------------------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public LocalDateTime getProjectStart() {
        return projectStart;
    }

    public void setProjectStart(LocalDateTime projectStart) {
        this.projectStart = projectStart;
    }

    public LocalDateTime getProjectEnd() {
        return projectEnd;
    }

    public void setProjectEnd(LocalDateTime projectEnd) {
        this.projectEnd = projectEnd;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Integer getNumberOfRequiredEmployees() {
        return numberOfRequiredEmployees;
    }

    public void setNumberOfRequiredEmployees(Integer numberOfRequiredEmployees) {
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

    public List<StaffingRequirement> getStaffingRequirements() {
        return staffingRequirements;
    }

    public void setStaffingRequirements(List<StaffingRequirement> staffingRequirements) {
        this.staffingRequirements = staffingRequirements;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    // -------------------------------------------------------------------
    // NESTED CLASS WITH GETTERS AND SETTERS
    // -------------------------------------------------------------------
    public static class StaffingRequirement {

        private String role;
        private List<String> requiredCompetencies;
        private Integer capacityHoursPerWeek;
        private String experienceLevel;

        public StaffingRequirement() {}

        public StaffingRequirement(String role,
                                   List<String> requiredCompetencies,
                                   Integer capacityHoursPerWeek,
                                   String experienceLevel) {
            this.role = role;
            this.requiredCompetencies = requiredCompetencies;
            this.capacityHoursPerWeek = capacityHoursPerWeek;
            this.experienceLevel = experienceLevel;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public List<String> getRequiredCompetencies() {
            return requiredCompetencies;
        }

        public void setRequiredCompetencies(List<String> requiredCompetencies) {
            this.requiredCompetencies = requiredCompetencies;
        }

        public Integer getCapacityHoursPerWeek() {
            return capacityHoursPerWeek;
        }

        public void setCapacityHoursPerWeek(Integer capacityHoursPerWeek) {
            this.capacityHoursPerWeek = capacityHoursPerWeek;
        }

        public String getExperienceLevel() {
            return experienceLevel;
        }

        public void setExperienceLevel(String experienceLevel) {
            this.experienceLevel = experienceLevel;
        }
    }
}

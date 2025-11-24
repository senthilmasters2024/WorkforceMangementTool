package com.frauas.workforce.repository;

import com.frauas.workforce.model.Project;
import com.frauas.workforce.model.ProjectStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ProjectManagerRepository Interface
 *
 * Repository interface for performing CRUD operations and custom queries
 * on Project entities in MongoDB. Extends MongoRepository to provide
 * built-in methods for database operations and defines custom query methods
 * for project-specific searches.
 *
 * @author Workforce Management Team
 * @version 1.0
 * @since 2025-11-16
 */
@Repository
public interface ProjectManagerRepository extends MongoRepository<Project, String>{

    /**
     * Find all projects filtered by publication status.
     *
     * @param isPublished Boolean flag - true for published projects, false for draft projects
     * @return List of projects matching the publication status
     */
    List<Project> findByIsPublished(Boolean isPublished);

    /**
     * Find all projects by their current status.
     *
     * @param status The project status (PLANNED, OPEN, STAFFING, ACTIVE, COMPLETED)
     * @return List of projects with the specified status
     */
    List<Project> findByStatus(ProjectStatus status);

    /**
     * Find all projects created by a specific user.
     * Useful for showing a project manager's portfolio of projects.
     *
     * @param createdBy User ID of the project creator (typically a project manager)
     * @return List of projects created by the specified user
     */
    List<Project> findByCreatedBy(String createdBy);

    /**
     * Find projects that match both publication status and project status.
     * Useful for finding published projects in a specific lifecycle stage.
     *
     * @param isPublished Boolean flag for publication status
     * @param status The project status to filter by
     * @return List of projects matching both criteria
     */
    List<Project> findByIsPublishedAndStatus(Boolean isPublished, ProjectStatus status);

    /**
     * Find projects by location using case-insensitive pattern matching.
     * Uses MongoDB regex query to perform partial, case-insensitive matching.
     *
     * Example: "frank" will match "Frankfurt", "frankfurt", "FRANKFURT"
     *
     * @param location Location string or partial location to search for
     * @return List of projects with matching locations
     */
    @Query("{ 'location': { $regex: ?0, $options: 'i' } }")
    List<Project> findByLocationContainingIgnoreCase(String location);

    /**
     * Find all projects where a specific employee is assigned.
     * Searches in the assignedEmployees array field of the project document.
     * Useful for displaying "My Assigned Projects" in employee dashboard.
     *
     * @param employeeId User ID of the employee
     * @return List of projects where the employee is in the assignedEmployees list
     */
    @Query("{ 'assignedEmployees': ?0 }")
    List<Project> findByAssignedEmployeesContaining(String employeeId);

    /**
     * Find all projects related to an employee in any capacity.
     * Searches across three fields: assignedEmployees, createdBy, and updatedBy.
     * This provides a comprehensive view of all projects an employee is involved with.
     *
     * Use cases:
     * - Employee is assigned to the project
     * - Employee created the project
     * - Employee last updated the project
     *
     * @param employeeId User ID of the employee
     * @return List of projects where employee is assigned, creator, or last updater
     */
    @Query("{ $or: [ { 'assignedEmployees': ?0 }, { 'createdBy': ?0 }, { 'updatedBy': ?0 } ] }")
    List<Project> findProjectsByEmployeeId(String employeeId);

    /**
     * Find a project by its business projectId field.
     *
     * @param projectId The business project ID (e.g., "PRJ-OZN439")
     * @return Optional containing the project if found
     */
    Optional<Project> findByProjectId(String projectId);

    /**
     * Check if a project exists by its business projectId field.
     *
     * @param projectId The business project ID
     * @return true if project exists, false otherwise
     */
    boolean existsByProjectId(String projectId);
}
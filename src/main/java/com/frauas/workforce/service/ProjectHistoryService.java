package com.frauas.workforce.service;

import com.frauas.workforce.model.*;
import com.frauas.workforce.repository.ApplicationRepository;
import com.frauas.workforce.repository.EmployeeRepository;
import com.frauas.workforce.repository.ProjectManagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ProjectHistoryService
 *
 * Service for managing employee project history tracking.
 * Handles starting projects, completing projects, and retrieving project history.
 */
@Service
@AllArgsConstructor
public class ProjectHistoryService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectManagerRepository projectRepository;

    /**
     * Get all project history for an employee (completed/past projects)
     * Projects where status = COMPLETED AND projectEnd < today
     *
     * @param employeeId The employee ID
     * @return List of completed project applications with details
     */
    public List<Application> getEmployeeProjectHistory(Integer employeeId) {
        LocalDate today = LocalDate.now();

        // Get all COMPLETED applications for this employee
        return applicationRepository.findAll().stream()
                .filter(app -> app.getEmployeeId().equals(employeeId)
                        && app.getCurrentStatus() == ApplicationStatus.COMPLETED)
                .filter(app -> {
                    // Check if project end date is in the past
                    LocalDate projectEnd = app.getEmployeeProjectEndDate();
                    return projectEnd != null && projectEnd.isBefore(today);
                })
                .sorted((a, b) -> {
                    LocalDate dateA = a.getEmployeeProjectEndDate();
                    LocalDate dateB = b.getEmployeeProjectEndDate();
                    if (dateA == null && dateB == null) return 0;
                    if (dateA == null) return 1;
                    if (dateB == null) return -1;
                    return dateB.compareTo(dateA); // Most recent first
                })
                .collect(Collectors.toList());
    }

    /**
     * Get all employees who have worked on a specific project (past projects only)
     * Projects where status = COMPLETED AND projectEnd < today
     *
     * @param projectId The project ID
     * @return List of applications for employees who completed work on this project
     */
    public List<Application> getProjectEmployeeHistory(String projectId) {
        LocalDate today = LocalDate.now();

        return applicationRepository.findAll().stream()
                .filter(app -> app.getProjectId().equals(projectId)
                        && app.getCurrentStatus() == ApplicationStatus.COMPLETED)
                .filter(app -> {
                    // Check if project end date is in the past
                    LocalDate projectEnd = app.getEmployeeProjectEndDate();
                    return projectEnd != null && projectEnd.isBefore(today);
                })
                .collect(Collectors.toList());
    }

    /**
     * Get currently active project for an employee
     * Projects where status = COMPLETED AND projectEnd >= today
     *
     * @param employeeId The employee ID
     * @return Current active application or null if no active project
     */
    public Application getCurrentActiveProject(Integer employeeId) {
        LocalDate today = LocalDate.now();

        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getAssignedProjectId() == null) {
            return null;
        }

        return applicationRepository.findAll().stream()
                .filter(app -> app.getEmployeeId().equals(employeeId)
                        && app.getProjectId().equals(employee.getAssignedProjectId())
                        && app.getCurrentStatus() == ApplicationStatus.COMPLETED)
                .filter(app -> {
                    // Check if project is ongoing or future (not yet ended)
                    LocalDate projectEnd = app.getEmployeeProjectEndDate();
                    return projectEnd != null && !projectEnd.isBefore(today);
                })
                .findFirst()
                .orElse(null);
    }
}

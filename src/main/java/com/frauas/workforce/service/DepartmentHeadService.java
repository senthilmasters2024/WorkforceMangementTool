package com.frauas.workforce.service;

import com.frauas.workforce.model.*;
import com.frauas.workforce.repository.ApplicationRepository;
import com.frauas.workforce.repository.EmployeeRepository;
import com.frauas.workforce.repository.ProjectManagerRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentHeadService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectManagerRepository projectRepository;

    /**
     * Approve an employee's application for a project
     *
     * @param applicationId The unique application ID
     * @param departmentHeadId The employee ID of the department head
     * @param comments Optional approval comments
     * @return The updated Application object
     * @throws RuntimeException if validation fails
     */
    public Application approveApplication(String applicationId, Integer departmentHeadId, String comments) {

        // 1. Find the application
        Application application = applicationRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // 2. Validate application status
        if (application.getCurrentStatus() != ApplicationStatus.REQUEST_DH_APPROVAL) {
            throw new RuntimeException("Only applications with REQUEST_DH_APPROVAL status can be approved");
        }

        // 3. Get the employee's department
        Employee employee = employeeRepository.findByEmployeeId(application.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // 4. Get the department head's details
        Employee departmentHead = employeeRepository.findByEmployeeId(departmentHeadId)
                .orElseThrow(() -> new RuntimeException("Department Head not found"));

        // 5. Validate that the department head can approve this employee (same department)
        if (!employee.getDepartment().equals(departmentHead.getDepartment())) {
            throw new RuntimeException("You can only approve applications for employees in your department");
        }

        // 6. Validate that the user is actually a Department Head
        if (departmentHead.getRole() != Role.DEPARTMENT_HEAD) {
            throw new RuntimeException("Only Department Heads can approve applications");
        }

        // 7. Update the application (DH approval is final step - employee is assigned)
        application.setCurrentStatus(ApplicationStatus.COMPLETED);

        // 8. Set approvedBy
        UserAction approvedBy = new UserAction();
        approvedBy.setUserId(departmentHeadId.toString());
        approvedBy.setUserName(departmentHead.getFirstName() + " " + departmentHead.getLastName());
        approvedBy.setRole(Role.DEPARTMENT_HEAD.name());
        application.setApprovedByDepartmentHead(approvedBy);

        // 9. Set approval comments if provided
        if (comments != null && !comments.trim().isEmpty()) {
            application.setApprovalComments(comments);
        }

        // 10. Set timestamp
        if (application.getTimestamps() == null) {
            application.setTimestamps(new Timestamps());
        }
        application.getTimestamps().setApprovedAt(Date.from(Instant.now()));

        // 11. Get project details to set employee project dates
        Project project = projectRepository.findByProjectId(application.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // 11.5. Update employee's assignedProjectId
        employee.setAssignedProjectId(application.getProjectId());
        employee.setAvailabilityStatus(AvailabilityStatus.NOT_AVAILABLE);
        employee.setSupervisor(project.getCreatedBy());
        employeeRepository.save(employee);

        // 11.6. Set employee project dates from project dates (not today's date)
        application.setEmployeeProjectStartDate(project.getProjectStart());
        application.setEmployeeProjectEndDate(project.getProjectEnd());

        // 12. Auto-reject all other pending applications by this employee
        List<Application> allEmployeeApplications = applicationRepository.findByEmployeeId(application.getEmployeeId());

        for (Application otherApp : allEmployeeApplications) {
            // Skip the current application and already completed/rejected ones
            if (!otherApp.getId().equals(application.getId())
                && (otherApp.getCurrentStatus() == ApplicationStatus.APPLIED
                    || otherApp.getCurrentStatus() == ApplicationStatus.REQUEST_DH_APPROVAL
                    || otherApp.getCurrentStatus() == ApplicationStatus.SUGGESTED)) {

                // Reject this application
                otherApp.setCurrentStatus(ApplicationStatus.REJECTED_BY_DH);

                // Set rejection reason
                otherApp.setRejectionReason("Employee has been assigned to project: " + application.getProjectId());

                // Set rejectedBy to the same DH who approved
                UserAction autoRejectedBy = new UserAction();
                autoRejectedBy.setUserId(departmentHeadId.toString());
                autoRejectedBy.setUserName(employee.getFirstName() + " " + employee.getLastName());
                autoRejectedBy.setRole(Role.DEPARTMENT_HEAD.name());
                otherApp.setRejectedBy(autoRejectedBy);

                // Save the rejected application
                applicationRepository.save(otherApp);
            }
        }

        // 13. Save and return the approved application
        return applicationRepository.save(application);
    }

    /**
     * Reject an employee's application for a project
     *
     * @param applicationId The unique application ID
     * @param departmentHeadId The employee ID of the department head
     * @param rejectionReason Mandatory rejection reason
     * @return The updated Application object
     * @throws RuntimeException if validation fails
     */
    public Application rejectApplication(String applicationId, Integer departmentHeadId, String rejectionReason) {

        // 1. Validate rejection reason is provided
        if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
            throw new RuntimeException("Rejection reason is required");
        }

        // 2. Find the application
        Application application = applicationRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // 3. Validate application status
        if (application.getCurrentStatus() != ApplicationStatus.REQUEST_DH_APPROVAL) {
            throw new RuntimeException("Only applications with REQUEST_DH_APPROVAL status can be rejected");
        }

        // 4. Get the employee's department
        Employee employee = employeeRepository.findByEmployeeId(application.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // 5. Get the department head's details
        Employee departmentHead = employeeRepository.findByEmployeeId(departmentHeadId)
                .orElseThrow(() -> new RuntimeException("Department Head not found"));

        // 6. Validate that the department head can reject this employee (same department)
        if (!employee.getDepartment().equals(departmentHead.getDepartment())) {
            throw new RuntimeException("You can only reject applications for employees in your department");
        }

        // 7. Validate that the user is actually a Department Head
        if (departmentHead.getRole() != Role.DEPARTMENT_HEAD) {
            throw new RuntimeException("Only Department Heads can reject applications");
        }

        // 8. Update the application
        application.setCurrentStatus(ApplicationStatus.REJECTED_BY_DH);

        // 9. Set rejectedBy
        UserAction rejectedBy = new UserAction();
        rejectedBy.setUserId(departmentHeadId.toString());
        rejectedBy.setUserName(departmentHead.getUsername());
        rejectedBy.setRole(Role.DEPARTMENT_HEAD.name());
        application.setRejectedBy(rejectedBy);

        // 10. Set rejection reason
        application.setRejectionReason(rejectionReason);

        // 11. Set timestamp (we'll need to add rejectedAt to Timestamps model)
        if (application.getTimestamps() == null) {
            application.setTimestamps(new Timestamps());
        }
        // Note: If Timestamps doesn't have rejectedAt, you may need to add it
        // application.getTimestamps().setRejectedAt(Date.from(Instant.now()));

        // 12. Save and return
        return applicationRepository.save(application);
    }

    /**
     * Get all applications for employees in the Department Head's department
     *
     * @param departmentHeadId The employee ID of the department head
     * @param status Optional status filter (if null, returns ALL applications)
     * @return List of applications for employees in the same department
     * @throws RuntimeException if Department Head not found
     */
    public List<Application> getApplicationsForDepartment(Integer departmentHeadId, ApplicationStatus status) {

        // 1. Get the department head's details
        Employee departmentHead = employeeRepository.findByEmployeeId(departmentHeadId)
                .orElseThrow(() -> new RuntimeException("Department Head not found"));

        // 2. Validate that the user is actually a Department Head
        if (departmentHead.getRole() != Role.DEPARTMENT_HEAD) {
            throw new RuntimeException("Only Department Heads can view department applications");
        }

        // 3. Get all employees in the same department
        List<Employee> departmentEmployees = employeeRepository.findByDepartment(departmentHead.getDepartment());

        // 4. Extract employee IDs
        List<Integer> employeeIds = departmentEmployees.stream()
                .map(Employee::getEmployeeId)
                .collect(Collectors.toList());

        // 5. If no employees, return empty list
        if (employeeIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 6. Get all applications for these employees
        List<Application> allApplications = applicationRepository.findAll();

        // 7. Filter by employee IDs and optionally by status
        return allApplications.stream()
                .filter(app -> employeeIds.contains(app.getEmployeeId()))
                .filter(app -> status == null || app.getCurrentStatus() == status)
                .collect(Collectors.toList());
    }
}

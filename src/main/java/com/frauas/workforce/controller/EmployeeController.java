package com.frauas.workforce.controller;
import com.frauas.workforce.model.Employee;
import com.frauas.workforce.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee", description = "Employee management APIs")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(
            summary = "Get all employees",
            description = "Retrieves a list of all employees in the system. Accessible to all authenticated users."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(schema = @Schema(implementation = Employee.class)))
    })
    @GetMapping
//    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'PROJECT_MANAGER', 'DEPARTMENT_HEAD', 'RESOURCE_PLANNER', 'EMPLOYEE')")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Operation(
            summary = "Get employee by Employee ID",
            description = "Retrieves a specific employee by their employee ID. Accessible to all authenticated users."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employee",
                    content = @Content(schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{employeeId}")
//    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'PROJECT_MANAGER', 'DEPARTMENT_HEAD', 'RESOURCE_PLANNER', 'EMPLOYEE')")
    public ResponseEntity<Employee> getEmployeeById(
            @Parameter(description = "Employee ID of the employee to retrieve")
            @PathVariable Integer employeeId) {
        try {
            return employeeService.getEmployeeByEmployeeId(employeeId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IncorrectResultSizeDataAccessException e) {
            Employee errorResponse = new Employee();
            errorResponse.setMessage("Error: Duplicate employeeId found in database. Please contact system administrator.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(
            summary = "Create a new employee",
            description = "Creates a new employee in the system. Only SYSTEM_ADMIN, DEPARTMENT_HEAD, and PROJECT_MANAGER can create employees."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully",
                    content = @Content(schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
//    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'DEPARTMENT_HEAD', 'PROJECT_MANAGER')")
    public ResponseEntity<Employee> createEmployee(
            @Parameter(description = "Employee object to create")
            @RequestBody Employee employee) {
        try {
            Employee createdEmployee = employeeService.createEmployee(employee);
            createdEmployee.setMessage("Employee created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
        } catch (Exception e) {
            employee.setMessage("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(employee);
        }
    }

    @Operation(
            summary = "Update an employee",
            description = "Updates an existing employee's information. SYSTEM_ADMIN, DEPARTMENT_HEAD, PROJECT_MANAGER, and RESOURCE_PLANNER can update employees."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully",
                    content = @Content(schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{employeeId}")
//    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'DEPARTMENT_HEAD', 'PROJECT_MANAGER', 'RESOURCE_PLANNER')")
    public ResponseEntity<Employee> updateEmployee(
            @Parameter(description = "Employee ID of the employee to update")
            @PathVariable Integer employeeId,
            @Parameter(description = "Updated employee object")
            @RequestBody Employee employee) {
        try {
            return employeeService.updateEmployeeByEmployeeId(employeeId, employee)
                    .map(updatedEmployee -> {
                        updatedEmployee.setMessage("Employee updated successfully");
                        return ResponseEntity.ok(updatedEmployee);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (IncorrectResultSizeDataAccessException e) {
            employee.setMessage("Error: Duplicate employeeId found in database. Please contact system administrator.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(employee);
        } catch (Exception e) {
            employee.setMessage("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(employee);
        }
    }

    @Operation(
            summary = "Delete an employee",
            description = "Deletes an employee from the system. Only SYSTEM_ADMIN can delete employees."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{employeeId}")
//    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "Employee ID of the employee to delete")
            @PathVariable Integer employeeId) {
        if (employeeService.deleteEmployeeByEmployeeId(employeeId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

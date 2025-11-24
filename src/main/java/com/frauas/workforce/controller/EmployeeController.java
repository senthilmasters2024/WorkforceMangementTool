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
            summary = "Get employee by ID",
            description = "Retrieves a specific employee by their ID. Accessible to all authenticated users."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employee",
                    content = @Content(schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'PROJECT_MANAGER', 'DEPARTMENT_HEAD', 'RESOURCE_PLANNER', 'EMPLOYEE')")
    public ResponseEntity<Employee> getEmployeeById(
            @Parameter(description = "ID of the employee to retrieve")
            @PathVariable String id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
        Employee createdEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
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
            @Parameter(description = "ID of the employee to update")
            @PathVariable String id,
            @Parameter(description = "Updated employee object")
            @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "ID of the employee to delete")
            @PathVariable String id) {
        if (employeeService.deleteEmployee(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

package com.frauas.workforce.controller;

import com.frauas.workforce.DTO.ErrorResponse;
import com.frauas.workforce.model.Application;
import com.frauas.workforce.service.ProjectHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProjectHistoryController
 *
 * REST API controller for viewing employee project history.
 *
 * Logic:
 * - When DH approves: employeeProjectStartDate = project.projectStart, employeeProjectEndDate = project.projectEnd
 * - History (past projects): status = COMPLETED AND projectEnd < today
 * - Active projects: status = COMPLETED AND projectEnd >= today
 *
 * No PM action needed - projects automatically become history when projectEnd passes
 */
@RestController
@RequestMapping("/api/project-history")
@Tag(name = "Project History", description = "APIs for viewing employee project history")
@CrossOrigin(origins = "*")
public class ProjectHistoryController {

    @Autowired
    private ProjectHistoryService projectHistoryService;

    /**
     * Get employee's project history (all completed projects)
     */
    @Operation(
            summary = "Get employee project history",
            description = "Retrieves all past/completed projects for an employee. " +
                    "Returns applications with status COMPLETED where projectEnd < today, sorted by end date (most recent first)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved project history",
                    content = @Content(schema = @Schema(implementation = Application.class)))
    })
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Application>> getEmployeeProjectHistory(
            @Parameter(description = "Employee ID", required = true)
            @PathVariable Integer employeeId
    ) {
        List<Application> history = projectHistoryService.getEmployeeProjectHistory(employeeId);
        return ResponseEntity.ok(history);
    }

    /**
     * Get all employees who worked on a specific project
     */
    @Operation(
            summary = "Get project employee history",
            description = "Retrieves all employees who have worked on a specific project (past projects only). " +
                    "Returns applications with status COMPLETED where projectEnd < today for the given project."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employee history for project",
                    content = @Content(schema = @Schema(implementation = Application.class)))
    })
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Application>> getProjectEmployeeHistory(
            @Parameter(description = "Project ID", required = true)
            @PathVariable String projectId
    ) {
        List<Application> history = projectHistoryService.getProjectEmployeeHistory(projectId);
        return ResponseEntity.ok(history);
    }

    /**
     * Get employee's current active project
     */
    @Operation(
            summary = "Get employee's current active project",
            description = "Retrieves the currently active project assignment for an employee. " +
                    "Returns applications with status COMPLETED where projectEnd >= today. " +
                    "Returns null if employee is not assigned to any project or project has ended."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved current project",
                    content = @Content(schema = @Schema(implementation = Application.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/employee/{employeeId}/current")
    public ResponseEntity<?> getCurrentActiveProject(
            @Parameter(description = "Employee ID", required = true)
            @PathVariable Integer employeeId
    ) {
        try {
            Application currentProject = projectHistoryService.getCurrentActiveProject(employeeId);

            if (currentProject == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Employee is not currently assigned to any project");
                response.put("application", null);
                return ResponseEntity.ok(response);
            }

            return ResponseEntity.ok(currentProject);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value()));
        }
    }
}

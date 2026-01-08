package com.frauas.workforce.controller;

import com.frauas.workforce.DTO.ApproveApplicationRequest;
import com.frauas.workforce.DTO.ErrorResponse;
import com.frauas.workforce.DTO.RejectApplicationRequest;
import com.frauas.workforce.model.Application;
import com.frauas.workforce.model.ApplicationStatus;
import com.frauas.workforce.service.DepartmentHeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department-head")
@Tag(name = "Department Head", description = "Department Head management APIs for approving/rejecting employee applications")
@CrossOrigin(origins = "*")
public class DepartmentHeadController {

    @Autowired
    private DepartmentHeadService departmentHeadService;

    /**
     * Approve an employee's application for a project
     */
    @Operation(
            summary = "Approve employee application for project",
            description = "Department Head approves an employee's application to join a project. " +
                    "Only applications from employees in the same department can be approved."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application approved successfully",
                    content = @Content(schema = @Schema(implementation = Application.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request - wrong status or already approved",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - can only approve applications for your department",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Application not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/applications/{applicationId}/approve")
    public ResponseEntity<?> approveApplication(
            @Parameter(description = "Unique application ID", required = true)
            @PathVariable String applicationId,
            @Parameter(description = "Approval request with department head ID and optional comments")
            @RequestBody ApproveApplicationRequest request
    ) {
        try {
            Application approvedApplication = departmentHeadService.approveApplication(
                    applicationId,
                    request.getDepartmentHeadId(),
                    request.getComments()
            );
            return ResponseEntity.ok(approvedApplication);

        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();

            // Determine appropriate status code based on error message
            if (errorMessage.contains("not found")) {
                ErrorResponse error = new ErrorResponse(errorMessage, HttpStatus.NOT_FOUND.value());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            } else if (errorMessage.contains("department") || errorMessage.contains("Only Department Heads")) {
                ErrorResponse error = new ErrorResponse(errorMessage, HttpStatus.FORBIDDEN.value());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            } else {
                ErrorResponse error = new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
        }
    }

    /**
     * Reject an employee's application for a project
     */
    @Operation(
            summary = "Reject employee application for project",
            description = "Department Head rejects an employee's application to join a project. " +
                    "Rejection reason is mandatory. Only applications from employees in the same department can be rejected."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application rejected successfully",
                    content = @Content(schema = @Schema(implementation = Application.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request - missing rejection reason or wrong status",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - can only reject applications for your department",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Application not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/applications/{applicationId}/reject")
    public ResponseEntity<?> rejectApplication(
            @Parameter(description = "Unique application ID", required = true)
            @PathVariable String applicationId,
            @Parameter(description = "Rejection request with department head ID and mandatory rejection reason")
            @RequestBody RejectApplicationRequest request
    ) {
        try {
            Application rejectedApplication = departmentHeadService.rejectApplication(
                    applicationId,
                    request.getDepartmentHeadId(),
                    request.getRejectionReason()
            );
            return ResponseEntity.ok(rejectedApplication);

        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();

            // Determine appropriate status code based on error message
            if (errorMessage.contains("not found")) {
                ErrorResponse error = new ErrorResponse(errorMessage, HttpStatus.NOT_FOUND.value());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            } else if (errorMessage.contains("department") || errorMessage.contains("Only Department Heads")) {
                ErrorResponse error = new ErrorResponse(errorMessage, HttpStatus.FORBIDDEN.value());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            } else {
                ErrorResponse error = new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
        }
    }

    /**
     * Get all applications for department employees
     */
    @Operation(
            summary = "Get all applications for department",
            description = "Department Head views all applications for employees in their department. " +
                    "By default returns ALL applications. Can optionally filter by status (APPLIED, APPROVED, REJECTED, etc.)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved applications",
                    content = @Content(schema = @Schema(implementation = Application.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - user is not a Department Head",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Department Head not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/applications")
    public ResponseEntity<?> getDepartmentApplications(
            @Parameter(description = "Department Head employee ID", required = true)
            @RequestParam Integer departmentHeadId,
            @Parameter(description = "Optional status filter (APPLIED, SUGGESTED, APPROVED, REJECTED, etc.). If not provided, returns ALL applications.")
            @RequestParam(required = false) ApplicationStatus status
    ) {
        try {
            List<Application> applications = departmentHeadService.getApplicationsForDepartment(
                    departmentHeadId,
                    status
            );
            return ResponseEntity.ok(applications);

        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();

            // Determine appropriate status code based on error message
            if (errorMessage.contains("not found")) {
                ErrorResponse error = new ErrorResponse(errorMessage, HttpStatus.NOT_FOUND.value());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            } else if (errorMessage.contains("Only Department Heads")) {
                ErrorResponse error = new ErrorResponse(errorMessage, HttpStatus.FORBIDDEN.value());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            } else {
                ErrorResponse error = new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
        }
    }
}

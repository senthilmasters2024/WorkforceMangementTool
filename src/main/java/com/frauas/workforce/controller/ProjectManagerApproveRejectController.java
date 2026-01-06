package com.frauas.workforce.controller;

import com.frauas.workforce.DTO.ErrorResponse;
import com.frauas.workforce.DTO.RejectByProjectManagerRequest;
import com.frauas.workforce.DTO.RequestDepartmentHeadApprovalRequest;
import com.frauas.workforce.model.Application;
import com.frauas.workforce.service.ProjectManagerService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-manager")
@Tag(name = "Project Manager", description = "Project Manager APIs for reviewing and forwarding applications")
@CrossOrigin(origins = "*")
public class ProjectManagerApproveRejectController {

    private final ProjectManagerService projectManagerService;

    public ProjectManagerApproveRejectController(ProjectManagerService projectManagerService) {
        this.projectManagerService = projectManagerService;
    }

    /**
     * View suggested applications
     */
    @Operation(
            summary = "View suggested applications",
            description = "Project Manager views employee suggestions made by Resource Planner"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved suggested applications"),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/applications/suggested")
    public ResponseEntity<List<Application>> getAllSuggestedApplications() {
        return ResponseEntity.ok(
                projectManagerService.getAllSuggestedApplications()
        );
    }

    /**
     * Request Department Head approval
     */
    @Operation(
            summary = "Request Department Head approval",
            description = "Project Manager requests Department Head approval for a suggested employee"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Approval request sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid application status",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Application not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/applications/{applicationId}/request-dh-approval")
    public ResponseEntity<?> requestDhApproval(
            @PathVariable String applicationId,
            @RequestBody RequestDepartmentHeadApprovalRequest request
    ) {
        try {
            Application application = projectManagerService.requestDepartmentHeadApproval(
                    applicationId,
                    request.getProjectManagerId(),
                    request.getComments()
            );
            return ResponseEntity.ok(application);

        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("not found")
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.BAD_REQUEST;

            return ResponseEntity.status(status)
                    .body(new ErrorResponse(e.getMessage(), status.value()));
        }
    }

    /**
     * Reject application
     */
    @Operation(
            summary = "Reject suggested application",
            description = "Project Manager rejects a suggested employee"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application rejected successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status or missing reason",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Application not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/applications/{applicationId}/reject")
    public ResponseEntity<?> rejectApplication(
            @PathVariable String applicationId,
            @RequestBody RejectByProjectManagerRequest request
    ) {
        try {
            Application application = projectManagerService.rejectByProjectManager(
                    applicationId,
                    request.getProjectManagerId(),
                    request.getRejectionReason()
            );
            return ResponseEntity.ok(application);

        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("not found")
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.BAD_REQUEST;

            return ResponseEntity.status(status)
                    .body(new ErrorResponse(e.getMessage(), status.value()));
        }
    }

    @Operation(
            summary = "View applied applications",
            description = "Project Manager views all applied project applications"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved applied applications")
    })
    @GetMapping("/applications/applied")
    public ResponseEntity<List<Application>> getAppliedApplications() {
        return ResponseEntity.ok(
                projectManagerService.getAppliedApplications()
        );
    }

    /**
     * Mark employee project as completed (changes status to PROJECT_COMPLETED)
     */
    @Operation(
            summary = "Mark employee project as completed",
            description = "Project Manager marks an employee's project work as completed. " +
                    "Changes status from COMPLETED to PROJECT_COMPLETED, " +
                    "sets employeeProjectEndDate to today, " +
                    "and frees up the employee making them available for new projects."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status changed to PROJECT_COMPLETED successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status or employee not assigned",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Application not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/applications/{applicationId}/mark-completed")
    public ResponseEntity<?> markEmployeeProjectCompleted(
            @PathVariable String applicationId,
            @RequestParam(required = false) String comments
    ) {
        try {
            Application application = projectManagerService.markEmployeeProjectCompleted(
                    applicationId,
                    comments
            );

            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("application", application);
            response.put("message", "Status changed to PROJECT_COMPLETED. Employee is now available for new assignments.");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("not found")
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.BAD_REQUEST;

            return ResponseEntity.status(status)
                    .body(new ErrorResponse(e.getMessage(), status.value()));
        }
    }
}

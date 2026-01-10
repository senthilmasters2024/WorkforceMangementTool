package com.frauas.workforce.DTO;

import com.frauas.workforce.model.ApplicationStatus;
import com.frauas.workforce.model.Timestamps;
import com.frauas.workforce.model.UserAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponseDTO {
    private String id;
    private String applicationId;
    private String projectId;
    private Integer employeeId;
    private String projectRole;
    private ApplicationStatus currentStatus;
    private Integer requestedCapacity;
//    private Integer approvedCapacity;

    private UserActionDTO initiatedBy;
    private UserActionDTO suggestedBy;
    private UserActionDTO approvedByProjectManager;
    private UserActionDTO approvedByDepartmentHead;
    private UserActionDTO rejectedBy;

    private Timestamps timestamps;

    public ApplicationResponseDTO(String id, String applicationId, String projectId, Integer employeeId, String projectRole, ApplicationStatus currentStatus, Integer requestedCapacity, Integer approvedCapacity, UserAction initiatedBy, UserAction suggestedBy, UserAction approvedByDepartmentHead, UserAction rejectedBy, UserAction approvedByProjectManager, Timestamps timestamps) {
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserActionDTO {
        private String userId;
        private String username; // include username for response
        private String role;
    }
}

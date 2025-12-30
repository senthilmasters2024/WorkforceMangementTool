package com.frauas.workforce.DTO;

import com.frauas.workforce.model.ApplicationStatus;
import com.frauas.workforce.model.Timestamps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponseDTO {
    private String id;
    private String projectId;
    private Integer employeeId;
    private ApplicationStatus currentStatus;
    private Integer requestedCapacity;
    private Integer approvedCapacity;

    private UserActionDTO initiatedBy;
    private UserActionDTO suggestedBy;
    private UserActionDTO approvedBy;
    private UserActionDTO confirmedBy;

    private Timestamps timestamps;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserActionDTO {
        private String userId;
        private String username; // include username for response
        private String role;
    }
}

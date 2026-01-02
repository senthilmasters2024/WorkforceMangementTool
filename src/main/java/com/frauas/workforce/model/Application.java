package com.frauas.workforce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Application {

    @Id
    private String id;

    @Indexed(unique = true)
    private String applicationId;

    private String projectRole;
    private String projectId;
    private Integer employeeId;

    private ApplicationStatus currentStatus;

    private UserAction initiatedBy;
    private UserAction suggestedBy;
    private UserAction approvedBy;
    private UserAction rejectedBy;
    private UserAction confirmedBy;

    private UserAction appliedAt;

    private String approvalComments;
    private String rejectionReason;

    private Timestamps timestamps;

    // Getters and Setters
}
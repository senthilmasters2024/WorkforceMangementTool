package com.frauas.workforce.DTO;

import lombok.Data;

@Data
public class RejectByProjectManagerRequest {
    private String projectManagerId;
    private String rejectionReason;
}
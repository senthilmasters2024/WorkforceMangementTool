package com.frauas.workforce.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Timestamps {
    private Date appliedAt;
    private Date suggestedAt;
    private Date approvedAt;
    private Date assignedAt;

    public void setRejectedAt(Instant now) {
    }

    public void setRejectionReason(String rejectionReason) {
    }

    public void setPmComments(String comments) {
    }
}

package com.frauas.workforce.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestProjectRequest {
    private String projectId;
    private Integer employeeId;

    private Integer requestedCapacity;

    private String plannerUserId; //
}

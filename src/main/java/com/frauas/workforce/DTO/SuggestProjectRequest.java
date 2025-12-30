package com.frauas.workforce.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestProjectRequest {
    private String projectId;
    private String projectRole;
    private Integer employeeId;

    private String plannerUserId; //
}

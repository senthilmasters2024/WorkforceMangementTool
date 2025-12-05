package com.frauas.workforce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Experience {
    private String role;
    private String company;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}

package com.frauas.workforce.model;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String position;
}

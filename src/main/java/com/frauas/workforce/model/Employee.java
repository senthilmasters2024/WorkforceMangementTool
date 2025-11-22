package com.frauas.workforce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Employee {

    @Id
    private String id;

    @Field("employeeId")
    private Integer employeeId;

    private String userId;

    @Field("remoteWorking")
    private Boolean remoteWorking;

    @Indexed(unique = true)
    @Field("username")
    private String username;

    @Field("password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Field("firstName")
    private String firstName;

    @Field("lastName")
    private String lastName;

    @Indexed(unique = true)
    @Field("email")
    private String email;

    @Field("department")
    private String department;

    @Field("position")
    private String position;

    @Field("role")
    private Role role;

    @Field("message")
    private String message;
}

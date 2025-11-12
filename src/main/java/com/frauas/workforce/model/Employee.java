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

    @Indexed(unique = true)
    @Field("username")
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

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

    public Employee(Object o, String admin, String admin123, String system, String admin1, String mail, String it, String systemAdministrator, com.frauas.workforce.model.Role role) {
    }
}

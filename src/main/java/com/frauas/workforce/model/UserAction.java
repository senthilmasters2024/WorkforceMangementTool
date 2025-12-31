package com.frauas.workforce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserAction {
    private String userId;
    private String role;

    public UserAction(String userId, Role role) {
        this.userId = userId;
        this.role = role.name(); // store enum as String
    }
}

package com.frauas.workforce.controller;

import com.frauas.workforce.model.Employee;
import com.frauas.workforce.repository.EmployeeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "Authentication and session management endpoints")
public class AuthController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Login with username and password", description = "Login with username and password. Returns employee details and role.")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username is required"));
        }

        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password is required"));
        }

        Employee employee = employeeRepository.findByUsername(username)
                .orElse(null);

        if (employee == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }

        // Validate password
        if (!passwordEncoder.matches(password, employee.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }

        // Store user in session
        session.setAttribute("userId", employee.getId());
        session.setAttribute("username", employee.getUsername());
        session.setAttribute("role", employee.getRole().name());

        Map<String, Object> response = new HashMap<>();
        response.put("id", employee.getId());
        response.put("username", employee.getUsername());
        response.put("firstName", employee.getFirstName());
        response.put("lastName", employee.getLastName());
        response.put("email", employee.getEmail());
        response.put("department", employee.getDepartment());
        response.put("position", employee.getPosition());
        response.put("role", employee.getRole());
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Logout and invalidate session")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    @GetMapping("/current-user")
    @Operation(summary = "Get current logged-in user", description = "Returns the current user's details from session")
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        Employee employee = employeeRepository.findById(userId)
                .orElse(null);

        if (employee == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", employee.getId());
        response.put("username", employee.getUsername());
        response.put("firstName", employee.getFirstName());
        response.put("lastName", employee.getLastName());
        response.put("email", employee.getEmail());
        response.put("department", employee.getDepartment());
        response.put("position", employee.getPosition());
        response.put("role", employee.getRole());

        return ResponseEntity.ok(response);
    }
}

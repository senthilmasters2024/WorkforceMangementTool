package com.frauas.workforce.service;

import com.frauas.workforce.model.Employee;
import com.frauas.workforce.model.Role;
import com.frauas.workforce.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostConstruct
    public void init() {
        // Add sample data with different roles if database is empty
        if (employeeRepository.count() == 0) {
            // System Admins (password: admin123)
            Employee admin = new Employee(null, 0, java.util.UUID.randomUUID().toString(), false,
                    "admin", passwordEncoder.encode("admin123"),
                    "System", "Admin", "admin@workforce.com", "IT", "System Administrator", Role.SYSTEM_ADMIN, "msg");
            employeeRepository.save(admin);

            // Project Managers (password: pm123)
            Employee projectManager1 = new Employee(null, 0, java.util.UUID.randomUUID().toString(), false,
                    "pm_john", passwordEncoder.encode("pm123"),
                    "John", "Doe", "john.doe@workforce.com", "IT", "Project Manager", Role.PROJECT_MANAGER, "msg");
            employeeRepository.save(projectManager1);

            Employee projectManager2 = new Employee(null, 0, java.util.UUID.randomUUID().toString(), false,
                    "pm_sarah", passwordEncoder.encode("pm123"),
                    "Sarah", "Connor", "sarah.connor@workforce.com", "Engineering", "Project Manager", Role.PROJECT_MANAGER, "msg");
            employeeRepository.save(projectManager2);

            // Department Heads (password: dh123)
            Employee deptHead1 = new Employee(null, 0, java.util.UUID.randomUUID().toString(), false,
                    "dh_jane", passwordEncoder.encode("dh123"),
                    "Jane", "Smith", "jane.smith@workforce.com", "HR", "Department Head", Role.DEPARTMENT_HEAD, "msg");
            employeeRepository.save(deptHead1);

            Employee deptHead2 = new Employee(null, 0, java.util.UUID.randomUUID().toString(), false,
                    "dh_michael", passwordEncoder.encode("dh123"),
                    "Michael", "Brown", "michael.brown@workforce.com", "Finance", "Department Head", Role.DEPARTMENT_HEAD, "msg");
            employeeRepository.save(deptHead2);

            // Resource Planners (password: rp123)
            Employee planner1 = new Employee(null, 0, java.util.UUID.randomUUID().toString(), false,
                    "rp_bob", passwordEncoder.encode("rp123"),
                    "Bob", "Johnson", "bob.johnson@workforce.com", "Operations", "Resource Planner", Role.RESOURCE_PLANNER, "msg");
            employeeRepository.save(planner1);

            Employee planner2 = new Employee(null, 0, java.util.UUID.randomUUID().toString(), false,
                    "rp_lisa", passwordEncoder.encode("rp123"),
                    "Lisa", "Davis", "lisa.davis@workforce.com", "HR", "Resource Planner", Role.RESOURCE_PLANNER, "msg");
            employeeRepository.save(planner2);

            // Regular Employees (password: emp123)
            Employee employee1 = new Employee(null, 0, java.util.UUID.randomUUID().toString(), false,
                    "emp_alice", passwordEncoder.encode("emp123"),
                    "Alice", "Williams", "alice.williams@workforce.com", "IT", "Software Engineer", Role.EMPLOYEE, "msg");
            employeeRepository.save(employee1);

            Employee employee2 = new Employee(null, 0, java.util.UUID.randomUUID().toString(), false,
                    "emp_tom", passwordEncoder.encode("emp123"),
                    "Tom", "Anderson", "tom.anderson@workforce.com", "IT", "Backend Developer", Role.EMPLOYEE, "msg");
            employeeRepository.save(employee2);

            Employee employee3 = new Employee(null, 0, java.util.UUID.randomUUID().toString(), false,
                    "emp_emma", passwordEncoder.encode("emp123"),
                    "Emma", "Wilson", "emma.wilson@workforce.com", "Marketing", "Marketing Specialist", Role.EMPLOYEE, "msg");
            employeeRepository.save(employee3);

            Employee employee4 = new Employee(null, 0, java.util.UUID.randomUUID().toString(), false,
                    "emp_david", passwordEncoder.encode("emp123"),
                    "David", "Martinez", "david.martinez@workforce.com", "Finance", "Financial Analyst", Role.EMPLOYEE, "msg");
            employeeRepository.save(employee4);

            Employee employee5 = new Employee(null, 0, java.util.UUID.randomUUID().toString(), false,
                    "emp_sophia", passwordEncoder.encode("emp123"),
                    "Sophia", "Garcia", "sophia.garcia@workforce.com", "HR", "HR Specialist", Role.EMPLOYEE, "msg");
            employeeRepository.save(employee5);
        }
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    public Employee createEmployee(Employee employee) {
        // Generate userId if not provided
        if (employee.getUserId() == null || employee.getUserId().isEmpty()) {
            employee.setUserId(java.util.UUID.randomUUID().toString());
        }

        // Encode password before saving
        if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        }


        // Generate sequential employee ID if not provided
        if (employee.getEmployeeId() == null || employee.getEmployeeId() == 0) {
            employee.setEmployeeId(generateNextEmployeeId());
        }

        return employeeRepository.save(employee);
    }

    public Optional<Employee> updateEmployee(String id, Employee employeeDetails) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setUsername(employeeDetails.getUsername());
            employee.setFirstName(employeeDetails.getFirstName());
            employee.setLastName(employeeDetails.getLastName());
            employee.setEmail(employeeDetails.getEmail());
            employee.setDepartment(employeeDetails.getDepartment());
            employee.setPosition(employeeDetails.getPosition());
            employee.setRole(employeeDetails.getRole());
            employee.setEmployeeId(employeeDetails.getEmployeeId());
            employee.setRemoteWorking(employeeDetails.getRemoteWorking());
            employee.setUserId(employeeDetails.getUserId());

            // Only update password if provided
            if (employeeDetails.getPassword() != null && !employeeDetails.getPassword().isEmpty()) {
                employee.setPassword(passwordEncoder.encode(employeeDetails.getPassword()));
            }

            return employeeRepository.save(employee);
        });
    }


    public Integer generateNextEmployeeId() {
        Optional<Employee> lastEmployee = employeeRepository.findTopByOrderByEmployeeIdDesc();
        return lastEmployee.map(emp -> emp.getEmployeeId() + 1).orElse(1);
    }

    public boolean deleteEmployee(String id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

package com.frauas.workforce.service;

import com.frauas.workforce.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EmployeeService {
    private final List<Employee> employees = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);


    public EmployeeService() {
        // Add some sample data
        employees.add(new Employee(idCounter.getAndIncrement(), "John", "Doe", "john.doe@example.com", "IT", "Software Engineer"));
        employees.add(new Employee(idCounter.getAndIncrement(), "Jane", "Smith", "jane.smith@example.com", "HR", "HR Manager"));
        employees.add(new Employee(idCounter.getAndIncrement(), "Bob", "Johnson", "bob.johnson@example.com", "Finance", "Financial Analyst"));
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employees.stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst();
    }

    public Employee createEmployee(Employee employee) {
        employee.setId(idCounter.getAndIncrement());
        employees.add(employee);
        return employee;
    }

    public Optional<Employee> updateEmployee(Long id, Employee employeeDetails) {
        return getEmployeeById(id).map(employee -> {
            employee.setFirstName(employeeDetails.getFirstName());
            employee.setLastName(employeeDetails.getLastName());
            employee.setEmail(employeeDetails.getEmail());
            employee.setDepartment(employeeDetails.getDepartment());
            employee.setPosition(employeeDetails.getPosition());
            return employee;
        });
    }

    public boolean deleteEmployee(Long id) {
        return employees.removeIf(emp -> emp.getId().equals(id));
    }
}

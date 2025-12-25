package com.frauas.workforce.service;

import com.frauas.workforce.model.AvailabilityStatus;
import com.frauas.workforce.model.ContractType;
import com.frauas.workforce.model.Employee;
import com.frauas.workforce.model.Experience;
import com.frauas.workforce.model.Role;
import com.frauas.workforce.repository.EmployeeRepository;
import com.frauas.workforce.repository.ProjectManagerRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
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

    @Autowired
    private ProjectManagerRepository projectManagerRepository;


    @PostConstruct
    public void init() {
        // Add sample data with different roles if database is empty
        if (employeeRepository.count() == 0) {
            // System Admins (password: admin123)
            Employee admin = new Employee(null, null, java.util.UUID.randomUUID().toString(), false,
                    "admin", "admin123",
                    "System", "Admin", "admin@workforce.com", "IT", "System Administrator", Role.SYSTEM_ADMIN, "msg",
                    Arrays.asList("System Administration", "Network Management", "Security"),
                    Arrays.asList("Cybersecurity", "Cloud Computing", "Automation"),
                    "Frankfurt",
                    Arrays.asList("Frankfurt", "Berlin"),
                    "+49-123-4567890",
                    AvailabilityStatus.AVAILABLE,
                    ContractType.FULL_TIME,
                    100,
                    Arrays.asList(
                            new Experience("Senior System Admin", "Tech Corp", LocalDate.of(2018, 1, 1), LocalDate.of(2023, 12, 31), "Managed enterprise IT infrastructure"),
                            new Experience("IT Administrator", "StartUp GmbH", LocalDate.of(2015, 6, 1), LocalDate.of(2017, 12, 31), "Handled network and security operations")
                    ), null, null);
            createEmployee(admin);

            // Project Managers (password: pm123)
            Employee projectManager1 = new Employee(null, null, java.util.UUID.randomUUID().toString(), false,
                    "pm_john", "pm123",
                    "John", "Doe", "john.doe@workforce.com", "IT", "Project Manager", Role.PROJECT_MANAGER, "msg",
                    Arrays.asList("Project Management", "Agile", "Scrum", "Leadership"),
                    Arrays.asList("Team Building", "Innovation", "Technology Trends"),
                    "Berlin",
                    Arrays.asList("Berlin", "Munich", "Hamburg"),
                    "+49-234-5678901",
                    AvailabilityStatus.PARTIALLY_AVAILABLE,
                    ContractType.FULL_TIME,
                    80,
                    Arrays.asList(
                            new Experience("Project Manager", "Digital Solutions AG", LocalDate.of(2020, 3, 1), null, "Leading multiple software development projects"),
                            new Experience("Scrum Master", "Innovation Labs", LocalDate.of(2017, 9, 1), LocalDate.of(2020, 2, 28), "Facilitated agile teams")
                    ), null, null);
            createEmployee(projectManager1);

            Employee projectManager2 = new Employee(null, null, java.util.UUID.randomUUID().toString(), false,
                    "pm_sarah", "pm123",
                    "Sarah", "Connor", "sarah.connor@workforce.com", "Engineering", "Project Manager", Role.PROJECT_MANAGER, "msg",
                    Arrays.asList("Project Management", "Risk Management", "Stakeholder Management"),
                    Arrays.asList("Process Improvement", "Mentoring", "Strategic Planning"),
                    "Munich",
                    Arrays.asList("Munich", "Frankfurt"),
                    "+49-345-6789012",
                    AvailabilityStatus.AVAILABLE,
                    ContractType.FULL_TIME,
                    100,
                    Arrays.asList(
                            new Experience("Senior Project Manager", "Engineering Firm", LocalDate.of(2019, 1, 1), null, "Managing large-scale engineering projects"),
                            new Experience("Team Lead", "Tech Innovations", LocalDate.of(2016, 4, 1), LocalDate.of(2018, 12, 31), "Led cross-functional teams")
                    ), null, null);
            createEmployee(projectManager2);

            // Department Heads (password: dh123)
            Employee deptHead1 = new Employee(null, null, java.util.UUID.randomUUID().toString(), false,
                    "dh_jane", "dh123",
                    "Jane", "Smith", "jane.smith@workforce.com", "HR", "Department Head", Role.DEPARTMENT_HEAD, "msg",
                    Arrays.asList("HR Management", "Recruitment", "Employee Relations", "Performance Management"),
                    Arrays.asList("Organizational Development", "Coaching", "Diversity & Inclusion"),
                    "Hamburg",
                    Arrays.asList("Hamburg", "Berlin"),
                    "+49-456-7890123",
                    AvailabilityStatus.AVAILABLE,
                    ContractType.FULL_TIME,
                    100,
                    Arrays.asList(
                            new Experience("HR Department Head", "Global Enterprise", LocalDate.of(2021, 1, 1), null, "Leading HR strategy and operations"),
                            new Experience("HR Manager", "Corporate Solutions", LocalDate.of(2018, 3, 1), LocalDate.of(2020, 12, 31), "Managed recruitment and employee development")
                    ), null, null);
            createEmployee(deptHead1);

            Employee deptHead2 = new Employee(null, null, java.util.UUID.randomUUID().toString(), false,
                    "dh_michael", "dh123",
                    "Michael", "Brown", "michael.brown@workforce.com", "Finance", "Department Head", Role.DEPARTMENT_HEAD, "msg",
                    Arrays.asList("Financial Planning", "Budgeting", "Financial Analysis", "Strategic Planning"),
                    Arrays.asList("Investment Strategies", "Business Analytics", "Leadership"),
                    "Frankfurt",
                    Arrays.asList("Frankfurt", "Munich"),
                    "+49-567-8901234",
                    AvailabilityStatus.AVAILABLE,
                    ContractType.FULL_TIME,
                    100,
                    Arrays.asList(
                            new Experience("Finance Department Head", "Financial Services Inc", LocalDate.of(2020, 6, 1), null, "Overseeing financial planning and analysis"),
                            new Experience("Senior Financial Analyst", "Investment Bank", LocalDate.of(2016, 1, 1), LocalDate.of(2020, 5, 31), "Financial modeling and forecasting")
                    ), null, null);
            createEmployee(deptHead2);

            // Resource Planners (password: rp123)
            Employee planner1 = new Employee(null, null, java.util.UUID.randomUUID().toString(), false,
                    "rp_bob", "rp123",
                    "Bob", "Johnson", "bob.johnson@workforce.com", "Operations", "Resource Planner", Role.RESOURCE_PLANNER, "msg",
                    Arrays.asList("Resource Planning", "Capacity Management", "Scheduling", "Forecasting"),
                    Arrays.asList("Operations Optimization", "Data Visualization", "Problem Solving"),
                    "Berlin",
                    Arrays.asList("Berlin", "Hamburg", "Frankfurt"),
                    "+49-678-9012345",
                    AvailabilityStatus.AVAILABLE,
                    ContractType.FULL_TIME,
                    100,
                    Arrays.asList(
                            new Experience("Resource Planner", "Operations Hub", LocalDate.of(2019, 7, 1), null, "Managing resource allocation and capacity planning"),
                            new Experience("Operations Coordinator", "Logistics Pro", LocalDate.of(2017, 2, 1), LocalDate.of(2019, 6, 30), "Coordinated scheduling and resources")
                    ), null, null);
            createEmployee(planner1);

            Employee planner2 = new Employee(null, null, java.util.UUID.randomUUID().toString(), false,
                    "rp_lisa", "rp123",
                    "Lisa", "Davis", "lisa.davis@workforce.com", "HR", "Resource Planner", Role.RESOURCE_PLANNER, "msg",
                    Arrays.asList("Workforce Planning", "Resource Allocation", "Data Analysis"),
                    Arrays.asList("HR Technology", "Predictive Analytics", "Continuous Improvement"),
                    "Munich",
                    Arrays.asList("Munich", "Berlin"),
                    "+49-789-0123456",
                    AvailabilityStatus.PARTIALLY_AVAILABLE,
                    ContractType.FULL_TIME,
                    75,
                    Arrays.asList(
                            new Experience("HR Resource Planner", "Tech Company", LocalDate.of(2020, 9, 1), null, "Workforce planning and analytics"),
                            new Experience("HR Analyst", "Consulting Firm", LocalDate.of(2018, 5, 1), LocalDate.of(2020, 8, 31), "Analyzed workforce trends and capacity")
                    ), null, null);
            createEmployee(planner2);

            // Regular Employees (password: emp123)
            Employee employee1 = new Employee(null, null, java.util.UUID.randomUUID().toString(), false,
                    "emp_alice", "emp123",
                    "Alice", "Williams", "alice.williams@workforce.com", "IT", "Software Engineer", Role.EMPLOYEE, "msg",
                    Arrays.asList("Java", "Spring Boot", "React", "Microservices"),
                    Arrays.asList("Open Source", "AI/ML", "Gaming", "Hiking"),
                    "Frankfurt",
                    Arrays.asList("Frankfurt", "Remote"),
                    "+49-890-1234567",
                    AvailabilityStatus.AVAILABLE,
                    ContractType.FULL_TIME,
                    100,
                    Arrays.asList(
                            new Experience("Software Engineer", "Tech Solutions GmbH", LocalDate.of(2021, 4, 1), null, "Developing microservices and web applications"),
                            new Experience("Junior Developer", "Code Factory", LocalDate.of(2019, 8, 1), LocalDate.of(2021, 3, 31), "Full-stack development with React and Java")
                    ), null, null);
            createEmployee(employee1);

            Employee employee2 = new Employee(null, null, java.util.UUID.randomUUID().toString(), false,
                    "emp_tom", "emp123",
                    "Tom", "Anderson", "tom.anderson@workforce.com", "IT", "Backend Developer", Role.EMPLOYEE, "msg",
                    Arrays.asList("Python", "Django", "REST API", "PostgreSQL"),
                    Arrays.asList("DevOps", "Cloud Architecture", "Photography", "Cycling"),
                    "Berlin",
                    Arrays.asList("Berlin", "Remote"),
                    "+49-901-2345678",
                    AvailabilityStatus.NOT_AVAILABLE,
                    ContractType.FULL_TIME,
                    0,
                    Arrays.asList(
                            new Experience("Backend Developer", "Digital Platform AG", LocalDate.of(2020, 10, 1), null, "Building scalable backend systems with Python"),
                            new Experience("Python Developer", "Startup Inc", LocalDate.of(2018, 6, 1), LocalDate.of(2020, 9, 30), "API development and database design")
                    ), null, null);
            createEmployee(employee2);

            Employee employee3 = new Employee(null, null, java.util.UUID.randomUUID().toString(), false,
                    "emp_emma", "emp123",
                    "Emma", "Wilson", "emma.wilson@workforce.com", "Marketing", "Marketing Specialist", Role.EMPLOYEE, "msg",
                    Arrays.asList("Digital Marketing", "SEO", "Content Strategy", "Social Media"),
                    Arrays.asList("Content Creation", "Brand Strategy", "Travel", "Blogging"),
                    "Munich",
                    Arrays.asList("Munich", "Hamburg"),
                    "+49-012-3456789",
                    AvailabilityStatus.AVAILABLE,
                    ContractType.PART_TIME,
                    60,
                    Arrays.asList(
                            new Experience("Marketing Specialist", "Brand Agency", LocalDate.of(2022, 1, 1), null, "Digital marketing campaigns and SEO optimization"),
                            new Experience("Content Manager", "Media House", LocalDate.of(2020, 3, 1), LocalDate.of(2021, 12, 31), "Content strategy and social media management")
                    ), null, null);
            createEmployee(employee3);

            Employee employee4 = new Employee(null, null, java.util.UUID.randomUUID().toString(), false,
                    "emp_david", "emp123",
                    "David", "Martinez", "david.martinez@workforce.com", "Finance", "Financial Analyst", Role.EMPLOYEE, "msg",
                    Arrays.asList("Financial Modeling", "Excel", "Data Analysis", "Forecasting"),
                    Arrays.asList("Economics", "Stock Market", "Chess", "Reading"),
                    "Hamburg",
                    Arrays.asList("Hamburg", "Frankfurt"),
                    "+49-123-4567890",
                    AvailabilityStatus.PARTIALLY_AVAILABLE,
                    ContractType.TEMPORARY,
                    50,
                    Arrays.asList(
                            new Experience("Financial Analyst", "Finance Corp", LocalDate.of(2021, 11, 1), LocalDate.of(2024, 12, 31), "Financial modeling and forecasting for projects"),
                            new Experience("Junior Analyst", "Investment Firm", LocalDate.of(2020, 2, 1), LocalDate.of(2021, 10, 31), "Data analysis and reporting")
                    ), null, null);
            createEmployee(employee4);

            Employee employee5 = new Employee(null, null, java.util.UUID.randomUUID().toString(), false, "emp_sophia", "emp123",
                    "Sophia", "Garcia", "sophia.garcia@workforce.com", "HR", "HR Specialist", Role.EMPLOYEE, "msg",
                    Arrays.asList("Recruitment", "Onboarding", "Employee Engagement", "HR Analytics"),
                    Arrays.asList("Psychology", "Team Dynamics", "Yoga", "Volunteering"),
                    "Frankfurt",
                    Arrays.asList("Frankfurt", "Berlin"),
                    "+49-234-5678901",
                    AvailabilityStatus.AVAILABLE,
                    ContractType.WORKING_STUDENT,
                    40,
                    Arrays.asList(
                            new Experience("HR Specialist - Working Student", "Corporate HR", LocalDate.of(2023, 9, 1), null, "Supporting recruitment and onboarding processes"),
                            new Experience("HR Intern", "Business Services", LocalDate.of(2023, 3, 1), LocalDate.of(2023, 8, 31), "Assisted with employee engagement initiatives")
                    ), null, null);
            createEmployee(employee5);
        }
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> getEmployeeByEmployeeId(Integer employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
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

        // Validate supervisor and assigned project
        validateSupervisor(employee.getEmployeeId(), employee.getSupervisor());
        validateAssignedProject(employee.getAssignedProjectId());

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
            employee.setMessage(employeeDetails.getMessage());
            employee.setSkills(employeeDetails.getSkills());
            employee.setInterests(employeeDetails.getInterests());
            employee.setBaseLocation(employeeDetails.getBaseLocation());
            employee.setPreferredLocations(employeeDetails.getPreferredLocations());
            employee.setEmergencyContact(employeeDetails.getEmergencyContact());
            employee.setAvailabilityStatus(employeeDetails.getAvailabilityStatus());
            employee.setContractType(employeeDetails.getContractType());
            employee.setCapacity(employeeDetails.getCapacity());
            employee.setWorkExperience(employeeDetails.getWorkExperience());
            employee.setSupervisor(employeeDetails.getSupervisor());
            employee.setAssignedProjectId(employeeDetails.getAssignedProjectId());

            // Only update password if provided
            if (employeeDetails.getPassword() != null && !employeeDetails.getPassword().isEmpty()) {
                employee.setPassword(passwordEncoder.encode(employeeDetails.getPassword()));
            }

            // Validate supervisor and assigned project
            validateSupervisor(employee.getEmployeeId(), employee.getSupervisor());
            validateAssignedProject(employee.getAssignedProjectId());

            return employeeRepository.save(employee);
        });
    }

    public Optional<Employee> updateEmployeeByEmployeeId(Integer employeeId, Employee employeeDetails) {
        return employeeRepository.findByEmployeeId(employeeId).map(employee -> {
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
            employee.setMessage(employeeDetails.getMessage());
            employee.setSkills(employeeDetails.getSkills());
            employee.setInterests(employeeDetails.getInterests());
            employee.setBaseLocation(employeeDetails.getBaseLocation());
            employee.setPreferredLocations(employeeDetails.getPreferredLocations());
            employee.setEmergencyContact(employeeDetails.getEmergencyContact());
            employee.setAvailabilityStatus(employeeDetails.getAvailabilityStatus());
            employee.setContractType(employeeDetails.getContractType());
            employee.setCapacity(employeeDetails.getCapacity());
            employee.setWorkExperience(employeeDetails.getWorkExperience());
            employee.setSupervisor(employeeDetails.getSupervisor());
            employee.setAssignedProjectId(employeeDetails.getAssignedProjectId());

            // Only update password if provided
            if (employeeDetails.getPassword() != null && !employeeDetails.getPassword().isEmpty()) {
                employee.setPassword(passwordEncoder.encode(employeeDetails.getPassword()));
            }

            // Validate supervisor and assigned project
            validateSupervisor(employee.getEmployeeId(), employee.getSupervisor());
            validateAssignedProject(employee.getAssignedProjectId());

            return employeeRepository.save(employee);
        });
    }


    public Integer generateNextEmployeeId() {
        Optional<Employee> lastEmployee = employeeRepository.findTopByOrderByEmployeeIdDesc();
        return lastEmployee.map(emp -> emp.getEmployeeId() + 1).orElse(1);
    }

    /**
     * Validates that a supervisor employee ID exists and is not self-referencing.
     */
    private void validateSupervisor(Integer employeeId, Integer supervisorId) {
        if (supervisorId == null) {
            return; // Null supervisor is allowed
        }

        if (supervisorId.equals(employeeId)) {
            throw new IllegalArgumentException("Employee cannot be their own supervisor");
        }

        Optional<Employee> supervisor = employeeRepository.findByEmployeeId(supervisorId);
        if (!supervisor.isPresent()) {
            throw new IllegalArgumentException("Supervisor with employeeId " + supervisorId + " does not exist");
        }
    }

    /**
     * Validates that an assigned project ID exists in the database.
     */
    private void validateAssignedProject(String projectId) {
        if (projectId == null || projectId.isEmpty()) {
            return; // Null/empty project assignment is allowed
        }

        if (!projectManagerRepository.existsByProjectId(projectId)) {
            throw new IllegalArgumentException("Project with projectId '" + projectId + "' does not exist");
        }
    }

    public boolean deleteEmployee(String id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean deleteEmployeeByEmployeeId(Integer employeeId) {
        Optional<Employee> employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee.isPresent()) {
            employeeRepository.deleteById(employee.get().getId());
            return true;
        }
        return false;
    }
}

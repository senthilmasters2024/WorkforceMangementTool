package com.frauas.workforce.repository;

import com.frauas.workforce.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Optional<Employee> findByUsername(String username);
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findTopByOrderByEmployeeIdDesc();
}


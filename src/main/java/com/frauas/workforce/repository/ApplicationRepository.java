package com.frauas.workforce.repository;

import com.frauas.workforce.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByEmployeeId(Integer employeeId);
}

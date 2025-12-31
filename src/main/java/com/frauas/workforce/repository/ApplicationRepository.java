package com.frauas.workforce.repository;

import com.frauas.workforce.model.Application;
import com.frauas.workforce.model.ApplicationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByEmployeeId(Integer employeeId);
    List<Application> findByEmployeeIdAndCurrentStatus(
            Integer employeeId,
            ApplicationStatus currentStatus
    );
    Optional<Application> findByApplicationId(String applicationId);
    Optional<Application> findByEmployeeIdAndProjectIdAndProjectRole(
            Integer employeeId,
            String projectId,
            String projectRole
    );

}

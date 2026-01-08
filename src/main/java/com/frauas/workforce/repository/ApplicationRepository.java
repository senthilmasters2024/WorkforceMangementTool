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
//    List<Application> findByEmployeeIdAndCurrentStatus(
//            Integer employeeId,
//            ApplicationStatus currentStatus
//    );
    Optional<Application> findByApplicationId(String applicationId);
    Optional<Application> findByEmployeeIdAndProjectIdAndProjectRole(
            Integer employeeId,
            String projectId,
            String projectRole
    );

    List<Application> findByCurrentStatus(ApplicationStatus status);
    /**
     * Find application by business applicationId
     */
//    Optional<Application> findByApplicationId(String applicationId);

    /**
     * PM: Get all suggested applications for a project
     */
    List<Application> findByProjectIdAndCurrentStatus(
            String projectId,
            ApplicationStatus status
    );

    /**
     * PM / DH: Get applications by project and multiple statuses
     */
    List<Application> findByProjectIdAndCurrentStatusIn(
            String projectId,
            List<ApplicationStatus> statuses
    );

    /**
     * DH: Get applications for multiple employees
     */
    List<Application> findByEmployeeIdIn(List<Integer> employeeIds);

    /**
     * DH: Get applications for department employees filtered by status
     */
    List<Application> findByEmployeeIdInAndCurrentStatus(
            List<Integer> employeeIds,
            ApplicationStatus status
    );

    /**
     * DH: Get applications for department employees (all statuses)
     */
    List<Application> findByEmployeeIdInAndCurrentStatusIn(
            List<Integer> employeeIds,
            List<ApplicationStatus> statuses
    );

    /**
     * Fetch all applications for a project (audit / reporting)
     */
    List<Application> findByProjectId(String projectId);
}

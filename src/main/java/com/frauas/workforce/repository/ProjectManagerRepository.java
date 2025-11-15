package com.frauas.workforce.repository;

import com.frauas.workforce.model.Project;
import com.frauas.workforce.model.ProjectStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProjectManagerRepository extends MongoRepository<Project, String>{
    // Find all published projects
    List<Project> findByIsPublished(Boolean isPublished);

    // Find projects by status
    List<Project> findByStatus(ProjectStatus status);

    // Find projects by created by (Project Manager)
    List<Project> findByCreatedBy(String createdBy);

    // Find published projects by status
    List<Project> findByIsPublishedAndStatus(Boolean isPublished, ProjectStatus status);

    // Custom query to find projects by location
    @Query("{ 'location': { $regex: ?0, $options: 'i' } }")
    List<Project> findByLocationContainingIgnoreCase(String location);
}

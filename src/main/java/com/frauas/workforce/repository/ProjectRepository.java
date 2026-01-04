package com.frauas.workforce.repository;

import com.frauas.workforce.model.Project;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProjectRepository {
    Optional<Project> findByProjectId(String projectId);
}

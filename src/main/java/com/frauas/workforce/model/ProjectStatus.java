package com.frauas.workforce.model;

/**
 * ProjectStatus Enum
 *
 * Represents the various lifecycle stages of a project in the workforce management system.
 * Projects transition through these statuses from initial planning to completion.
 *
 * @author Workforce Management Team
 * @version 1.0
 * @since 2025-11-16
 */
public enum ProjectStatus {

    /**
     * PLANNED - Initial status when a project is being planned.
     * The project is still in the design and planning phase.
     * Not yet ready for staffing or execution.
     */
    PLANNED,

    /**
     * OPEN - Project is finalized and open for team formation.
     * Requirements are defined and ready to accept applications or assignments.
     */
    OPEN,

    /**
     * STAFFING - Project is actively being staffed.
     * Team members are being recruited, interviewed, and assigned to the project.
     */
    STAFFING,

    /**
     * ACTIVE - Project is currently in progress.
     * Team is working on deliverables and tasks are being executed.
     */
    ACTIVE,

    /**
     * COMPLETED - Project has been finished.
     * All deliverables are complete and the project is closed.
     */
    COMPLETED
}

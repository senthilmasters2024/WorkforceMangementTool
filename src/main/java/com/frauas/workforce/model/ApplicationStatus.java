package com.frauas.workforce.model;

public enum ApplicationStatus {
    DRAFT,
    APPLIED,
    SUGGESTED,
    REQUEST_DH_APPROVAL,
    REJECTED_BY_PM,
    REJECTED_BY_DH,
    COMPLETED,

    // Old statuses for backward compatibility (deprecated)
    @Deprecated
    APPROVAL_PENDING_PM,
    @Deprecated
    APPROVED,
    @Deprecated
    REJECTED,
    @Deprecated
    ASSIGNED,
    @Deprecated
    WITHDRAWN
}

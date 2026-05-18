package com.thitsaworks.operation_portal.component.misc.util;

public enum ActionCategory {

    AUTHENTICATION_AND_ACCOUNT_SECURITY("Authentication & Account Security"),

    USER_MANAGEMENT("User Management"),

    ROLE_MENU_PERMISSION_IAM("Role / Menu / Permission / IAM"),

    PARTICIPANT_MANAGEMENT("Participant Management"),

    PARTICIPANT_PROFILE_AND_FINANCIAL_CONFIGURATION(
        "Participant Profile / NDC / Currency / Position"),

    CONTACT_MANAGEMENT("Contact Management"),

    LIQUIDITY_PROFILE("Liquidity Profile"),

    ANNOUNCEMENT_AND_GREETING_CONTENT("Announcement & Greeting Content"),

    APPROVAL_WORKFLOW("Approval Workflow"),

    SETTLEMENT_CORE_OPERATIONS("Settlement Core Operations"),

    SETTLEMENT_MODEL_MANAGEMENT("Settlement Model Management"),

    TRANSFER_OPERATIONS("Transfer Operations"),

    REPORTING("Reporting"),

    AUDIT_AND_LOGS("Audit & Logs"),

    SCHEDULER_AND_JOB_CONFIGURATION("Scheduler / Job Configuration"),

    SYSTEM_JOBS_AND_SCHEDULED_EXECUTORS("Scheduled Job Executors / System Jobs");

    private final String displayName;

    ActionCategory(String displayName) {

        this.displayName = displayName;
    }

    public String getDisplayName() {

        return displayName;
    }
}

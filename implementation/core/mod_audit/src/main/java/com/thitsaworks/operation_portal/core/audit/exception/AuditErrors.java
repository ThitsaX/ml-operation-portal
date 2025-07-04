package com.thitsaworks.operation_portal.core.audit.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class AuditErrors {

    //@@formatter:off
    public static final ErrorMessage AUDIT_NOT_FOUND = new ErrorMessage("AUDIT_NOT_FOUND", "The system cannot find the audit with provided ID.");
    public static final ErrorMessage USER_NOT_FOUND = new ErrorMessage("USER_NOT_FOUND", "System cannot find the user with provided ID.");
    //@@formatter:on
}

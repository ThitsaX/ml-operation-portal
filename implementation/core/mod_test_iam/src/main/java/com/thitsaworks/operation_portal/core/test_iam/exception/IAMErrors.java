package com.thitsaworks.operation_portal.core.test_iam.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class IAMErrors {

    //@@formatter:off
    public static final ErrorMessage DUPLICATE_PRINCIPAL = new ErrorMessage("DUPLICATE_PRINCIPAL", "Duplicate principal for user with ID ({0})");
    public static final ErrorMessage PASSWORD_AUTHENTICATION_FAILURE = new ErrorMessage("PASSWORD_AUTHENTICATION_FAILURE", "Password authentication is failed.");
    public static final ErrorMessage PRINCIPAL_NOT_FOUND = new ErrorMessage("PRINCIPAL_NOT_FOUND", "Principal is not found for the user.");
    public static final ErrorMessage UNAUTHORIZED_CREATION = new ErrorMessage("UNAUTHORIZED_CREATION", "Unauthorized creation for other participant's user.");
    public static final ErrorMessage PERMISSION_DENIED = new ErrorMessage("PERMISSION_DENIED", "Access denied: You do not have permission to perform this action.");

    public static final ErrorMessage ROLE_NOT_FOUND = new ErrorMessage("ROLE_NOT_FOUND","System cannot find Role.");
    public static final ErrorMessage ROLE_ALREADY_ASSIGN_TO_USER = new ErrorMessage("ROLE_ALREADY_ASSIGN_TO_USER","Role already assign to user");
    public static final ErrorMessage ACTION_NOT_FOUND = new ErrorMessage("ACTION_NOT_FOUND","System cannot find Action.");
    public static final ErrorMessage BLOCKED_ACTION_NOT_FOUND = new ErrorMessage("BLOCKED_ACTION_NOT_FOUND","System cannot find Blocked action.");
    public static final ErrorMessage USER_ROLE_NOT_FOUND = new ErrorMessage("USER_ROLE_NOT_FOUND","System cannot find User role.");
    public static final ErrorMessage USER_NOT_FOUND = new ErrorMessage("USER_NOT_FOUND","System cannot find User.");
    public static final ErrorMessage ROLE_GRANT_NOT_FOUND = new ErrorMessage("ROLE_GRANT_NOT_FOUND","System cannot find Role grant.");
    public static final ErrorMessage USER_GRANT_NOT_FOUND = new ErrorMessage("USER_GRANT_NOT_FOUND","System cannot find User grant.");



    public static final ErrorMessage DUPLICATE_ROLE_NAME = new ErrorMessage("DUPLICATE_ROLE_NAME","Duplicate role name ");
    //@@formatter:on
}

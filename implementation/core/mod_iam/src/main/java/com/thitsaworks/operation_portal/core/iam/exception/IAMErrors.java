package com.thitsaworks.operation_portal.core.iam.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class IAMErrors {

    //@@formatter:off

    public static final ErrorMessage PRINCIPAL_NOT_FOUND = new ErrorMessage("PRINCIPAL_NOT_FOUND", "System cannot find the principle for the User with provided ID: [{0}].");
    public static final ErrorMessage ROLE_NOT_FOUND = new ErrorMessage("ROLE_NOT_FOUND","System cannot find the Role with provided ID: [{0}].");
    public static final ErrorMessage ACTION_NOT_FOUND = new ErrorMessage("ACTION_NOT_FOUND","System cannot find the Action  with provided ID: [{0}].");
    public static final ErrorMessage MENU_NOT_FOUND = new ErrorMessage("MENU_NOT_FOUND","System cannot find the Menu with provided ID: [{0}].");

    public static final ErrorMessage DUPLICATE_PRINCIPAL = new ErrorMessage("DUPLICATE_PRINCIPAL", "Duplicate principal for user : [{0}].");
    public static final ErrorMessage DUPLICATE_ROLE_NAME = new ErrorMessage("DUPLICATE_ROLE_NAME","Duplicate role : [{0}].");

    public static final ErrorMessage ROLE_ALREADY_ASSIGNED = new ErrorMessage("ROLE_ALREADY_ASSIGNED","The Role has already assigned to the user : [{0}].");

    public static final ErrorMessage PASSWORD_AUTHENTICATION_FAILURE = new ErrorMessage("PASSWORD_AUTHENTICATION_FAILURE", "Password authentication is failed.");
    public static final ErrorMessage OLD_PASSWORD_MISMATCH = new ErrorMessage("OLD_PASSWORD_MISMATCH", "The old password you entered is incorrect. Password cannot be changed.");
    public static final ErrorMessage UNAUTHORIZED_CREATION = new ErrorMessage("UNAUTHORIZED_CREATION", "The other participant's user is not authorized to create.");
    public static final ErrorMessage UNAUTHORIZED_ROLE_CREATION = new ErrorMessage("UNAUTHORIZED_ROLE_CREATION", "The role is not authorized to create.");
    public static final ErrorMessage UNAUTHORIZED_USER_ACCESS = new ErrorMessage("UNAUTHORIZED_USER_ACCESS", "The user is not authorized to view.");
    public static final ErrorMessage PERMISSION_DENIED = new ErrorMessage("PERMISSION_DENIED", "Access denied: You do not have permission to perform this action : [{0}].");

    public static final ErrorMessage INVALID_ROLE_NAME = new ErrorMessage("INVALID_ROLE_NAME", "Role Name is required and must NOT be blank or empty.");
    public static final ErrorMessage SELF_APPROVAL_NOT_ALLOWED = new ErrorMessage("SELF_APPROVAL_NOT_ALLOWED", "You cannot approve or reject your own request. Please ask another authorized user.");
    public static final ErrorMessage INACTIVE_STATUS_CHANGE_NOT_ALLOWED = new ErrorMessage("INACTIVE_STATUS_CHANGE_NOT_ALLOWED", "You cannot change your own inactive status.");

    //@@formatter:on
}

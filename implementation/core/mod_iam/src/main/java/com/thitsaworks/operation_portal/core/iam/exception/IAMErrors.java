package com.thitsaworks.operation_portal.core.iam.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class IAMErrors {

    //@@formatter:off
    public static final ErrorMessage DUPLICATE_PRINCIPAL = new ErrorMessage("DUPLICATE_PRINCIPAL", "Duplicate principal for user with ID ({0})");
    public static final ErrorMessage PASSWORD_AUTHENTICATION_FAILURE = new ErrorMessage("PASSWORD_AUTHENTICATION_FAILURE", "Password authentication is failed.");
    public static final ErrorMessage PRINCIPAL_NOT_FOUND = new ErrorMessage("PRINCIPAL_NOT_FOUND", "Principal is not found for the user.");
    public static final ErrorMessage UNAUTHORIZED_CREATION = new ErrorMessage("UNAUTHORIZED_CREATION", "Unauthorized creation for other participant's user.");
    public static final ErrorMessage PERMISSION_DENIED = new ErrorMessage("PERMISSION_DENIED", "Access denied: You do not have permission to perform this action.");
    //@@formatter:on
}

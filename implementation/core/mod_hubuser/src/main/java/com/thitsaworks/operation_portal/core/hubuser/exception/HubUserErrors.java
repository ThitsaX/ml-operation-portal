package com.thitsaworks.operation_portal.core.hubuser.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class HubUserErrors {

    //@@formatter:off
    public static final ErrorMessage ALREADY_ANNOUNCED = new ErrorMessage("ALREADY_ANNOUNCED", "The announcement already existed.");
    public static final ErrorMessage ANNOUNCEMENT_NOT_FOUND = new ErrorMessage("ANNOUNCEMENT_NOT_FOUND", "System cannot find the announcement.");
    public static final ErrorMessage USER_NOT_FOUND = new ErrorMessage("USER_NOT_FOUND", "System cannot find the user with provided ID.");
    public static final ErrorMessage EMAIL_ALREADY_REGISTERED = new ErrorMessage("EMAIL_ALREADY_REGISTERED", "The provided email has already registered in the system.");
    //@@formatter:on
}

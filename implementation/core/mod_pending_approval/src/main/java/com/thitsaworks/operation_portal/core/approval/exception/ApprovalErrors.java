package com.thitsaworks.operation_portal.core.approval.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class ApprovalErrors {

    //@@formatter:off
    public static final ErrorMessage INVALID_REQUESTED_ACTION = new ErrorMessage("INVALID_REQUESTED_ACTION", "Requested Action is required and must NOT be blank or empty.");
    public static final ErrorMessage INVALID_DFSP = new ErrorMessage("INVALID_DFSP", "For DFSP is required and must NOT be blank or empty.");
    public static final ErrorMessage INVALID_CURRENCY = new ErrorMessage("INVALID_CURRENCY", "Currency is required and must NOT be blank or empty.");
    public static final ErrorMessage INVALID_AMOUNT = new ErrorMessage("INVALID_AMOUNT", "Amount is required and must NOT be blank or empty.");
    public static final ErrorMessage INVALID_REQUESTED_BY = new ErrorMessage("INVALID_REQUESTED_BY", "Requested By is required and must NOT be blank or empty.");
    public static final ErrorMessage INVALID_RESPONDED_BY = new ErrorMessage("INVALID_RESPONDED_BY", "Responded By is required and must NOT be blank or empty.");
    public static final ErrorMessage APPROVAL_REQUEST_NOT_FOUND = new ErrorMessage("APPROVAL_REQUEST_NOT_FOUND", "Approval Request does not exist in System!");

    //@@formatter:on

}

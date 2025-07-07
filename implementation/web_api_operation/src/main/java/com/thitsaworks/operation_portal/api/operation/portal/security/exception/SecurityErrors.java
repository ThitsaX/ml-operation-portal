package com.thitsaworks.operation_portal.api.operation.portal.security.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class SecurityErrors {

    //@@formatter:off
    public static final ErrorMessage PUBLIC_KEY_INFO_NOT_FOUND = new ErrorMessage("PUBLIC_KEY_INFO_NOT_FOUND", "Public key info is not found.");
    public static final ErrorMessage INVALID_KEY_NO = new ErrorMessage("INVALID_KEY_NO", "Invalid key no.");
    public static final ErrorMessage PUBLIC_KEY_DISABLE = new ErrorMessage("PUBLIC_KEY_DISABLE", "Public key is disabled.");
    public static final ErrorMessage JWT_TOKEN_NOT_FOUND = new ErrorMessage("JWT_TOKEN_NOT_FOUND", "JWT token is not found in the Authorization header.");
    public static final ErrorMessage INVALID_PAYLOAD = new ErrorMessage("INVALID_PAYLOAD", "Authentication failure. Invalid payload.");
    public static final ErrorMessage AUTHENTICATION_FAILED = new ErrorMessage("AUTHENTICATION_FAILED", "Authentication failed. Unable to generate a valid Auth-Header at the backend.");
    public static final ErrorMessage JWT_TOKEN_VERIFICATION_ERROR = new ErrorMessage("JWT_TOKEN_VERIFICATION_ERROR", "JWT token verification error.");
    public static final ErrorMessage MISSING_MANDATORY_ELEMENT_OR_SYNTAX = new ErrorMessage("MISSING_MANDATORY_ELEMENT_OR_SYNTAX", "Mandatory element in the data model was missing.");
    public static final ErrorMessage TRANSACTION_NOT_FOUND = new ErrorMessage("TRANSACTION_NOT_FOUND", "Transaction is not found.");
    public static final ErrorMessage JSON_UI_NOT_FOUND = new ErrorMessage("JSON_UI_NOT_FOUND", "Json UI is not found.");
    public static final ErrorMessage JSON_PROCESSING_ERROR = new ErrorMessage("JSON_PROCESSING_ERROR", "Something went wrong in Json processing.");
    public static final ErrorMessage ACCOUNT_INACTIVE = new ErrorMessage("ACCOUNT_INACTIVE", "Account is inactive.");
    //@@formatter:on
}

package com.thitsaworks.operation_portal.core.hub_services.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class HubServicesErrors {

    //@@formatter:off
    public static final ErrorMessage CENTRAL_LEDGER_FAILURE_EXCEPTION = new ErrorMessage("CENTRAL_LEDGER_FAILURE_EXCEPTION", "Central Ledger failure encountered.");
    public static final ErrorMessage HUB_PARTICIPANT_NOT_FOUND = new ErrorMessage("HUB_PARTICIPANT_NOT_FOUND", "System cannot find the participant on Hub.");
    public static final ErrorMessage HUB_TRANSACTION_NOT_FOUND = new ErrorMessage("TRANSACTION_NOT_FOUND", "System cannot find the transaction with provided transferID.");


    public static final ErrorMessage CONNECTION_ERROR = new ErrorMessage("CONNECTION_ERROR", "Cannot connect to the hub service!");
    public static final ErrorMessage INVALID_SETTLEMENT_STATE = new ErrorMessage("INVALID_SETTLEMENT_STATE", "The settlement state is invalid.");
    public static final ErrorMessage INVALID_SETTLEMENT = new ErrorMessage("INVALID_SETTLEMENT", "The settlement state is invalid.");
    public static final ErrorMessage SETTLEMENT_ACCOUNT_NOT_FOUND = new ErrorMessage("SETTLEMENT_ACCOUNT_NOT_FOUND", "System cannot find the settlement account for the participant with given currency on Hub.");

    public static final ErrorMessage SETTLEMENT_WINDOW_NOT_FOUND = new ErrorMessage("SETTLEMENT_WINDOW_NOT_FOUND", "System cannot find the settlement window for the participant with given currency on Hub.");
    //@@formatter:on
}

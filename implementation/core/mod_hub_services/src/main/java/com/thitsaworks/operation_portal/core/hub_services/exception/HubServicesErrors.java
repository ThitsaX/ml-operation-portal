package com.thitsaworks.operation_portal.core.hub_services.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class HubServicesErrors {

    //@@formatter:off
    public static final ErrorMessage CENTRAL_LEDGER_FAILURE_EXCEPTION = new ErrorMessage("CENTRAL_LEDGER_FAILURE_EXCEPTION", "Central Ledger failure encountered.");
    public static final ErrorMessage HUB_PARTICIPANT_NOT_FOUND = new ErrorMessage("HUB_PARTICIPANT_NOT_FOUND", "System cannot find the participant with provided name on Hub.");
    public static final ErrorMessage HUB_TRANSACTION_NOT_FOUND = new ErrorMessage("TRANSACTION_NOT_FOUND", "System cannot find the transaction with provided transferID.");


    public static final ErrorMessage CONNECTION_ERROR = new ErrorMessage("CONNECTION_ERROR", "Cannot connect to the hub service!");
    //@@formatter:on
}

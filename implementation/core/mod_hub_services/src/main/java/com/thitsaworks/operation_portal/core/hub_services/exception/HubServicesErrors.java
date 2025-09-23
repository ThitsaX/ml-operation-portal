package com.thitsaworks.operation_portal.core.hub_services.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class HubServicesErrors {

    //@@formatter:off
    public static final ErrorMessage CONNECTION_ERROR = new ErrorMessage("CONNECTION_ERROR", "Failed to connect to the hub service!");
    public static final ErrorMessage HUB_TRANSFER_ERROR = new ErrorMessage("HUB_TRANSFER_ERROR", "Failed to process the operation for Transaction on Hub.");
    public static final ErrorMessage HUB_CURRENCY_ERROR = new ErrorMessage("HUB_CURRENCY_ERROR", "Failed to process the operation for Currency on Hub.");
    public static final ErrorMessage PARTY_IDENTIFIER_TYPE_ID  = new ErrorMessage("PARTY_IDENTIFIER_TYPE_ID", "Failed to process the operation for Party Identifier Type on Hub.");
    public static final ErrorMessage SETTLEMENT_WINDOW_ERROR = new ErrorMessage("SETTLEMENT_WINDOW_ERROR", "Failed to process the operation for Settlement Window on Hub.");
    public static final ErrorMessage SETTLEMENT_ERROR = new ErrorMessage("SETTLEMENT_ERROR", "Failed to process the operation for Settlement on Hub.");
    public static final ErrorMessage HUB_PARTICIPANT_ERROR = new ErrorMessage("HUB_PARTICIPANT_ERROR", "Failed to process the operation for Participant on Hub.");
    public static final ErrorMessage HUB_PARTICIPANT_BALANCE_ERROR = new ErrorMessage("HUB_PARTICIPANT_BALANCE_ERROR", "Failed to get the participant's balance on Hub.");
    public static final ErrorMessage HUB_PARTICIPANT_POSITION_ERROR = new ErrorMessage("HUB_PARTICIPANT_POSITION_ERROR", "Failed to get the participant's position data on Hub.");

    //@@formatter:on
}

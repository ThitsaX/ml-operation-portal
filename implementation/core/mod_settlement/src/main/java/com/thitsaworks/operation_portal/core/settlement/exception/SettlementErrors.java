package com.thitsaworks.operation_portal.core.settlement.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class SettlementErrors {

    //@@formatter:off
    public static final ErrorMessage SCHEDULER_ALREADY_ADDED = new ErrorMessage("SCHEDULER_ALREADY_ADDED", "Scheduler config : [{0}] already added to Settlement Model.");
    public static final ErrorMessage SETTLEMENT_MODEL_NOT_AUTO_CLOSE_WINDOW = new ErrorMessage("SETTLEMENT_MODEL_NOT_AUTO_CLOSE_WINDOW", "The Settlement Model : [{0}] is not configured to close windows automatically.");
    public static final ErrorMessage SETTLEMENT_MODEL_NOT_FOUND = new ErrorMessage("SETTLEMENT_MODEL_NOT_FOUND", "System cannot find the Settlement Model with provided ID : [{0}].");
    public static final ErrorMessage SETTLEMENT_MODEL_SCHEDULER_NOT_FOUND = new ErrorMessage("SETTLEMENT_MODEL_SCHEDULER_NOT_FOUND", "System cannot find the Settlement Model with provided Scheduler Config ID : [{0}].");
    public static final ErrorMessage SETTLEMENT_MODEL_ALREADY_REGISTERED = new ErrorMessage("SETTLEMENT_MODEL_ALREADY_REGISTERED", "The provided settlement model name : [{0}] has already registered in the system.");
    public static final ErrorMessage SETTLEMENT_SCHEDULER_OVERLAP = new ErrorMessage("SETTLEMENT_SCHEDULER_OVERLAP", "The settlement model : [{0}] has already registered scheduler with the same time. Please consider to edit existing one to reschedule extra days.");
    //@@formatter:on
}

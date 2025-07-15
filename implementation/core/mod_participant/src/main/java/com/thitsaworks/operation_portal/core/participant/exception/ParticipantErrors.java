package com.thitsaworks.operation_portal.core.participant.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class ParticipantErrors {

    //@@formatter:off
    public static final ErrorMessage CONTACT_NOT_FOUND = new ErrorMessage("CONTACT_NOT_FOUND", "System cannot find the contact with provided ID.");
    public static final ErrorMessage EMAIL_ALREADY_REGISTERED = new ErrorMessage("EMAIL_ALREADY_REGISTERED", "The provided email has already registered in the system.");
    public static final ErrorMessage EMAIL_NOT_FOUND = new ErrorMessage("EMAIL_NOT_FOUND", "System cannot find the provided email.");
    public static final ErrorMessage LIQUIDITY_PROFILE_NOT_FOUND = new ErrorMessage("LIQUIDITY_PROFILE_NOT_FOUND", "System cannot find the liquidity profile with provided ID");
    public static final ErrorMessage PARTICIPANT_ALREADY_REGISTER = new ErrorMessage("PARTICIPANT_ALREADY_REGISTER", "The Participant has already registered in the system with provided DFSP Code");
    public static final ErrorMessage PARTICIPANT_NOT_FOUND = new ErrorMessage("PARTICIPANT_NOT_FOUND", "System cannot find the participant with provided ID.");
    public static final ErrorMessage USER_NOT_FOUND = new ErrorMessage("USER_NOT_FOUND", "System cannot find the user with provided ID.");
    public static final ErrorMessage CONTACT_ALREADY_REGISTERED = new ErrorMessage("CONTACT_ALREADY_REGISTERED", "The Contact has already registered in the system with provided Contact Type.");

    //@@formatter:on
}

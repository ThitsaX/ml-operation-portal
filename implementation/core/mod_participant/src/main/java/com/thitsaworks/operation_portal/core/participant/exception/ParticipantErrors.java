package com.thitsaworks.operation_portal.core.participant.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class ParticipantErrors {

    //@@formatter:off
    public static final ErrorMessage CONTACT_NOT_FOUND = new ErrorMessage("CONTACT_NOT_FOUND", "System cannot find the contact with provided ID : [{0}].");
    public static final ErrorMessage EMAIL_NOT_FOUND = new ErrorMessage("EMAIL_NOT_FOUND", "System cannot find the email with provided ID : [{0}].");
    public static final ErrorMessage LIQUIDITY_PROFILE_NOT_FOUND = new ErrorMessage("LIQUIDITY_PROFILE_NOT_FOUND", "System cannot find the liquidity profile with provided ID : [{0}].");
    public static final ErrorMessage PARTICIPANT_NOT_FOUND = new ErrorMessage("PARTICIPANT_NOT_FOUND", "System cannot find the participant with provided ID : [{0}].");
    public static final ErrorMessage USER_NOT_FOUND = new ErrorMessage("USER_NOT_FOUND", "System cannot find the user with provided ID : [{0}].");
    public static final ErrorMessage PARTICIPANT_NDC_NOT_FOUND = new ErrorMessage("PARTICIPANT_NDC_NOT_FOUND", "System cannot find the participant ndc with provided ID : [{0}].");
    public static final ErrorMessage ANNOUNCEMENT_NOT_FOUND = new ErrorMessage("ANNOUNCEMENT_NOT_FOUND", "System cannot find the announcement with provided ID : [{0}].");
    public static final ErrorMessage GREETING_MESSAGE_NOT_FOUND = new ErrorMessage("GREETING_MESSAGE_NOT_FOUND", "System cannot find the Greeting Message with provided ID : [{0}].");

    public static final ErrorMessage EMAIL_ALREADY_REGISTERED = new ErrorMessage("EMAIL_ALREADY_REGISTERED", "The provided Email : [{0}] has already registered in the system.");
    public static final ErrorMessage PARTICIPANT_ALREADY_REGISTERED = new ErrorMessage("PARTICIPANT_ALREADY_REGISTERED", "The Participant has already registered in the system with provided DFSP Code : [{0}].");
    public static final ErrorMessage CONTACT_TYPE_ALREADY_REGISTERED = new ErrorMessage("CONTACT_TYPE_ALREADY_REGISTERED", "The provided Contact Type : [{0}] has already registered in the system.");
    public static final ErrorMessage LIQUIDITY_PROFILE_ALREADY_REGISTERED = new ErrorMessage("LIQUIDITY_PROFILE_ALREADY_REGISTERED", "The provided Currency : [{0}] for the Liquidity profile has already registered in the system.");
    public static final ErrorMessage ANNOUNCEMENT_ALREADY_REGISTERED = new ErrorMessage("ANNOUNCEMENT_ALREADY_REGISTERED", "The provided Announcement : [{0}] has already registered in the system.");
    public static final ErrorMessage GREETING_MESSAGE_ALREADY_REGISTERED = new ErrorMessage("GREETING_MESSAGE_ALREADY_REGISTERED", "The provided Greeting Message : [{0}] has already registered in the system.");

    public static final ErrorMessage USER_NOT_ACTIVE = new ErrorMessage("USER_NOT_ACTIVE", "The user is not active.");
    //@@formatter:on
}

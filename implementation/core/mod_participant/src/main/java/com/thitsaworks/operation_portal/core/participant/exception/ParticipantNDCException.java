package com.thitsaworks.operation_portal.core.participant.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class ParticipantNDCException extends DomainException {

    public ParticipantNDCException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}

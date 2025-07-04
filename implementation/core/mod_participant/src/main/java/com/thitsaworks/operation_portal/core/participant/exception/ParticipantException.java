package com.thitsaworks.operation_portal.core.participant.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class ParticipantException extends DomainException {

    public ParticipantException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}

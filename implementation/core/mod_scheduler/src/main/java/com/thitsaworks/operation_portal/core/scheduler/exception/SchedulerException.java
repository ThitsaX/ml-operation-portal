package com.thitsaworks.operation_portal.core.scheduler.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class SchedulerException extends DomainException {

    public SchedulerException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}

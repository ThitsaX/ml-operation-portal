package com.thitsaworks.operation_portal.core.hub_services.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class HubServicesException extends DomainException {

    public HubServicesException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}

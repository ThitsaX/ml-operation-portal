package com.thitsaworks.operation_portal.core.hub_services.exception;

import com.thitsaworks.operation_portal.component.fspiop.model.ErrorInformation;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class HubServicesApiException extends DomainException {

    @Getter(AccessLevel.PUBLIC)
    private final ErrorInformation errorInformation;

    public HubServicesApiException(ErrorInformation errorInformation) {

        super(new ErrorMessage(errorInformation.getErrorCode(), errorInformation.getErrorDescription()));

        this.errorInformation = errorInformation;

    }

}

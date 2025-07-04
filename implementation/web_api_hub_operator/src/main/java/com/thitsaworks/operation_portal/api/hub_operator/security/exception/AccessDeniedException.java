package com.thitsaworks.operation_portal.api.hub_operator.security.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccessDeniedException extends DomainException {

    public AccessDeniedException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}

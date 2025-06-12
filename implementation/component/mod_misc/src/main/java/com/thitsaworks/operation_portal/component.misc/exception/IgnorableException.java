package com.thitsaworks.operation_portal.component.misc.exception;

import lombok.NoArgsConstructor;

public abstract class IgnorableException extends DFSPPortalException {

    public IgnorableException() {

        super();
    }

    public IgnorableException(String errorMessage) {

        super(errorMessage);
    }

}

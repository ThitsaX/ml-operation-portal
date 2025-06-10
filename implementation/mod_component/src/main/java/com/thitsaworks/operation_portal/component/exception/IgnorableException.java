package com.thitsaworks.operation_portal.component.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class IgnorableException extends DFSPPortalException {

    protected IgnorableException(String params) {

        super(params);
    }

}

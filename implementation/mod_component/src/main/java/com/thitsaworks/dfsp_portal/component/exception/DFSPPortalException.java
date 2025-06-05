package com.thitsaworks.dfsp_portal.component.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class DFSPPortalException extends Exception {

    protected String params;

    protected DFSPPortalException(String params) {

        super();
        this.params = params;
    }

    public abstract String errorCode();

    public abstract String defaultErrorMessage();

    public abstract boolean requireTranslation();

    public String paramDescription() {

        return "No params required.";
    }

}

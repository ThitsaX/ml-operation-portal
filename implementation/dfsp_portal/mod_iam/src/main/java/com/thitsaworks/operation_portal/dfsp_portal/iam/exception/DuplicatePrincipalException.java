package com.thitsaworks.operation_portal.dfsp_portal.iam.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DFSPPortalException;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;

@NoArgsConstructor
public class DuplicatePrincipalException extends DFSPPortalException {

    public DuplicatePrincipalException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "DUPLICATE_PRINCIPAL";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format("Duplicate principal for user with ID ({0})", this.params);
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

    @Override
    public String paramDescription() {

        return "{0} : Principal ID";
    }

}

package com.thitsaworks.operation_portal.core.audit.exception;

import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;

@NoArgsConstructor
public class AuditNotFoundException extends OperationPortalException {

    public AuditNotFoundException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "AUDIT_NOT_FOUND";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format("The system is not found for the audit ({0})", this.params);
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

    @Override
    public String paramDescription() {

        return "{0} : Audit ID";
    }
}

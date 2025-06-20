package com.thitsaworks.operation_portal.component.misc.usecase;

import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.thitsaworks.operation_portal.component.exception.SystemProblemException;

public abstract class AbstractPublicUseCase<I, O> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPublicUseCase.class);

    public O execute(I input) throws OperationPortalException {

        LOG.info("Invoking UseCase : id : [{}], name :[{}]", this.getId(), this.getName());

        O output = null;

        try {

            output = this.onExecute(input);

        } catch (OperationPortalException e) {

            LOG.error("(OperationPortalException) Error : code: [{}], message :[{}]", e.errorCode(), e.defaultErrorMessage());
            LOG.error("Error details :", e);

            throw e;

        } catch (Exception e) {

            LOG.error("Error details :", e);

            throw new SystemProblemException(e.getMessage());
        }

        return output;
    }

    protected abstract O onExecute(I input) throws Exception;

    protected abstract String getName();

    protected abstract String getDescription();

    protected abstract String getScope();

    protected abstract String getId();

}